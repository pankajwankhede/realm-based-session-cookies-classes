<%@ page contentType="text/html;charset=UTF-8" %>
<jsp:include page="/WEB-INF/viewjsp/fragments/header.jsp" />

<div style="max-width:520px;margin:24px auto;padding:16px;border:1px solid #eee;border-radius:10px;">
  <h3 style="margin-top:0;">Password Change Required</h3>
  <p>This is a demo page. In real flow, you would validate policy per realm and update password.</p>

  <form method="post" action="${pageContext.request.contextPath}/password-change">
    <jsp:include page="/WEB-INF/viewjsp/fragments/common-hidden.jsp" />
    <div style="margin-bottom:10px;">
      <label>New Password</label><br/>
      <input type="password" name="newPassword" style="width:100%;padding:8px;" />
    </div>
    <button type="submit" style="padding:8px 14px;">Update</button>
  </form>

  <p style="margin-top:14px;"><a href="${pageContext.request.contextPath}/oauth/continue">Continue</a></p>
</div>

<jsp:include page="/WEB-INF/viewjsp/fragments/footer.jsp" />
