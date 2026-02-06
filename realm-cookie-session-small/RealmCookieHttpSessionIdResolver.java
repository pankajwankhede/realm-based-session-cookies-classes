package com.example.sso.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

public class RealmCookieHttpSessionIdResolver implements HttpSessionIdResolver {

    private final RealmResolver realmResolver;

    public RealmCookieHttpSessionIdResolver(RealmResolver realmResolver) {
        this.realmResolver = realmResolver;
    }

    @Override
    public List<String> resolveSessionIds(HttpServletRequest request) {

        String realm = normalize(realmResolver.resolveRealm(request));
        String cookieName = "SSOSESSION_" + realm;

        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Collections.emptyList();

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())
                    && StringUtils.hasText(cookie.getValue())) {
                return List.of(cookie.getValue());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void setSessionId(
            HttpServletRequest request,
            HttpServletResponse response,
            String sessionId) {

        String realm = normalize(realmResolver.resolveRealm(request));
        String cookieName = "SSOSESSION_" + realm;

        Cookie cookie = new Cookie(cookieName, sessionId);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // true in prod HTTPS
        cookie.setPath(request.getContextPath().isEmpty()
                ? "/"
                : request.getContextPath());

        response.addCookie(cookie);
    }

    @Override
    public void expireSession(
            HttpServletRequest request,
            HttpServletResponse response) {

        String realm = normalize(realmResolver.resolveRealm(request));
        String cookieName = "SSOSESSION_" + realm;

        Cookie cookie = new Cookie(cookieName, "");
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath(request.getContextPath().isEmpty()
                ? "/"
                : request.getContextPath());

        response.addCookie(cookie);
    }

    private String normalize(String realm) {
        if (!StringUtils.hasText(realm)) {
            return "DEFAULT";
        }
        return realm.trim().toUpperCase();
    }
}
