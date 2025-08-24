<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*, java.time.*" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>그룹웨어 - Shoppingmall Company ERP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/groupware.css">
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
    <%@ include file="common-jsp/header.jsp" %>

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
                <input type="text" id="noticeTitle" placeholder="제목을 입력하세요" maxlength="100">
                <textarea id="noticeContent" placeholder="공지사항 내용을 입력하세요" maxlength="500"></textarea>
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
                        <th>일</th><th>월</th><th>화</th><th>수</th><th>목</th><th>금</th><th>토</th>
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
                <input type="text" id="todoInput" placeholder="새로운 할일을 입력하세요" maxlength="100">
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
                        <strong>출근 시간</strong>
                        <span id="checkinTime">미등록</span>
                    </div>
                    <div>
                        <strong>퇴근 시간</strong>
                        <span id="checkoutTime">미등록</span>
                    </div>
                </div>
                <div class="attendance-info" style="margin-top: 10px;">
                    <div>
                        <strong>근무 시간</strong>
                        <span id="workingTime">00:00</span>
                    </div>
                    <div>
                        <strong>상태</strong>
                        <span id="attendanceStatus">대기중</span>
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
                <input type="text" id="messageInput" placeholder="메시지를 입력하세요..." maxlength="200" onkeypress="if(event.keyCode==13) sendMessage()">
                <button onclick="sendMessage()">전송</button>
            </div>
        </div>
    </div>

    <script>
        // 전역 변수
        let currentDate = new Date();
        let notices = [];
        let todos = JSON.parse(localStorage.getItem('todos') || '[]');
        let messages = JSON.parse(localStorage.getItem('messages') || '[]');
        let attendance = {};

        // 페이지 로드 시 초기화
        document.addEventListener('DOMContentLoaded', function() {
            updateCurrentTime();
            setInterval(updateCurrentTime, 1000);
            generateCalendar();
            loadNoticesFromServer();
            loadTodos();
            loadMessages();
            loadAttendanceFromServer();
        });

        // 현재 시간 업데이트
        function updateCurrentTime() {
            const now = new Date();
            const timeString = now.toLocaleTimeString('ko-KR', {
                hour: '2-digit',
                minute: '2-digit',
                second: '2-digit'
            });
            document.getElementById('currentTime').textContent = timeString;
            
            if (attendance.checkInTime && !attendance.checkOutTime) {
                updateWorkingTime();
            }
        }

        // 공지사항 관련 함수 (서버 연동)
        function addNotice() {
            const title = document.getElementById('noticeTitle').value.trim();
            const content = document.getElementById('noticeContent').value.trim();
            
            if (!title || !content) {
                alert('제목과 내용을 모두 입력해주세요.');
                return;
            }

            // 서버로 공지사항 전송
            fetch('GroupwareServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'command=addNotice&title=' + encodeURIComponent(title) + '&content=' + encodeURIComponent(content)
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert(data.message);
                    document.getElementById('noticeTitle').value = '';
                    document.getElementById('noticeContent').value = '';
                    loadNoticesFromServer();
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('공지사항 등록 중 오류가 발생했습니다.');
            });
        }

        function loadNoticesFromServer() {
            fetch('GroupwareServlet?command=getNotices')
            .then(response => response.json())
            .then(data => {
                notices = data;
                displayNotices();
            })
            .catch(error => {
                console.error('Error:', error);
                // 오류 시 기존 방식으로 fallback
                loadNotices();
            });
        }

        function displayNotices() {
            const noticeList = document.getElementById('noticeList');
            noticeList.innerHTML = '';

            notices.forEach(notice => {
                const noticeItem = document.createElement('div');
                noticeItem.className = 'notice-item';
                
                // 날짜 형식 변환
                const createDate = new Date(notice.createDate).toLocaleString('ko-KR');
                
                noticeItem.innerHTML = 
                    '<div class="notice-header">' +
                        '<span class="notice-title">' + notice.title + '</span>' +
                        '<span class="notice-date">' + createDate + '</span>' +
                    '</div>' +
                    '<div class="notice-content">' + notice.content + '</div>' +
                    '<div class="notice-author">작성자: ' + notice.authorName + ' | 조회수: ' + notice.viewCount + '</div>';
                noticeList.appendChild(noticeItem);
            });
        }

        function loadNotices() {
            // 기존 localStorage 기반 방식 (fallback)
            const localNotices = JSON.parse(localStorage.getItem('notices') || '[]');
            const noticeList = document.getElementById('noticeList');
            noticeList.innerHTML = '';

            localNotices.forEach(notice => {
                const noticeItem = document.createElement('div');
                noticeItem.className = 'notice-item';
                noticeItem.innerHTML = 
                    '<div class="notice-header">' +
                        '<span class="notice-title">' + notice.title + '</span>' +
                        '<span class="notice-date">' + notice.date + '</span>' +
                    '</div>' +
                    '<div class="notice-content">' + notice.content + '</div>' +
                    '<div class="notice-author">작성자: ' + notice.author + '</div>';
                noticeList.appendChild(noticeItem);
            });
        }

        // 출퇴근 관리 관련 함수 (서버 연동)
        function checkIn() {
            fetch('GroupwareServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'command=checkIn'
            })
            .then(response => response.json())
            .then(data => {
                alert(data.message);
                if (data.success) {
                    loadAttendanceFromServer();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('출근 처리 중 오류가 발생했습니다.');
            });
        }

        function checkOut() {
            fetch('GroupwareServlet', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: 'command=checkOut'
            })
            .then(response => response.json())
            .then(data => {
                alert(data.message);
                if (data.success) {
                    loadAttendanceFromServer();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('퇴근 처리 중 오류가 발생했습니다.');
            });
        }

        function loadAttendanceFromServer() {
            fetch('GroupwareServlet?command=getTodayAttendance')
            .then(response => response.json())
            .then(data => {
                if (data) {
                    attendance = data;
                    displayAttendance();
                } else {
                    // 데이터가 없으면 기본값 설정
                    attendance = {};
                    displayAttendance();
                }
            })
            .catch(error => {
                console.error('Error:', error);
                // 오류 시 기존 방식으로 fallback
                loadAttendance();
            });
        }

        function displayAttendance() {
            // 시간 형식 변환 함수
            function formatTime(timestamp) {
                if (!timestamp) return '미등록';
                return new Date(timestamp).toLocaleTimeString('ko-KR');
            }

            document.getElementById('checkinTime').textContent = formatTime(attendance.checkInTime);
            document.getElementById('checkoutTime').textContent = formatTime(attendance.checkOutTime);

            if (attendance.checkInTime) {
                if (attendance.checkOutTime) {
                    document.getElementById('attendanceStatus').textContent = '퇴근완료';
                    // 총 근무시간 표시
                    if (attendance.totalWorkMinutes) {
                        const hours = Math.floor(attendance.totalWorkMinutes / 60);
                        const minutes = attendance.totalWorkMinutes % 60;
                        document.getElementById('workingTime').textContent = 
                            hours.toString().padStart(2, '0') + ':' + minutes.toString().padStart(2, '0');
                    }
                } else {
                    document.getElementById('attendanceStatus').textContent = '근무중';
                    updateWorkingTimeFromServer();
                }
            } else {
                document.getElementById('attendanceStatus').textContent = '대기중';
                document.getElementById('workingTime').textContent = '00:00';
            }
        }

        function updateWorkingTimeFromServer() {
            if (!attendance.checkInTime) return;

            const checkin = new Date(attendance.checkInTime);
            const now = new Date();
            const diff = now - checkin;
            const hours = Math.floor(diff / (1000 * 60 * 60));
            const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

            document.getElementById('workingTime').textContent = 
                hours.toString().padStart(2, '0') + ':' + minutes.toString().padStart(2, '0');
        }

        function loadAttendance() {
            // 기존 localStorage 기반 방식 (fallback)
            const today = new Date().toDateString();
            const localAttendance = JSON.parse(localStorage.getItem('attendance') || '{}');
            const todayAttendance = localAttendance[today] || {};

            document.getElementById('checkinTime').textContent = 
                todayAttendance.checkinTime || '미등록';
            document.getElementById('checkoutTime').textContent = 
                todayAttendance.checkoutTime || '미등록';

            if (todayAttendance.checkinTime) {
                if (todayAttendance.checkoutTime) {
                    document.getElementById('attendanceStatus').textContent = '퇴근완료';
                } else {
                    document.getElementById('attendanceStatus').textContent = '근무중';
                    updateWorkingTime();
                }
            } else {
                document.getElementById('attendanceStatus').textContent = '대기중';
            }
        }

        function updateWorkingTime() {
            const today = new Date().toDateString();
            const localAttendance = JSON.parse(localStorage.getItem('attendance') || '{}');
            const todayAttendance = localAttendance[today];
            
            if (!todayAttendance?.checkinTime) return;

            const checkin = new Date(today + ' ' + todayAttendance.checkinTime);
            const checkout = todayAttendance.checkoutTime ? 
                new Date(today + ' ' + todayAttendance.checkoutTime) : new Date();

            const diff = checkout - checkin;
            const hours = Math.floor(diff / (1000 * 60 * 60));
            const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

            document.getElementById('workingTime').textContent = 
                hours.toString().padStart(2, '0') + ':' + minutes.toString().padStart(2, '0');
        }

        // 캘린더 관련 함수 (기존 유지)
        function generateCalendar() {
            const year = currentDate.getFullYear();
            const month = currentDate.getMonth();
            
            document.getElementById('currentMonth').textContent = 
                year + '년 ' + (month + 1) + '월';

            const firstDay = new Date(year, month, 1).getDay();
            const daysInMonth = new Date(year, month + 1, 0).getDate();
            const today = new Date();

            const calendarBody = document.getElementById('calendarBody');
            calendarBody.innerHTML = '';

            let date = 1;
            for (let i = 0; i < 6; i++) {
                const row = document.createElement('tr');
                
                for (let j = 0; j < 7; j++) {
                    const cell = document.createElement('td');
                    
                    if (i === 0 && j < firstDay) {
                        cell.textContent = '';
                        cell.className = 'other-month';
                    } else if (date > daysInMonth) {
                        cell.textContent = '';
                        cell.className = 'other-month';
                    } else {
                        cell.textContent = date;
                        
                        if (year === today.getFullYear() && 
                            month === today.getMonth() && 
                            date === today.getDate()) {
                            cell.className = 'today';
                        }
                        date++;
                    }
                    
                    row.appendChild(cell);
                }
                
                calendarBody.appendChild(row);
                if (date > daysInMonth) break;
            }
        }

        function changeMonth(delta) {
            currentDate.setMonth(currentDate.getMonth() + delta);
            generateCalendar();
        }

        // 할일 목록 관련 함수 (기존 localStorage 방식 유지)
        function addTodo() {
            const input = document.getElementById('todoInput');
            const text = input.value.trim();
            
            if (!text) {
                alert('할일을 입력해주세요.');
                return;
            }

            const todo = {
                id: Date.now(),
                text: text,
                completed: false,
                date: new Date().toLocaleString('ko-KR')
            };

            todos.unshift(todo);
            localStorage.setItem('todos', JSON.stringify(todos));
            input.value = '';
            loadTodos();
        }

        function toggleTodo(id) {
            todos = todos.map(todo => 
                todo.id === id ? {...todo, completed: !todo.completed} : todo
            );
            localStorage.setItem('todos', JSON.stringify(todos));
            loadTodos();
        }

        function loadTodos() {
            const todoList = document.getElementById('todoList');
            todoList.innerHTML = '';

            todos.forEach(todo => {
                const todoItem = document.createElement('div');
                todoItem.className = 'todo-item ' + (todo.completed ? 'completed' : '');
                todoItem.innerHTML = 
                    '<input type="checkbox" ' + (todo.completed ? 'checked' : '') + 
                           ' onchange="toggleTodo(' + todo.id + ')">' +
                    '<span class="todo-text">' + todo.text + '</span>' +
                    '<span class="todo-date">' + todo.date + '</span>';
                todoList.appendChild(todoItem);
            });
        }

        // 메신저 관련 함수 (기존 localStorage 방식 유지)
        function sendMessage() {
            const input = document.getElementById('messageInput');
            const text = input.value.trim();
            
            if (!text) return;

            const message = {
                id: Date.now(),
                text: text,
                sender: '나',
                timestamp: new Date().toLocaleTimeString('ko-KR'),
                type: 'sent'
            };

            messages.push(message);
            localStorage.setItem('messages', JSON.stringify(messages));
            input.value = '';
            loadMessages();

            // 자동 응답 시뮬레이션
            setTimeout(() => {
                const autoReply = {
                    id: Date.now(),
                    text: '메시지를 확인했습니다.',
                    sender: '동료',
                    timestamp: new Date().toLocaleTimeString('ko-KR'),
                    type: 'received'
                };
                messages.push(autoReply);
                localStorage.setItem('messages', JSON.stringify(messages));
                loadMessages();
            }, 1000);
        }

        function loadMessages() {
            const chatMessages = document.getElementById('chatMessages');
            chatMessages.innerHTML = '';

            messages.slice(-20).forEach(message => {
                const messageDiv = document.createElement('div');
                messageDiv.className = 'message ' + message.type;
                messageDiv.innerHTML = 
                    '<div>' + message.text + '</div>' +
                    '<div class="message-info">' + message.sender + ' · ' + message.timestamp + '</div>';
                chatMessages.appendChild(messageDiv);
            });

            chatMessages.scrollTop = chatMessages.scrollHeight;
        }
    </script>
</body>
</html>