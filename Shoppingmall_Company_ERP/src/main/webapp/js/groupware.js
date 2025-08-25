// groupware.js - 그룹웨어 전용 JavaScript 함수들

// 전역 변수
let todos = getLocalTodos();
let messages = getLocalMessages();
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

// 출퇴근 관리 관련 함수 (서버 연동)
function checkIn() {
    fetch('GroupwareServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'command=checkIn'
    })
    .then(response => response.text())
    .then(data => {
        if (data.includes('success')) {
            showAlert('출근 처리되었습니다.', 'success');
            loadAttendanceFromServer();
        } else {
            showAlert('출근 처리에 실패했습니다.', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('출근 처리 중 오류가 발생했습니다.', 'error');
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
    .then(response => response.text())
    .then(data => {
        if (data.includes('success')) {
            showAlert('퇴근 처리되었습니다.', 'success');
            loadAttendanceFromServer();
        } else {
            showAlert('퇴근 처리에 실패했습니다.', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('퇴근 처리 중 오류가 발생했습니다.', 'error');
    });
}

function loadAttendanceFromServer() {
    fetch('GroupwareServlet?command=getTodayAttendance')
    .then(response => response.text())
    .then(data => {
        if (data && data.trim() !== '') {
            // 서버 응답을 파싱하여 attendance 객체에 저장
            parseAttendanceFromServer(data);
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

// 서버 응답을 파싱하여 attendance 객체에 저장
function parseAttendanceFromServer(data) {
    // 예시: "checkInTime:2024-01-01 09:00:00,checkOutTime:2024-01-01 18:00:00,totalWorkMinutes:540"
    attendance = {};
    const pairs = data.split(',');
    pairs.forEach(pair => {
        if (pair.trim()) {
            const [key, value] = pair.split(':');
            if (key && value) {
                attendance[key.trim()] = value.trim();
            }
        }
    });
}

function displayAttendance() {
    document.getElementById('checkinTime').textContent = formatTime(attendance.checkInTime);
    document.getElementById('checkoutTime').textContent = formatTime(attendance.checkOutTime);

    if (attendance.checkInTime) {
        if (attendance.checkOutTime) {
            document.getElementById('attendanceStatus').textContent = '퇴근완료';
            // 총 근무시간 표시
            if (attendance.totalWorkMinutes) {
                const hours = Math.floor(parseInt(attendance.totalWorkMinutes) / 60);
                const minutes = parseInt(attendance.totalWorkMinutes) % 60;
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

// 기존 localStorage 기반 출퇴근 관리 (fallback)
function loadAttendance() {
    const today = new Date().toDateString();
    const localAttendance = getLocalAttendance();
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
    const localAttendance = getLocalAttendance();
    const todayAttendance = localAttendance[today];
    
    if (!todayAttendance || !todayAttendance.checkinTime) return;

    const checkin = new Date(today + ' ' + todayAttendance.checkinTime);
    const checkout = todayAttendance.checkoutTime ? 
        new Date(today + ' ' + todayAttendance.checkoutTime) : new Date();

    const diff = checkout - checkin;
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));

    document.getElementById('workingTime').textContent = 
        hours.toString().padStart(2, '0') + ':' + minutes.toString().padStart(2, '0');
}

// localStorage에서 출퇴근 정보 가져오기 (JSON.parse 대신)
function getLocalAttendance() {
    const stored = localStorage.getItem('attendance');
    if (!stored) return {};
    
    try {
        const attendance = {};
        const lines = stored.split('\n');
        lines.forEach(line => {
            if (line.trim()) {
                const [date, data] = line.split('|');
                if (date && data) {
                    const pairs = data.split(',');
                    const dayAttendance = {};
                    pairs.forEach(pair => {
                        const [key, value] = pair.split(':');
                        if (key && value) {
                            dayAttendance[key.trim()] = value.trim();
                        }
                    });
                    attendance[date.trim()] = dayAttendance;
                }
            }
        });
        return attendance;
    } catch (e) {
        return {};
    }
}

// 캘린더 관련 함수
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

// 할일 목록 관련 함수
function addTodo() {
    const input = document.getElementById('todoInput');
    const text = input.value.trim();
    
    if (!text) {
        showAlert('할일을 입력해주세요.', 'warning');
        return;
    }

    const todo = {
        id: Date.now(),
        text: text,
        completed: false,
        date: new Date().toLocaleString('ko-KR')
    };

    todos.unshift(todo);
    saveTodos();
    input.value = '';
    loadTodos();
}

function toggleTodo(id) {
    todos = todos.map(todo => 
        todo.id === id ? {...todo, completed: !todo.completed} : todo
    );
    saveTodos();
    loadTodos();
}

function loadTodos() {
    const todoList = document.getElementById('todoList');
    if (!todoList) return;
    
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

// localStorage에 할일 저장 (JSON.stringify 대신)
function saveTodos() {
    const todoStrings = todos.map(todo => 
        `${todo.id}|${todo.text}|${todo.completed}|${todo.date}`
    ).join('\n');
    localStorage.setItem('todos', todoStrings);
}

// localStorage에서 할일 가져오기 (JSON.parse 대신)
function getLocalTodos() {
    const stored = localStorage.getItem('todos');
    if (!stored) return [];
    
    try {
        const todos = [];
        const lines = stored.split('\n');
        lines.forEach(line => {
            if (line.trim()) {
                const parts = line.split('|');
                if (parts.length >= 4) {
                    todos.push({
                        id: parseInt(parts[0]),
                        text: parts[1],
                        completed: parts[2] === 'true',
                        date: parts[3]
                    });
                }
            }
        });
        return todos;
    } catch (e) {
        return [];
    }
}

// 메신저 관련 함수
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
    saveMessages();
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
        saveMessages();
        loadMessages();
    }, 1000);
}

function loadMessages() {
    const chatMessages = document.getElementById('chatMessages');
    if (!chatMessages) return;
    
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

// localStorage에 메시지 저장 (JSON.stringify 대신)
function saveMessages() {
    const messageStrings = messages.map(message => 
        `${message.id}|${message.text}|${message.sender}|${message.timestamp}|${message.type}`
    ).join('\n');
    localStorage.setItem('messages', messageStrings);
}

// localStorage에서 메시지 가져오기 (JSON.parse 대신)
function getLocalMessages() {
    const stored = localStorage.getItem('messages');
    if (!stored) return [];
    
    try {
        const messages = [];
        const lines = stored.split('\n');
        lines.forEach(line => {
            if (line.trim()) {
                const parts = line.split('|');
                if (parts.length >= 5) {
                    messages.push({
                        id: parseInt(parts[0]),
                        text: parts[1],
                        sender: parts[2],
                        timestamp: parts[3],
                        type: parts[4]
                    });
                }
            }
        });
        return messages;
    } catch (e) {
        return [];
    }
}
