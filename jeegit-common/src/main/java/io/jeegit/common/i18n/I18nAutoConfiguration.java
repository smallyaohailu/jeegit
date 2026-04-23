package io.jeegit.common.i18n;

import java.nio.charset.StandardCharsets;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

/**
 * Wires jeegit's internationalization stack: - UTF-8 message bundles loaded from {@code
 * classpath:i18n/messages} - Accept-Language based locale resolution, capped to the supported list
 * - {@code ?lang=xx} query parameter locale override for easy testing - A {@link WebMvcConfigurer}
 * to register the change interceptor
 *
 * <p>Client precedence: cookie (via LocaleChangeInterceptor param) > Accept-Language > JVM default
 * > {@link SupportedLocales#DEFAULT}.
 */
@Configuration
public class I18nAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean(MessageSource.class)
  public MessageSource messageSource() {
    ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
    ms.setBasename("classpath:i18n/messages");
    ms.setDefaultEncoding(StandardCharsets.UTF_8.name());
    ms.setFallbackToSystemLocale(false);
    ms.setUseCodeAsDefaultMessage(true);
    ms.setCacheSeconds(60);
    return ms;
  }

  @Bean(name = "localeResolver")
  @ConditionalOnMissingBean(name = "localeResolver")
  public LocaleResolver localeResolver() {
    return new JeegitLocaleResolver();
  }

  @Bean
  public LocaleChangeInterceptor localeChangeInterceptor() {
    LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
    interceptor.setParamName("lang");
    return interceptor;
  }

  @Bean
  public WebMvcConfigurer i18nWebMvcConfigurer(LocaleChangeInterceptor localeChangeInterceptor) {
    return new WebMvcConfigurer() {
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor);
      }
    };
  }
}
