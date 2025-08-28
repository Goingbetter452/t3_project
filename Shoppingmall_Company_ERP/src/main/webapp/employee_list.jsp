<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="com.company1.dto.EmployeeDTO" %>

<%
    // 서블릿에서 전달한 employeeList 속성을 List<EmployeeDTO> 타입으로 받습니다.
    List<EmployeeDTO> employeeList = (List<EmployeeDTO>) request.getAttribute("employeeList");
%>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>직원 관리 시스템</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/employee.css">
</head>
<body>
    <!-- 헤더 섹션 -->
        <%@ include file="common-jsp/header.jsp" %>

    <div class="container">
        <!-- 통계 섹션 -->
        <div class="stats employee-stats">
            <div class="stat-item">
                <div class="stat-number"><%= employeeList != null ? employeeList.size() : 0 %></div>
                <div class="stat-label">전체 직원 수</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">0</div>
                <div class="stat-label">관리자 수</div>
            </div>
            <div class="stat-item">
                <div class="stat-number">0</div>
                <div class="stat-label">일반 직원 수</div>
            </div>
        </div>

        <!-- 직원 등록 폼 -->
        <div class="employee-form">
            <h2>👨‍💼 신규 직원 등록</h2>
            <form action="EmployeeServlet" method="post">
                <input type="hidden" name="action" value="insert">
                <div class="form-group">
                    <label for="empId">직원 ID:</label>
                    <input type="text" id="empId" name="empId" required placeholder="로그인 ID를 입력하세요">
                </div>
                <div class="form-group">
                    <label for="empPw">비밀번호:</label>
                    <input type="password" id="empPw" name="empPw" required placeholder="비밀번호를 입력하세요">
                </div>
                <div class="form-group">
                    <label for="empName">직원명:</label>
                    <input type="text" id="empName" name="empName" required placeholder="직원명을 입력하세요">
                </div>
                <div class="form-group">
                    <label for="position">직책:</label>
                    <select id="position" name="position" required>
                        <option value="">직책을 선택하세요</option>
                        <option value="사원">사원</option>
                        <option value="대리">대리</option>
                        <option value="과장">과장</option>
                        <option value="차장">차장</option>
                        <option value="부장">부장</option>
                        <option value="이사">이사</option>
                        <option value="시스템 관리자">시스템 관리자</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="auth">권한:</label>
                    <select id="auth" name="auth" required>
                        <option value="">권한을 선택하세요</option>
                        <option value="admin">관리자</option>
                        <option value="user">일반 사용자</option>
                    </select>
                </div>
                <div class="form-actions">
                    <input type="submit" value="✅ 직원 등록">
                </div>
            </form>
        </div>

        <!-- 직원 목록 -->
        <div class="list-section">
            <h2>👨‍💼 직원 목록</h2>
            <table class="employee-table">
                <thead>
                    <tr>
                        <th>직원 ID</th>
                        <th>직원명</th>
                        <th>직책</th>
                        <th>권한</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        // 리스트가 null이 아니고 비어있지 않은 경우에만 내용을 표시합니다.
                        if (employeeList != null && !employeeList.isEmpty()) {
                            // for-each 반복문으로 리스트를 순회합니다.
                            for (EmployeeDTO employee : employeeList) {
                    %>
                                <tr>
                                    <%-- DTO의 getter 메소드를 사용하여 값을 가져옵니다. --%>
                                    <td class="employee-id"><strong><%= employee.getEmpId() %></strong></td>
                                    <td class="employee-name"><strong><%= employee.getEmpName() %></strong></td>
                                    <td class="employee-position"><%= employee.getPosition() != null ? employee.getPosition() : "-" %></td>
                                    <td class="employee-auth"><%= "admin".equals(employee.getAuth()) ? "관리자" : "일반 사용자" %></td>
                                    <td class="actions">
                                        <a href="EmployeeServlet?action=edit&empId=<%= employee.getEmpId() %>" class="btn-edit">✏️ 수정</a>
                                        <a href="EmployeeServlet?action=delete&empId=<%= employee.getEmpId() %>" class="btn-delete" onclick="return confirm('정말로 삭제하시겠습니까?');">🗑️ 삭제</a>
                                    </td>
                                </tr>
                    <%
                            }
                        } else {
                            // 리스트가 비어있는 경우 메시지를 표시합니다.
                    %>
                        <tr>
                            <td colspan="5" class="no-data">📭 등록된 직원이 없습니다. 첫 번째 직원을 등록해보세요!</td>
                        </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
