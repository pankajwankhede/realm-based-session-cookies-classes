package com.example.sso.util;

import com.example.sso.dto.AuthzRequest;
import jakarta.servlet.http.HttpSession;

import java.util.LinkedHashMap;
import java.util.Map;

public final class SessionUtil {
    private SessionUtil() {}

    @SuppressWarnings("unchecked")
    public static Map<String, AuthzRequest> getOrCreateAuthzMap(HttpSession session) {
        Object obj = session.getAttribute(SessionKeys.AUTHZ_REQ_MAP);
        if (obj instanceof Map<?, ?>) {
            return (Map<String, AuthzRequest>) obj;
        }
        Map<String, AuthzRequest> map = new LinkedHashMap<>();
        session.setAttribute(SessionKeys.AUTHZ_REQ_MAP, map);
        return map;
    }
}
