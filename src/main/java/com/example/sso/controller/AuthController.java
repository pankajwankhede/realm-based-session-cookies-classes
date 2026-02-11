package com.example.sso.controller;

import com.example.sso.dto.AuthzRequest;
import com.example.sso.dto.HomeDTO;
import com.example.sso.dto.LoginVO;
import com.example.sso.service.AuthService;
import com.example.sso.util.SessionKeys;
import com.example.sso.util.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/ssoauthenticate")
    public String ssoauthenticate(HttpServletRequest request) {
        // Interceptor already stored AuthzRequest and created session
        // If already authenticated, continue directly to oauth
        HttpSession session = request.getSession(false);
        if (session != null) {
            HomeDTO home = (HomeDTO) session.getAttribute(SessionKeys.HOME_DTO);
            if (home != null && home.isUserAuthenticated()) {
                return "redirect:/oauth/continue";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(true);

        HomeDTO home = (HomeDTO) session.getAttribute(SessionKeys.HOME_DTO);
        if (home == null) {
            home = new HomeDTO();
            home.setLoginVo(new LoginVO());
            session.setAttribute(SessionKeys.HOME_DTO, home);
        } else if (home.getLoginVo() == null) {
            home.setLoginVo(new LoginVO());
        }

        model.addAttribute("homeDTO", home);
        model.addAttribute("showForgetUsername", true);
        model.addAttribute("showForgetPassword", true);

        return "login/login";
    }

    @PostMapping("/auth")
    public String authenticate(HttpServletRequest request,
                               @RequestParam(required = false) String state,
                               @RequestParam(required = false) String realm,
                               @ModelAttribute("homeDTO") HomeDTO homeDTO,
                               Model model) {

        HttpSession session = request.getSession(true);

        // Ensure home dto exists in session
        HomeDTO sessionHome = (HomeDTO) session.getAttribute(SessionKeys.HOME_DTO);
        if (sessionHome == null) sessionHome = new HomeDTO();

        // Get authz from session (multi-tab safe)
        Map<String, AuthzRequest> map = SessionUtil.getOrCreateAuthzMap(session);
        AuthzRequest authz = null;
        if (StringUtils.hasText(state) && map.containsKey(state)) authz = map.get(state);
        if (authz == null) {
            Object last = session.getAttribute(SessionKeys.LAST_FLOW_KEY);
            if (last instanceof String key && map.containsKey(key)) authz = map.get(key);
        }

        // Read user/pass from submitted object
        LoginVO login = homeDTO != null ? homeDTO.getLoginVo() : null;
        String username = login != null ? login.getUserName() : null;
        String password = login != null ? login.getPassword() : null;

        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            model.addAttribute("error", "Username/Password required");
            return "login/login";
        }

        // Determine realm: prefer submitted realm, otherwise from authz, otherwise DEFAULT
        String r = StringUtils.hasText(realm) ? realm.trim().toUpperCase() : (authz != null ? authz.getRealm() : "DEFAULT");
        session.setAttribute(SessionKeys.REALM, r);

        AuthService.AuthResult result = authService.authenticate(r, username, password);

        if (!result.success()) {
            model.addAttribute("error", result.message());
            return "login/login";
        }

        sessionHome.setUserAuthenticated(true);
        sessionHome.setUsername(username);
        sessionHome.setUserDetails(result.userDetails());

        // clear password from memory
        if (sessionHome.getLoginVo() != null) sessionHome.getLoginVo().setPassword(null);

        session.setAttribute(SessionKeys.HOME_DTO, sessionHome);

        if (result.passwordChangeRequired()) {
            return "redirect:/password-change";
        }

        if (result.mfaRequired()) {
            return "redirect:/mfa";
        }

        return StringUtils.hasText(state) ? "redirect:/oauth/continue?state=" + state : "redirect:/oauth/continue";
    }

    @GetMapping("/password-change")
    public String passwordChangePage() {
        return "password/password-change";
    }

    @GetMapping("/mfa")
    public String mfaPage() {
        return "mfa/mfa";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/login";
    }
}
