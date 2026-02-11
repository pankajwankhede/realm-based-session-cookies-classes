package com.example.sso.config;

import com.example.sso.security.RealmCookieHttpSessionIdResolver;
import com.example.sso.security.RealmResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HttpSessionIdResolver;

@Configuration
public class SessionIdResolverConfig {

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver(RealmResolver realmResolver) {
        return new RealmCookieHttpSessionIdResolver(realmResolver);
    }
}
