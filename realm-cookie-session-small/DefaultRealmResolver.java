package com.example.sso.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DefaultRealmResolver implements RealmResolver {

    @Override
    public String resolveRealm(HttpServletRequest request) {

        // 1) request parameter
        String realm = request.getParameter("realm");
        if (StringUtils.hasText(realm)) {
            return realm.trim().toUpperCase();
        }

        // 2) header (optional)
        realm = request.getHeader("X-REALM");
        if (StringUtils.hasText(realm)) {
            return realm.trim().toUpperCase();
        }

        // 3) fallback
        return "DEFAULT";
    }
}
