package com.emlakjet.purchasing.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

/**
 * This is our config class where CORS settings are made.
 */
@Configuration
public class CorsConfig {

    private static final long MAX_AGE = 7200;   //2 hours (2 * 60 * 60)
    private static final String ALLOWED_ORIGIN = "*";
    private static final List<String> ALLOWED_METHODS = Arrays.asList("GET, PUT, POST, DELETE, OPTIONS, PATCH");
    private static final List<String> ALLOWED_HEADERS = Arrays.asList("X-Requested-With", "Origin", "Content-Type", "Accept",
            "Authorization", "remember-me", "x-auth-token", "X-XSRF-TOKEN", "credential", "Content-Length", "Content-Type",
            "access-control-allow-credentials", "access-control-allow-methods", "access-control-allow-origin");

    @Bean
    public FilterRegistrationBean corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setMaxAge(MAX_AGE);
        config.setAllowedMethods(ALLOWED_METHODS);
        config.addAllowedOrigin(ALLOWED_ORIGIN);
        config.setAllowedHeaders(ALLOWED_HEADERS);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

}
