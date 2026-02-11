package com.example.sso.interceptor;

import com.example.sso.dto.AuthzRequest;
import com.example.sso.security.RealmResolver;
import com.example.sso.util.SessionKeys;
import com.example.sso.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.UUID;

public class SsoAuthenticateInterceptor implements HandlerInterceptor {

    private final RealmResolver realmResolver;

    public SsoAuthenticateInterceptor(RealmResolver realmResolver) {
        this.realmResolver = realmResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        HttpSession session = request.getSession(true);

        String realm = realmResolver.resolveRealm(request);
        session.setAttribute(SessionKeys.REALM, realm);

        String clientId = request.getParameter("clientID");
        String responseType = request.getParameter("response_type");
        if (!StringUtils.hasText(responseType)) responseType = request.getParameter("responsetype");

        String redirectUrl = request.getParameter("redirecturl");
        String state = request.getParameter("state");
        String scope = request.getParameter("scope");

        String key = StringUtils.hasText(state) ? state : ("FLOW-" + UUID.randomUUID());

        AuthzRequest authz = new AuthzRequest();
        authz.setRealm(realm);
        authz.setClientId(clientId);
        authz.setResponseType(responseType);
        authz.setRedirectUrl(redirectUrl);
        authz.setState(StringUtils.hasText(state) ? state : key);
        authz.setScope(scope);

        Map<String, AuthzRequest> map = SessionUtil.getOrCreateAuthzMap(session);
        map.put(key, authz);

        session.setAttribute(SessionKeys.LAST_FLOW_KEY, key);

        return true;
    }
}
