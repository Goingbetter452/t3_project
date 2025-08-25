<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.company1.dao.EmployeeDAO"%>
<%@ page import="com.company1.dto.EmployeeDTO"%>
<%
	// --- 1. 페이지 모드(등록/수정)를 결정하는 Java코드 ---
	String empId = request.getParameter("empId"); // URL에 empId가 있는지 확인

    String pageTitle = "직원 신규 등록";
    String buttonText = "등록하기";
    String formAction = "insert"; // 서블릿에 보낼 action 값 기본은 'insert'

    EmployeeDTO emp = new EmployeeDTO(); // 빈 DTO 객체 준비
	
    // URL에 empId가 있다면, '수정 모드'로 변경
    if (empId != null && !empId.isEmpty()) {
        pageTitle = "직원 정보 수정";
        buttonText = "수정하기";
        formAction = "update"; // 서블릿에 보낼 action 값을 'update'로 변경

        EmployeeDAO dao = new EmployeeDAO();
        emp = dao.selectEmployeeById(empId); // DB에서 해당 직원 정보를 가져옴
    }
%>
	<!DOCTYPE html>
	<html>
	<head>
	<meta charset="UTF-8">
	<title><%= pageTitle %></title> <%-- 2. 페이지 제목 동적 변경 --%>
	<style>
    /* 간단한 스타일 예시 */
    .form-container { width: 400px; margin: 20px auto; padding: 20px; border: 1px solid #ccc; }
    .form-container h1 { text-align: center; }
    .form-container input { width: 100%; padding: 8px; margin-bottom: 10px; box-sizing: border-box; }
    .form-container button { width: 100%; padding: 10px; background-color: #007bff; color: white; border: none; }
	</style>
	<script>
    // 폼 데이터를 제출하기 전에 유효성을 검사하는 함수
    function validateForm() {
        // form 요소에 접근하기 쉽게 form 자체를 변수에 저장해요.
        const form = document.employeeForm;
        
        // 사용자가 입력한 값들을 가져와서 앞뒤 공백을 제거해요.
        const empId = form.empId.value.trim();
        const empPw = form.empPw.value.trim();
        const empName = form.empName.value.trim();
        const position = form.position.value.trim();
        
        // 지금이 '등록' 모드인지 '수정' 모드인지 확인해요.
        const action = form.action.value;

        // --- 요구사항 1: 데이터가 비어있는지 확인 ---
        // '등록' 모드일 때는 모든 칸이 채워져 있어야 해요.
        if (action === 'insert' && (empId === "" || empPw === "" || empName === "" || position === "")) {
            alert("모든 데이터를 입력해주세요!");
            return false; // false를 반환하면 데이터 전송(제출)을 막아요!
        }
        
        // '수정' 모드일 때는 비밀번호를 제외한 정보들이 비어있는지 확인해요.
        // (비밀번호는 변경하고 싶을 때만 입력하니까요!)
        if (action === 'update' && (empId === "" || empName === "" || position === "")) {
            alert("필수 데이터를 입력해주세요!");
            return false;
        }

        // --- 요구사항 3: 아이디에 한글이 있는지 확인 (신규 등록일 때만!) ---
        if (action === 'insert') {
            const koreanRegex = /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/; // '한글이 있나?'를 찾아내는 정규표현식
            if (koreanRegex.test(empId)) {
                alert("아이디는 영문, 숫자로만 입력해주세요.");
                form.empId.focus(); // 사용자가 편하게 수정하도록 아이디 칸에 커서를 옮겨줘요.
                return false;
            }
        }

        // --- 요구사항 2: 비밀번호가 8자리 이상인지 확인 ---
        // 비밀번호가 입력되었을 경우에만 길이를 검사해요.
        if (empPw !== "" && empPw.length < 8) {
            alert("비밀번호는 8자리 이상으로 입력해주세요.");
            form.empPw.focus(); // 비밀번호 칸에 커서를 옮겨줘요.
            return false;
        }

        // 모든 검사를 통과했다면 true를 반환해서 데이터를 서버로 전송해요!
        return true;
    }
	</script>
	</head>
	<body>

    <div class="form-container">
        <h1><%= pageTitle %></h1> <%-- 2. 페이지 제목 동적 변경 --%>
        
        <%-- 3. EmployeeServlet으로 데이터를 보내는 form --%>
        <form name="employeeForm" action="<%= request.getContextPath() %>/employee" method="post" onsubmit="return validateForm();">
            
            <%-- 4. 서블릿에게 '등록'인지 '수정'인지 알려주는 비밀 정보 --%>
            <input type="hidden" name="action" value="<%= formAction %>">
            
            <label>직원 아이디</label>
            <%-- 5. 수정 모드일 때 아이디는 변경 못하게 readonly 처리 --%>
            <input type="text" name="empId" value="<%= (emp.getEmpId() != null) ? emp.getEmpId() : "" %>" 
                   <%= (formAction.equals("update")) ? "readonly" : "" %>>

            <label>비밀번호</label>
            <input type="password" name="empPw" placeholder="새 비밀번호 또는 변경할 비밀번호 입력">

            <label>이름</label>
            <input type="text" name="empName" value="<%= (emp.getEmpName() != null) ? emp.getEmpName() : "" %>">

            <label>직급</label>
            <input type="text" name="position" value="<%= (emp.getPosition() != null) ? emp.getPosition() : "" %>">

            <label>권한</label>
            <select name="auth">
                <%-- 6. 수정 모드일 때 기존 권한을 선택된 상태(selected)로 표시 --%>
                <option value="user" <%= ("user".equals(emp.getAuth())) ? "selected" : "" %>>일반사용자</option>
                <option value="admin" <%= ("admin".equals(emp.getAuth())) ? "selected" : "" %>>관리자</option>
            </select>
            <br><br>
            
            <%-- 7. 버튼 텍스트 동적 변경 --%>
            <button type="submit"><%= buttonText %></button>
        </form>
    </div>

	</body>
	</html>
