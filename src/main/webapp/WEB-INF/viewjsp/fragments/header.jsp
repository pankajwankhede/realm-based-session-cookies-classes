<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<div style="padding:12px;border-bottom:1px solid #ddd;display:flex;gap:12px;align-items:center;justify-content:space-between;">
  <div><strong>SSO Central IdP</strong> <span style="color:#666;">(realm: ${realm})</span></div>

  <div style="display:flex;gap:10px;align-items:center;">
    <c:if test="${not empty homeDTO && homeDTO.userAuthenticated}">
      <span>Welcome, ${homeDTO.username}</span>
      <form method="post" action="${pageContext.request.contextPath}/logout" style="margin:0;">
        <jsp:include page="/WEB-INF/viewjsp/fragments/common-hidden.jsp" />
        <button type="submit">Logout</button>
      </form>
    </c:if>
  </div>
</div>
