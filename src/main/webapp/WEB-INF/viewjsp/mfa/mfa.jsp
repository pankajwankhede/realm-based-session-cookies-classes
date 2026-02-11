<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="/WEB-INF/viewjsp/fragments/header.jsp" />

<div style="max-width:520px;margin:24px auto;padding:16px;border:1px solid #eee;border-radius:10px;">
  <h3 style="margin-top:0;">MFA Step</h3>
  <p>Demo MFA page. In real flow, call MFA SDK/provider based on realm policy.</p>

  <form method="post" action="${pageContext.request.contextPath}/mfa/verify">
    <jsp:include page="/WEB-INF/viewjsp/fragments/common-hidden.jsp" />
    <div style="margin-bottom:10px;">
      <label>OTP</label><br/>
      <input type="text" name="otp" style="width:100%;padding:8px;" />
    </div>
    <button type="submit" style="padding:8px 14px;">Verify</button>
  </form>

  <p style="margin-top:14px;"><a href="${pageContext.request.contextPath}/oauth/continue">Skip demo and continue</a></p>
</div>

<jsp:include page="/WEB-INF/viewjsp/fragments/footer.jsp" />
