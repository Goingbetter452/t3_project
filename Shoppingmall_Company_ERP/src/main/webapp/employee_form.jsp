<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.company1.dao.EmployeeDAO"%>
<%@ page import="com.company1.dto.EmployeeDTO"%>
<%
	// --- 1. 페이지 모드(등록/수정)를 결정하는 Java코드 ---
	String empId = request.getParameter("empId");

    String pageTitle = "직원 신규 등록";
    String buttonText = "등록하기";
    String formAction = "insert";

    EmployeeDTO emp = new EmployeeDTO();
	
    if (empId != null && !empId.isEmpty()) {
        pageTitle = "직원 정보 수정";
        buttonText = "수정하기";
        formAction = "update";

        EmployeeDAO dao = new EmployeeDAO();
        emp = dao.selectEmployeeById(empId);
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= pageTitle %></title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/common.css">
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/form.css"> <%-- 폼 전용 CSS 추가 --%>

<script>
    function validateForm() {
        const form = document.employeeForm;
        
        const empId = form.empId.value.trim();
        const empPw = form.empPw.value.trim();
        const empName = form.empName.value.trim();
        const email = form.email.value.trim(); // ✨ 추가된 부분: 이메일 값 가져오기
        const position = form.position.value.trim();
        
        const action = form.action.value;

        // --- '등록' 모드일 때 유효성 검사 ---
        if (action === 'insert') {
            // ✨ 수정된 부분: email 필드 검사 추가
            if (empId === "" || empPw === "" || empName === "" || position === "") {
                alert("모든 데이터를 입력해주세요!");
                return false;
            }
            
            // 이메일이 비어있으면 기본값 설정
            if (email === "") {
                form.email.value = 'no-email@company.com';
            }
        }
        
        // --- '수정' 모드일 때 유효성 검사 ---
        if (action === 'update') {
            // ✨ 수정된 부분: email 필드 검사 추가
            if (empId === "" || empName === "" || position === "") {
                alert("필수 데이터를 입력해주세요! (비밀번호는 변경 시에만 입력)");
                return false;
            }
            
            // 이메일이 비어있으면 기본값 설정
            if (email === "") {
                form.email.value = 'no-email@company.com';
            }
        }
        
        // ✨ 추가된 부분: 이메일 형식 검사 (이메일이 입력되었을 경우에만)
        if (email !== "") {
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // 간단한 이메일 형식 정규표현식
            if (!emailRegex.test(email)) {
                alert("올바른 이메일 형식을 입력해주세요. (예: user@example.com)");
                form.email.focus();
                return false;
            }
        }

        // --- 아이디에 한글이 있는지 확인 (신규 등록일 때만!) ---
        if (action === 'insert') {
            const koreanRegex = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/;
            if (koreanRegex.test(empId)) {
                alert("아이디는 영문, 숫자로만 입력해주세요.");
                form.empId.focus();
                return false;
            }
        }

        // --- 비밀번호가 8자리 이상인지 확인 ---
        if (empPw !== "" && empPw.length < 8) {
            alert("비밀번호는 8자리 이상으로 입력해주세요.");
            form.empPw.focus();
            return false;
        }

        return true;
    }
</script>
</head>
	
<body>
<%@ include file="/common-jsp/header.jsp" %>
<div class="form-container">
    <h1><%= pageTitle %></h1>
    
    <form name="employeeForm" action="<%= request.getContextPath() %>/EmployeeServlet" method="post" onsubmit="return validateForm();">
        
        <input type="hidden" name="action" value="<%= formAction %>">
        
        <label for="empId">직원 아이디</label>
        <input type="text" id="empId" name="empId" value="<%= (emp.getEmpId() != null) ? emp.getEmpId() : "" %>" 
               <%= (formAction.equals("update")) ? "readonly class='readonly-input'" : "" %>>

        <label for="empPw">비밀번호</label>
        <input type="password" id="empPw" name="empPw" placeholder="새 비밀번호 또는 변경할 비밀번호 입력">

        <label for="empName">이름</label>
        <input type="text" id="empName" name="empName" value="<%= (emp.getEmpName() != null) ? emp.getEmpName() : "" %>">

        <!-- ✨ 추가된 부분: 이메일 입력 폼 -->
        <label for="email">이메일</label>
        <input type="email" id="email" name="email" value="<%= (emp.getEmail() != null) ? emp.getEmail() : "" %>">

        <label for="position">직급</label>
        <input type="text" id="position" name="position" value="<%= (emp.getPosition() != null) ? emp.getPosition() : "" %>">

        <label for="auth">권한</label>
        <select id="auth" name="auth">
            <option value="user" <%= ("user".equals(emp.getAuth())) ? "selected" : "" %>>일반사용자</option>
            <option value="admin" <%= ("admin".equals(emp.getAuth())) ? "selected" : "" %>>관리자</option>
        </select>
        
        <button type="submit"><%= buttonText %></button>
    </form>
</div>
</body>
</html>