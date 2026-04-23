package io.jeegit.openapi.apikey;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiKeyRepository extends JpaRepository<ApiKey, String> {
  Optional<ApiKey> findByKeyDigest(String keyDigest);
}
