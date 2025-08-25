<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*, java.time.*"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>그룹웨어 - Shoppingmall Company ERP</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/groupware.css">
<style>
.page-header {
	background: linear-gradient(135deg, #6c5ce7, #4a29a0);
	color: white;
	padding: 30px 0;
	text-align: center;
	margin-bottom: 30px;
}

.page-header h1 {
	margin: 0;
	font-size: 2.5em;
	font-weight: 300;
}

.page-header p {
	margin: 10px 0 0 0;
	opacity: 0.9;
	font-size: 1.1em;
}
</style>

</head>
<body>
	<!-- 헤더 포함 -->
	<%@ include file="common-jsp/header.jsp"%>

	<!-- 페이지 헤더 -->
	<div class="page-header">
		<h1>🏢 그룹웨어</h1>
		<p>효율적인 업무 협업을 위한 통합 워크스페이스</p>
	</div>

	<div class="groupware-container">
		<!-- 공지사항 섹션 -->
		<div class="groupware-section notice-section">
			<h2>📢 공지사항</h2>
			<div class="notice-form">
				<input type="text" id="noticeTitle" placeholder="제목을 입력하세요"
					maxlength="100">
				<textarea id="noticeContent" placeholder="공지사항 내용을 입력하세요"
					maxlength="500"></textarea>
				<button onclick="addNotice()">등록</button>
			</div>
			<div class="notice-list" id="noticeList">
				<!-- 공지사항 목록이 여기에 동적으로 생성됩니다 -->
			</div>
		</div>

		<!-- 캘린더 섹션 -->
		<div class="groupware-section calendar-section">
			<h2>📅 캘린더</h2>
			<div style="text-align: center; margin-bottom: 15px;">
				<button onclick="changeMonth(-1)">◀</button>
				<span id="currentMonth" style="margin: 0 20px; font-weight: bold;"></span>
				<button onclick="changeMonth(1)">▶</button>
			</div>
			<table id="calendar">
				<thead>
					<tr>
						<th>일</th>
						<th>월</th>
						<th>화</th>
						<th>수</th>
						<th>목</th>
						<th>금</th>
						<th>토</th>
					</tr>
				</thead>
				<tbody id="calendarBody">
				</tbody>
			</table>
		</div>

		<!-- 할일 목록 섹션 -->
		<div class="groupware-section todo-section">
			<h2>✅ 할일 목록</h2>
			<div class="todo-form">
				<input type="text" id="todoInput" placeholder="새로운 할일을 입력하세요"
					maxlength="100">
				<button onclick="addTodo()">추가</button>
			</div>
			<div class="todo-list" id="todoList">
				<!-- 할일 목록이 여기에 동적으로 생성됩니다 -->
			</div>
		</div>

		<!-- 출퇴근 관리 섹션 -->
		<div class="groupware-section attendance-section">
			<h2>⏰ 출퇴근 관리</h2>
			<div class="current-time" id="currentTime"></div>
			<div class="attendance-buttons">
				<button class="attendance-btn checkin-btn" onclick="checkIn()">출근</button>
				<button class="attendance-btn checkout-btn" onclick="checkOut()">퇴근</button>
			</div>
			<div class="attendance-status">
				<div class="attendance-info">
					<div>
						<strong>출근 시간</strong> <span id="checkinTime">미등록</span>
					</div>
					<div>
						<strong>퇴근 시간</strong> <span id="checkoutTime">미등록</span>
					</div>
				</div>
				<div class="attendance-info" style="margin-top: 10px;">
					<div>
						<strong>근무 시간</strong> <span id="workingTime">00:00</span>
					</div>
					<div>
						<strong>상태</strong> <span id="attendanceStatus">대기중</span>
					</div>
				</div>
			</div>
		</div>

		<!-- 메신저 섹션 -->
		<div class="groupware-section messenger-section">
			<h2>💬 메신저</h2>
			<div class="chat-messages" id="chatMessages">
				<!-- 채팅 메시지가 여기에 표시됩니다 -->
			</div>
			<div class="messenger-form">
				<input type="text" id="messageInput" placeholder="메시지를 입력하세요..."
					maxlength="200" onkeypress="if(event.keyCode==13) sendMessage()">
				<button onclick="sendMessage()">전송</button>
			</div>
		</div>
	</div>
	<!-- JavaScript 파일 연결 -->
	<script src="${pageContext.request.contextPath}/js/jquery-3.7.1.js"></script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    <script src="${pageContext.request.contextPath}/js/notice_admin.js"></script>
    <script src="${pageContext.request.contextPath}/js/groupware.js"></script>
	<!-- JavaScript 코드는 별도 파일로 분리되었습니다 -->
</body>
</html>