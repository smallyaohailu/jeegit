package io.jeegit.openapi.apikey;

import io.jeegit.common.TenantContext;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service that issues and validates API keys for partner applications.
 *
 * <p>Issued keys look like {@code jeegit_&lt;44 url-safe base64 chars&gt;}; the plaintext is
 * returned only once, at issue time. The server only ever stores the SHA-256 digest.
 */
@Service
public class ApiKeyService {

  private static final String KEY_PREFIX = "jeegit_";
  private static final SecureRandom RANDOM = new SecureRandom();
  private final ApiKeyRepository repository;

  public ApiKeyService(ApiKeyRepository repository) {
    this.repository = repository;
  }

  @Transactional
  public Map<String, Object> issue(String name, String owner) {
    String raw = generateRawKey();
    ApiKey entity = new ApiKey(digest(raw), name, owner);
    entity.setTenantId(TenantContext.tenant());
    ApiKey saved = repository.save(entity);
    return Map.of(
        "id",
        saved.getId(),
        "name",
        saved.getName(),
        "owner",
        saved.getOwner() == null ? "" : saved.getOwner(),
        "apiKey",
        raw,
        "digest",
        saved.getKeyDigest(),
        "warning",
        "Store this value now — the raw key is only shown once.");
  }

  @Transactional
  public void revoke(String id) {
    repository.findById(id).ifPresent(apiKey -> apiKey.setEnabled(false));
  }

  @Transactional(readOnly = true)
  public Optional<ApiKey> resolve(String presentedKey) {
    if (presentedKey == null || presentedKey.isBlank()) {
      return Optional.empty();
    }
    Optional<ApiKey> entity = repository.findByKeyDigest(digest(presentedKey));
    return entity.filter(ApiKey::isEnabled);
  }

  private static String generateRawKey() {
    byte[] bytes = new byte[32];
    RANDOM.nextBytes(bytes);
    return KEY_PREFIX + Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
  }

  static String digest(String raw) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] out = md.digest(raw.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder(out.length * 2);
      for (byte b : out) sb.append(String.format("%02x", b));
      return sb.toString();
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("SHA-256 not available", ex);
    }
  }
}
