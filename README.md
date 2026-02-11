# Realm SSO + JSP Demo (Backend + JSP UI)

This is a small demo that shows:

- `/ssoauthenticate` entry endpoint captures OAuth-ish params and stores them in session (multi-tab safe via `state`)
- Realm-wise sessions using **one cookie per realm**: `SSOSESSION_<REALM>`
- Cookie settings for PCF/HTTPS: `SameSite=None; Secure; HttpOnly`
- `@ControllerAdvice` auto-injects `${realm}` and `${state}` into ALL JSPs
- Common hidden fields via `fragments/common-hidden.jsp`

## Run
```bash
./gradlew bootRun
```

Default context path is `/ssopartner` (see `application.yml`).

## Try in browser
Open:
```
http://localhost:8080/ssopartner/ssoauthenticate?clientID=BC&response_type=code&redirecturl=https://example.com/callback&state=abc123&scope=openid
```

Login with:
- username: anything (except `locked`)
- password: `pass`

Then it redirects to the provided `redirecturl` with `code=demo-oauth-code`.

## Notes
- This demo uses in-memory session repository (Spring Session core). You can swap to Geode/Redis in your real app.
- Static resources are mapped from WEB-INF via `WebMvcConfig.addResourceHandlers`.
