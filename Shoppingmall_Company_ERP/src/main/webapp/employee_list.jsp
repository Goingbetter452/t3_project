<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.company1.dto.EmployeeDTO"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>직원 목록</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/employee/employee_list.css">
</head>
<body>
	<div class="table-container">
		<header>
			<h1>직원 목록</h1>
		</header>

		<main>
			<div class="table-container">
				<table>
					<thead>
						<tr>
							<th>사원번호</th>
							<th>이름</th>
							<th>부서</th>
							<th>직급</th>
							<th>입사일</th>
							<th>관리</th>
						</tr>
					</thead>
					<tbody>
						<%
						// 서블릿에서 넘겨준 employeeList 꺼내오기
						List<EmployeeDTO> employeeList = (List<EmployeeDTO>) request.getAttribute("employeeList");

						if (employeeList == null || employeeList.isEmpty()) {
						%>
						<tr>
							<td colspan="6" class="no-data">등록된 직원이 없습니다.</td>
						</tr>
						<%
						} else {
						for (EmployeeDTO emp : employeeList) {
						%>

						<tr>
							<td><%=emp.getEmpId()%></td>
							<td><%=emp.getEmpName()%></td>
							<td><%=emp.getPosition()%></td>
							<td><%=emp.getAuth()%></td>
							<td><a
								href="employee?command=editForm&employeeId=<%=emp.getEmpId()%>"
								class="btn btn-edit">수정</a> <a
								href="employee?command=delete&employeeId=<%=emp.getEmpId()%>"
								class="btn btn-delete" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</a>
							</td>
						</tr>
						<%
						} // end for
						} // end if
						%>
					</tbody>
				</table>
			</div>

			<div class="action-buttons">
				<a href="employee_form.jsp" class="btn btn-primary">신규 직원 등록</a>
			</div>
		</main>

		<footer>
			<p>&copy; 2025 Company1 ERP Project. All Rights Reserved.</p>
		</footer>
	</div>
</body>
</html>
