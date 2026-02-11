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

        for (Cookie c : cookies) {
            if (cookieName.equals(c.getName()) && StringUtils.hasText(c.getValue())) {
                return List.of(c.getValue());
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
        String realm = normalize(realmResolver.resolveRealm(request));
        String cookieName = "SSOSESSION_" + realm;

        String path = request.getContextPath();
        if (!StringUtils.hasText(path)) path = "/";

        // SameSite=None requires Secure
        response.addHeader("Set-Cookie", buildSetCookie(cookieName, sessionId, path, true, "None", -1));
    }

    @Override
    public void expireSession(HttpServletRequest request, HttpServletResponse response) {
        String realm = normalize(realmResolver.resolveRealm(request));
        String cookieName = "SSOSESSION_" + realm;

        String path = request.getContextPath();
        if (!StringUtils.hasText(path)) path = "/";

        response.addHeader("Set-Cookie", buildSetCookie(cookieName, "", path, true, "None", 0));
    }

    private String buildSetCookie(String name, String value, String path, boolean secure, String sameSite, int maxAgeSeconds) {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(value == null ? "" : value);
        sb.append("; Path=").append(path);
        sb.append("; HttpOnly");
        if (secure) sb.append("; Secure");
        if (maxAgeSeconds >= 0) sb.append("; Max-Age=").append(maxAgeSeconds);
        if (StringUtils.hasText(sameSite)) sb.append("; SameSite=").append(sameSite);
        return sb.toString();
    }

    private String normalize(String realm) {
        if (!StringUtils.hasText(realm)) return "DEFAULT";
        return realm.trim().toUpperCase();
    }
}
