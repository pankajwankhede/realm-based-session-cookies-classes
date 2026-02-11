package com.example.sso.controller;

import com.example.sso.dto.AuthzRequest;
import com.example.sso.dto.HomeDTO;
import com.example.sso.util.RedirectUtil;
import com.example.sso.util.SessionKeys;
import com.example.sso.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Controller
public class OauthController {

    @GetMapping("/oauth/continue")
    public void continueOauth(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam(required = false) String state) throws Exception {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendRedirect(RedirectUtil.url(request, "/login"));
            return;
        }

        HomeDTO home = (HomeDTO) session.getAttribute(SessionKeys.HOME_DTO);
        if (home == null || !home.isUserAuthenticated()) {
            response.sendRedirect(RedirectUtil.url(request, "/login"));
            return;
        }

        Map<String, AuthzRequest> map = SessionUtil.getOrCreateAuthzMap(session);

        AuthzRequest authz = null;
        if (StringUtils.hasText(state) && map.containsKey(state)) authz = map.get(state);
        if (authz == null) {
            Object last = session.getAttribute(SessionKeys.LAST_FLOW_KEY);
            if (last instanceof String key && map.containsKey(key)) authz = map.get(key);
        }

        if (authz == null || !StringUtils.hasText(authz.getRedirectUrl())) {
            response.sendRedirect(RedirectUtil.url(request, "/login"));
            return;
        }

        // Simulated third-party OAuth provider redirect with code
        String redirectUrl = authz.getRedirectUrl();
        String sep = redirectUrl.contains("?") ? "&" : "?";
        String code = "demo-oauth-code";
        String encodedState = URLEncoder.encode(authz.getState(), StandardCharsets.UTF_8);

        // cleanup optional: map.remove(authz.getState());

        response.sendRedirect(redirectUrl + sep + "code=" + code + "&state=" + encodedState);
    }
}
