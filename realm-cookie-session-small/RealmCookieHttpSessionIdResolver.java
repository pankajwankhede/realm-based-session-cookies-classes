package com.example.sso.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

public class RealmCookieHttpSessionIdResolver implements HttpSessionIdResolver {

    private final RealmResolver realmResolver;
    private final CookieSerializer cookieSerializer;

    public RealmCookieHttpSessionIdResolver(RealmResolver realmResolver, CookieSerializer cookieSerializer) {
        this.realmResolver = realmResolver;
        this.cookieSerializer = cookieSerializer;
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {
        String realm = realmResolver.resolveRealm(request);
        String cookieName = "SSOSESSION_" + realm;

        if (request.getCookies() == null) return Collections.emptyList();
        for (Cookie c : request.getCookies()) {
            if (cookieName.equals(c.getName()) && StringUtils.hasText(c.getValue())) {
                return List.of(c.getValue());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        if (cookieSerializer instanceof DefaultCookieSerializer dcs) {
            String realm = realmResolver.resolveRealm(request);
            String original = dcs.getCookieName();
            dcs.setCookieName("SSOSESSION_" + realm);
            dcs.writeCookieValue(new CookieSerializer.CookieValue(request, response, sessionId));
            dcs.setCookieName(original);
        }
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        if (cookieSerializer instanceof DefaultCookieSerializer dcs) {
            String realm = realmResolver.resolveRealm(request);
            String original = dcs.getCookieName();
            dcs.setCookieName("SSOSESSION_" + realm);
            dcs.writeCookieValue(new CookieSerializer.CookieValue(request, response, ""));
            dcs.setCookieName(original);
        }
    }
}
