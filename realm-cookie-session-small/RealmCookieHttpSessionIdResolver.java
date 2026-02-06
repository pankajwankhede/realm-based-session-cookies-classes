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
    private final String cookiePrefix;

    public RealmCookieHttpSessionIdResolver(
            RealmResolver realmResolver,
            CookieSerializer cookieSerializer,
            String cookiePrefix
    ) {
        this.realmResolver = realmResolver;
        this.cookieSerializer = cookieSerializer;
        this.cookiePrefix = cookiePrefix;
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {

        String realm = normalize(realmResolver.resolveRealm(request));
        String cookieName = cookiePrefix + "_" + realm;

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Collections.emptyList();
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())
                    && StringUtils.hasText(cookie.getValue())) {
                return List.of(cookie.getValue());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void setSessionId(HttpServletRequest request,
                             HttpServletResponse response,
                             String sessionId) {

        String realm = normalize(realmResolver.resolveRealm(request));
        String cookieName = cookiePrefix + "_" + realm;

        if (cookieSerializer instanceof DefaultCookieSerializer dcs) {
            String original = dcs.getCookieName();
            dcs.setCookieName(cookieName);
            dcs.writeCookieValue(
                    new CookieSerializer.CookieValue(request, response, sessionId)
            );
            dcs.setCookieName(original);
        }
    }

    @Override
    public void expireSession(HttpServletRequest request,
                              HttpServletResponse response) {

        String realm = normalize(realmResolver.resolveRealm(request));
        String cookieName = cookiePrefix + "_" + realm;

        if (cookieSerializer instanceof DefaultCookieSerializer dcs) {
            String original = dcs.getCookieName();
            dcs.setCookieName(cookieName);
            dcs.writeCookieValue(
                    new CookieSerializer.CookieValue(request, response, "")
            );
            dcs.setCookieName(original);
        }
    }

    private String normalize(String realm) {
        if (!StringUtils.hasText(realm)) {
            return "DEFAULT";
        }
        return realm.trim().toUpperCase();
    }
}
