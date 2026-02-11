<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<input type="hidden" name="realm" value="${realm}" />
<input type="hidden" name="state" value="${state}" />
<c:if test="${not empty _csrf}">
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</c:if>
