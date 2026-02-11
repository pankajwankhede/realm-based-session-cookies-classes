package com.example.sso.util;

import jakarta.servlet.http.HttpServletRequest;

public final class RedirectUtil {
    private RedirectUtil() {}

    public static String url(HttpServletRequest request, String path) {
        String ctx = request.getContextPath();
        if (ctx == null) ctx = "";
        if (path == null || path.isBlank()) path = "/";
        if (!path.startsWith("/")) path = "/" + path;
        return ctx + path;
    }
}
