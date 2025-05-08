package com.x.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

/**
 * Configuration class for setting up the default locale of the application.
 * <p>
 * This ensures that all incoming HTTP requests use English as the default language
 * unless explicitly specified in the request headers.
 */
@Configuration
public class LocaleConfig {

    /**
     * Defines a LocaleResolver bean that uses the "Accept-Language" header to determine the locale.
     *
     * @return a LocaleResolver with English set as the default locale
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(Locale.ENGLISH);
        return localeResolver;
    }

}
