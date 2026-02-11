package com.example.sso.interceptor;

import com.example.sso.dto.HomeDTO;
import com.example.sso.util.RedirectUtil;
import com.example.sso.util.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public class SessionValidationInterceptor implements HandlerInterceptor {

    private final List<String> skipPaths;
    private final AntPathMatcher matcher = new AntPathMatcher();

    public SessionValidationInterceptor(List<String> skipPaths) {
        this.skipPaths = skipPaths;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        if (ctx != null && !ctx.isBlank() && uri.startsWith(ctx)) {
            uri = uri.substring(ctx.length());
        }

        for (String p : skipPaths) {
            if (matcher.match(p, uri)) return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(RedirectUtil.url(request, "/login"));
            return false;
        }

        HomeDTO home = (HomeDTO) session.getAttribute(SessionKeys.HOME_DTO);
        if (home == null || !home.isUserAuthenticated()) {
            response.sendRedirect(RedirectUtil.url(request, "/login"));
            return false;
        }

        return true;
    }
}
