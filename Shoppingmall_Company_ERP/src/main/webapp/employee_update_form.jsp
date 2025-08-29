<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.company1.dto.EmployeeDTO" %>

<%
    // 서블릿이 request 바구니에 담아준 'employee' 정보를 꺼냅니다.
    EmployeeDTO employee = (EmployeeDTO) request.getAttribute("employee");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>직원 정보 수정</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/employee.css">
</head>
<body>
    <!-- 헤더 섹션 -->
    <%@ include file="common-jsp/header.jsp" %>

    <div class="container">
        <!-- 직원 수정 폼 -->
        <div class="employee-form">
            <h2>✏️ 직원 정보 수정</h2>
            
            <%-- 정보가 제대로 넘어왔을 때만 폼을 보여줍니다. --%>
            <% if (employee != null) { %>
                <form action="EmployeeServlet" method="post">
                    <%-- ✨ 어떤 action을 실행할지(update)와 누구의 정보인지(empId)를 숨겨서 보냅니다. --%>
                    <input type="hidden" name="action" value="update">
                    <input type="hidden" name="empId" value="<%= employee.getEmpId() %>">
                    
                    <div class="form-group">
                        <label for="empId">직원 ID:</label>
                        <%-- ID(Primary Key)는 수정할 수 없도록 readonly 속성을 추가합니다. --%>
                        <input type="text" id="empId" name="empId_display" value="<%= employee.getEmpId() %>" readonly disabled>
                    </div>
                    <div class="form-group">
                        <label for="empPw">새 비밀번호:</label>
                        <input type="password" id="empPw" name="empPw" placeholder="변경할 경우에만 입력하세요">
                    </div>
                    <div class="form-group">
                        <label for="empName">직원명:</label>
                        <input type="text" id="empName" name="empName" value="<%= employee.getEmpName() %>" required>
                    </div>
                    <div class="form-group">
                        <label for="position">직책:</label>
                        <select id="position" name="position" required>
                            <%-- 기존 직책과 일치하는 옵션을 'selected'로 표시합니다. --%>
                            <option value="사원" <%= "사원".equals(employee.getPosition()) ? "selected" : "" %>>사원</option>
                            <option value="대리" <%= "대리".equals(employee.getPosition()) ? "selected" : "" %>>대리</option>
                            <option value="과장" <%= "과장".equals(employee.getPosition()) ? "selected" : "" %>>과장</option>
                            <option value="차장" <%= "차장".equals(employee.getPosition()) ? "selected" : "" %>>차장</option>
                            <option value="부장" <%= "부장".equals(employee.getPosition()) ? "selected" : "" %>>부장</option>
                            <option value="이사" <%= "이사".equals(employee.getPosition()) ? "selected" : "" %>>이사</option>
                            <option value="시스템 관리자" <%= "시스템 관리자".equals(employee.getPosition()) ? "selected" : "" %>>시스템 관리자</option>
                        </select>
                    </div>
                    <div>
					<label for="email">이메일:</label> <input type="email" id="email"
						name="email" value="<%=employee.getEmail()%>" required>
				</div>
                    <div class="form-group">
                        <label for="auth">권한:</label>
                        <select id="auth" name="auth" required>
                            <option value="user" <%= "user".equals(employee.getAuth()) ? "selected" : "" %>>일반 사용자</option>
                            <option value="admin" <%= "admin".equals(employee.getAuth()) ? "selected" : "" %>>관리자</option>
                        </select>
                    </div>
                    <div class="form-actions">
                        <input type="submit" value="💾 수정 완료">
                        <a href="EmployeeServlet?action=list" class="btn-default" style="background-color: #ccc; color: #333; border: none; padding: 12px 20px; text-decoration: none; border-radius: 5px;">❌ 취소</a>
                    </div>
                </form>
            <% } else { %>
                <p>😢 수정할 직원 정보를 불러오지 못했습니다.</p>
            <% } %>
        </div>
    </div>
</body>
</html>
