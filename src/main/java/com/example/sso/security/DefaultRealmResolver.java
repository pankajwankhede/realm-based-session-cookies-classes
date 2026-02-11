package com.example.sso.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class DefaultRealmResolver implements RealmResolver {

    @Override
    public String resolveRealm(HttpServletRequest request) {
        String realm = request.getParameter("realm");
        if (StringUtils.hasText(realm)) return realm.trim().toUpperCase();

        // Your entry might send clientID instead of realm
        realm = request.getParameter("clientID");
        if (StringUtils.hasText(realm)) return realm.trim().toUpperCase();

        realm = request.getHeader("X-REALM");
        if (StringUtils.hasText(realm)) return realm.trim().toUpperCase();

        return "DEFAULT";
    }
}
