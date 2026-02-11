package com.example.sso.web;

import com.example.sso.security.RealmResolver;
import com.example.sso.util.SessionKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.util.StringUtils;

@ControllerAdvice(annotations = Controller.class)
public class GlobalViewModelAdvice {

    private final RealmResolver realmResolver;

    public GlobalViewModelAdvice(RealmResolver realmResolver) {
        this.realmResolver = realmResolver;
    }

    @ModelAttribute("realm")
    public String realm(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object r = session.getAttribute(SessionKeys.REALM);
            if (r instanceof String s && StringUtils.hasText(s)) return s;
        }
        return realmResolver.resolveRealm(request);
    }

    @ModelAttribute("state")
    public String state(HttpServletRequest request) {
        String s = request.getParameter("state");
        if (StringUtils.hasText(s)) return s;

        HttpSession session = request.getSession(false);
        if (session == null) return null;

        Object last = session.getAttribute(SessionKeys.LAST_FLOW_KEY);
        return (last instanceof String ls && StringUtils.hasText(ls)) ? ls : null;
    }
}
