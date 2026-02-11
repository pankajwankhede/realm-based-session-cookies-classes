<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<jsp:include page="/WEB-INF/viewjsp/fragments/header.jsp" />

<div style="max-width:420px;margin:24px auto;padding:16px;border:1px solid #eee;border-radius:10px;">
  <h3 style="margin-top:0;">Login</h3>

  <c:if test="${not empty error}">
    <div style="padding:10px;background:#fff3f3;border:1px solid #ffd2d2;color:#a00;border-radius:8px;margin-bottom:12px;">
      ${error}
    </div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/auth">
    <jsp:include page="/WEB-INF/viewjsp/fragments/common-hidden.jsp" />

    <div style="margin-bottom:10px;">
      <label>Username</label><br/>
      <input type="text" name="loginVo.userName" style="width:100%;padding:8px;" />
    </div>

    <div style="margin-bottom:10px;">
      <label>Password</label><br/>
      <input type="password" name="loginVo.password" style="width:100%;padding:8px;" />
    </div>

    <button type="submit" style="padding:8px 14px;">Login</button>
  </form>

  <div style="margin-top:12px;display:flex;gap:12px;">
    <c:if test="${showForgetUsername}">
      <a href="${pageContext.request.contextPath}/forgetUser">Forgot Username</a>
    </c:if>
    <c:if test="${showForgetPassword}">
      <a href="${pageContext.request.contextPath}/forgetPass">Forgot Password</a>
    </c:if>
  </div>

  <div style="margin-top:14px;color:#666;">
    <small>Demo password is <b>pass</b>. Try username <b>locked</b> to simulate lock.</small>
  </div>
</div>

<jsp:include page="/WEB-INF/viewjsp/fragments/footer.jsp" />
