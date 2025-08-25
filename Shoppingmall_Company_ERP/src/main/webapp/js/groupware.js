// groupware.js - 그룹웨어 전용 JavaScript 함수들

// 전역 변수들
let todos = [];
let messages = [];
let attendance = {};
let currentDate = new Date();

// 페이지 로드 시 실행
document.addEventListener('DOMContentLoaded', function() {
    initPage();
});

// 페이지 초기화
function initPage() {
    loadData();
    startClock();
    generateCalendar();
}

// 데이터 로드
function loadData() {
    todos = getFromStorage('todos') || [];
    messages = getFromStorage('messages') || [];
    attendance = getFromStorage('attendance') || {};
    
    displayTodos();
    displayMessages();
    displayAttendance();
}

// 시계 시작
function startClock() {
    updateTime();
    setInterval(updateTime, 1000);
}

// 현재 시간 표시
function updateTime() {
    const timeElement = document.getElementById('currentTime');
    if (timeElement) {
        const now = new Date();
        timeElement.textContent = now.toLocaleTimeString('ko-KR');
    }
}

// 공지사항 추가
/*function addNotice() {
    const title = document.getElementById('noticeTitle').value.trim();
    const content = document.getElementById('noticeContent').value.trim();
    
    if (!title || !content) {
        alert('제목과 내용을 입력해주세요.');
        return;
    }
    
    const notice = {
        id: Date.now(),
        title: title,
        content: content,
        date: new Date().toLocaleString('ko-KR')
    };
    
    const notices = getFromStorage('notices') || [];
    notices.unshift(notice);
    saveToStorage('notices', notices);
    
    // 입력 필드 초기화
    document.getElementById('noticeTitle').value = '';
    document.getElementById('noticeContent').value = '';
    
    displayNotices();
    alert('공지사항이 등록되었습니다.');
}
*/

// 공지사항 표시
/*function displayNotices() {
    const noticeList = document.getElementById('noticeList');
    if (!noticeList) return;
    
    const notices = getFromStorage('notices') || [];
    noticeList.innerHTML = '';
    
    notices.forEach(notice => {
        const div = document.createElement('div');
        div.className = 'notice-item';
        div.innerHTML = `
            <div class="notice-header">
                <strong>${notice.title}</strong>
                <small>${notice.date}</small>
            </div>
            <div>${notice.content}</div>
        `;
        noticeList.appendChild(div);
    });
}
*/

// 할일 추가
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
    saveToStorage('todos', todos);
    
    input.value = '';
    displayTodos();
}

// 할일 완료/미완료 토글
function toggleTodo(id) {
    todos = todos.map(todo => 
        todo.id === id ? {...todo, completed: !todo.completed} : todo
    );
    saveToStorage('todos', todos);
    displayTodos();
}

// 할일 목록 표시
function displayTodos() {
    const todoList = document.getElementById('todoList');
    if (!todoList) return;
    
    todoList.innerHTML = '';
    
    todos.forEach(todo => {
        const div = document.createElement('div');
        div.className = 'todo-item ' + (todo.completed ? 'completed' : '');
        div.innerHTML = `
            <input type="checkbox" ${todo.completed ? 'checked' : ''} 
                   onchange="toggleTodo(${todo.id})">
            <span>${todo.text}</span>
            <small>${todo.date}</small>
        `;
        todoList.appendChild(div);
    });
}

// 출근 처리
function checkIn() {
    const now = new Date();
    attendance.checkInTime = now.toLocaleString('ko-KR');
    attendance.status = '근무중';
    
    saveToStorage('attendance', attendance);
    displayAttendance();
    alert('출근 처리되었습니다.');
}

// 퇴근 처리
function checkOut() {
    const now = new Date();
    attendance.checkOutTime = now.toLocaleString('ko-KR');
    attendance.status = '퇴근완료';
    
    // 근무시간 계산
    if (attendance.checkInTime) {
        const checkIn = new Date(attendance.checkInTime);
        const checkOut = new Date();
        const diff = checkOut - checkIn;
        const hours = Math.floor(diff / (1000 * 60 * 60));
        const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
        attendance.workingTime = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
    }
    
    saveToStorage('attendance', attendance);
    displayAttendance();
    alert('퇴근 처리되었습니다.');
}

// 출퇴근 정보 표시
function displayAttendance() {
    const checkinTime = document.getElementById('checkinTime');
    const checkoutTime = document.getElementById('checkoutTime');
    const workingTime = document.getElementById('workingTime');
    const status = document.getElementById('attendanceStatus');
    
    if (checkinTime) checkinTime.textContent = attendance.checkInTime || '미등록';
    if (checkoutTime) checkoutTime.textContent = attendance.checkOutTime || '미등록';
    if (workingTime) workingTime.textContent = attendance.workingTime || '00:00';
    if (status) status.textContent = attendance.status || '대기중';
}

// 캘린더 생성
function generateCalendar() {
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    
    const monthElement = document.getElementById('currentMonth');
    if (monthElement) {
        monthElement.textContent = `${year}년 ${month + 1}월`;
    }
    
    const calendarBody = document.getElementById('calendarBody');
    if (!calendarBody) return;
    
    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const today = new Date();
    
    calendarBody.innerHTML = '';
    
    let date = 1;
    for (let i = 0; i < 6; i++) {
        const row = document.createElement('tr');
        
        for (let j = 0; j < 7; j++) {
            const cell = document.createElement('td');
            
            if (i === 0 && j < firstDay) {
                cell.textContent = '';
            } else if (date > daysInMonth) {
                cell.textContent = '';
            } else {
                cell.textContent = date;
                
                // 오늘 날짜 표시
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

// 월 변경
function changeMonth(delta) {
    currentDate.setMonth(currentDate.getMonth() + delta);
    generateCalendar();
}

// 메시지 전송
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
    saveToStorage('messages', messages);
    
    input.value = '';
    displayMessages();
    
    // 자동 응답
    setTimeout(() => {
        const reply = {
            id: Date.now(),
            text: '메시지를 확인했습니다.',
            sender: '동료',
            timestamp: new Date().toLocaleTimeString('ko-KR'),
            type: 'received'
        };
        messages.push(reply);
        saveToStorage('messages', messages);
        displayMessages();
    }, 1000);
}

// 메시지 목록 표시
function displayMessages() {
    const chatMessages = document.getElementById('chatMessages');
    if (!chatMessages) return;
    
    chatMessages.innerHTML = '';
    
    messages.slice(-20).forEach(message => {
        const div = document.createElement('div');
        div.className = 'message ' + message.type;
        div.innerHTML = `
            <div>${message.text}</div>
            <small>${message.sender} · ${message.timestamp}</small>
        `;
        chatMessages.appendChild(div);
    });
    
    chatMessages.scrollTop = chatMessages.scrollHeight;
}

// 로컬 스토리지에서 데이터 가져오기
function getFromStorage(key) {
    try {
        const stored = localStorage.getItem(key);
        return stored ? JSON.parse(stored) : null;
    } catch (e) {
        return null;
    }
}

// 로컬 스토리지에 데이터 저장
function saveToStorage(key, data) {
    try {
        localStorage.setItem(key, JSON.stringify(data));
    } catch (e) {
        console.error('저장 실패:', e);
    }
}
