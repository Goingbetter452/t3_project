// groupware.js - 그룹웨어 전용 JavaScript (JSON 의존성 제거 버전)

// 전역 상태
let todos = [];
let messages = [];
let attendance = {};
let currentDate = new Date();
let currentUser = null; // 페이지 로드 시 세션에서 가져올 예정

// --- 유틸: 로컬스토리지 (Plain Text 직렬화) ---
function saveToStorage(name, value) {
    try {
        if (name === 'todos') {
            // id|completed|text|createdAt 
            const lines = (Array.isArray(value) ? value : []).map(t => [
                String(t.id || ''),
                t.completed ? '1' : '0',
                (t.text || '').replace(/\n/g, ' '),
                String(t.createdAt || '')
            ].join('|'));
            localStorage.setItem('todos', lines.join('\n'));
            return;
        }
        if (name === 'attendance') {
            // key:value comma-joined
            const flat = [
                'checkInTime:' + (value.checkInTime || ''),
                'checkOutTime:' + (value.checkOutTime || ''),
                'workingTime:' + (value.workingTime || ''),
                'status:' + (value.status || '')
            ].join(',');
            localStorage.setItem('attendance', flat);
            return;
        }
        // 기본 문자열 저장
        localStorage.setItem(name, String(value || ''));
    } catch (e) {
        console.error('saveToStorage 실패:', name, e);
    }
}

function getFromStorage(name) {
    try {
        const raw = localStorage.getItem(name);
        if (!raw) return name === 'attendance' ? {} : [];

        if (name === 'todos') {
            // id|completed|text|createdAt per line
            return raw.split('\n').filter(Boolean).map(line => {
                const [id, completed, text, createdAt] = line.split('|');
                return {
                    id: Number(id),
                    completed: completed === '1',
                    text: text || '',
                    createdAt: createdAt || ''
                };
            });
        }
        if (name === 'attendance') {
            const obj = {};
            raw.split(',').forEach(kv => {
                const [k, ...rest] = kv.split(':');
                obj[k] = rest.join(':');
            });
            return obj;
        }

        return raw;
    } catch (e) {
        console.error('getFromStorage 실패:', name, e);
        return name === 'attendance' ? {} : [];
    }
}

// 시계
function startClock() {
  updateTime();
  setInterval(updateTime, 1000);
}

function updateTime() {
  const el = document.getElementById('currentTime');
  if (!el) return;
  const now = new Date();
  el.textContent = now.toLocaleTimeString('ko-KR');
}

function formatKoreanDate(isoString) {
  if (!isoString) return '';
  const d = new Date(isoString);
  if (isNaN(d.getTime())) return String(isoString);
  return d.toLocaleString('ko-KR');
}

// 페이지 초기화
document.addEventListener('DOMContentLoaded', function () {
  initPage();
});

function initPage() {
    // 현재 로그인한 사용자 정보를 세션에서 가져오기
    getCurrentUser();
    
    // 페이지 로드 시 기존 localStorage 출퇴근 데이터 초기화
    localStorage.removeItem('attendance');
    
    // 공지: 서버에서 텍스트로 로드 (notice_admin.js 의 함수 사용)
    if (typeof loadNoticesFromServer === 'function') {
        loadNoticesFromServer();
    }

    attendance = {}; // 빈 객체로 초기화
    
    // 서버에서 데이터 로드
    loadTodosFromServer();
    loadAttendanceFromServer();
    loadMessagesFromServer();

    startClock();
    generateCalendar();
}

// 현재 로그인한 사용자 정보 가져오기 (JSP에서 주입된 전역 변수 우선)
function getCurrentUser() {
    if (!currentUser && typeof window !== 'undefined' && window.CURRENT_USER_ID) {
        currentUser = String(window.CURRENT_USER_ID).trim();
        console.log('현재 사용자(window):', currentUser);
        return currentUser;
    }
    // JSP의 세션 디버그 텍스트에서 파싱 (fallback)
    const sessionDebugDiv = document.getElementById('sessionDebug');
    if (sessionDebugDiv) {
        const text = sessionDebugDiv.textContent || '';
        const match = text.match(/ID:\s*([^)]+)\)/);
        if (match) {
            currentUser = match[1].trim();
            console.log('현재 사용자(parsed):', currentUser);
            return currentUser;
        }
    }
    return currentUser;
}

// ===== 공지 =====
// 이 파일에서는 addNotice/displayNotices 를 정의하지 않음 (notice_admin.js 사용)

// ===== 할일 =====
function loadTodosFromServer() {
  fetch('GroupwareServlet?command=getTodos')
    .then(r => r.text())
    .then(text => {
      todos = [];
      if (text && text.trim() !== '') {
        text.split('%%%').forEach(todoStr => {
          const [todoId, title, description, isCompleted, priority, dueDate, createDate] = todoStr.split('|');
          todos.push({
            id: parseInt(todoId),
            text: title || '',
            description: description || '',
            completed: isCompleted === 'Y',
            priority: parseInt(priority) || 3,
            dueDate: dueDate || '',
            createdAt: createDate || ''
          });
        });
      }
      displayTodos();
    })
    .catch(() => {
      console.log('서버에서 할일 목록을 가져올 수 없습니다.');
      todos = [];
      displayTodos();
    });
}

function addTodo() {
  const input = document.getElementById('todoInput');
  if (!input) {
    alert('입력창을 찾을 수 없습니다.');
    return;
  }
  const text = (input.value || '').trim();
  if (!text) {
    alert('할일을 입력해주세요.');
    return;
  }
  
  // 서버에 할일 추가 요청
  fetch('GroupwareServlet', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'command=addTodo&title=' + encodeURIComponent(text)
  })
  .then(r => r.text())
  .then(t => {
    if ((t || '').indexOf('success') !== -1) {
      input.value = '';
      loadTodosFromServer(); // 서버에서 최신 목록 다시 로드
    } else {
      alert('할일 추가에 실패했습니다.');
    }
  })
  .catch(() => alert('할일 추가 중 오류가 발생했습니다.'));
}

function toggleTodo(id) {
  // 서버에 할일 완료/미완료 토글 요청
  fetch('GroupwareServlet', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'command=toggleTodo&todoId=' + encodeURIComponent(id)
  })
  .then(r => r.text())
  .then(t => {
    if ((t || '').indexOf('success') !== -1) {
      loadTodosFromServer(); // 서버에서 최신 목록 다시 로드
    } else {
      alert('할일 상태 변경에 실패했습니다.');
    }
  })
  .catch(() => alert('할일 상태 변경 중 오류가 발생했습니다.'));
}

function displayTodos() {
  const list = document.getElementById('todoList');
  if (!list) return;
  list.innerHTML = '';
  (todos || []).forEach(t => {
    const item = document.createElement('div');
    item.className = 'todo-item ' + (t.completed ? 'completed' : '');

    const cb = document.createElement('input');
    cb.type = 'checkbox';
    cb.checked = !!t.completed;
    cb.addEventListener('change', () => toggleTodo(t.id));

    const span = document.createElement('span');
    span.className = 'todo-text';
    span.textContent = t.text;

    const small = document.createElement('small');
    small.className = 'todo-date';
    small.textContent = formatKoreanDate(t.createdAt);

    // 삭제 버튼 추가
    const deleteBtn = document.createElement('button');
    deleteBtn.textContent = '×';
    deleteBtn.className = 'todo-delete-btn';
    deleteBtn.style.cssText = 'margin-left: 10px; background: #ff4444; color: white; border: none; padding: 2px 6px; border-radius: 3px; cursor: pointer;';
    deleteBtn.addEventListener('click', (e) => {
      e.stopPropagation();
      deleteTodo(t.id);
    });

    item.appendChild(cb);
    item.appendChild(span);
    item.appendChild(small);
    item.appendChild(deleteBtn);
    list.appendChild(item);
  });
}

function deleteTodo(id) {
  if (!confirm('이 할일을 삭제하시겠습니까?')) {
    return;
  }
  
  // 서버에 할일 삭제 요청
  fetch('GroupwareServlet', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'command=deleteTodo&todoId=' + encodeURIComponent(id)
  })
  .then(r => r.text())
  .then(t => {
    if ((t || '').indexOf('success') !== -1) {
      loadTodosFromServer(); // 서버에서 최신 목록 다시 로드
    } else {
      alert('할일 삭제에 실패했습니다.');
    }
  })
  .catch(() => alert('할일 삭제 중 오류가 발생했습니다.'));
}

// ===== 메신저 =====
function loadMessagesFromServer() {
    fetch('GroupwareServlet?command=getMessages')
        .then(response => {
            if (response.status === 401) {
                console.log('로그인이 필요합니다.');
                return '';
            }
            return response.text();
        })
        .then(text => {
            messages = [];
            if (!text) {
                displayMessages();
                return;
            }
            text.split('%%%').forEach(msgStr => {
                const [id, type, senderId, senderName, receiverId, receiverName, content, isRead, sendDate, readDate] = msgStr.split('|');
                messages.push({
                    id: parseInt(id),
                    type: type || 'PERSONAL',
                    senderId: senderId,
                    senderName: senderName,
                    receiverId: receiverId,
                    receiverName: receiverName,
                    text: content,
                    isRead: isRead === 'Y',
                    timestamp: sendDate,
                    readDate: readDate
                });
            });
            
            displayMessages();
        })
        .catch(error => {
            console.error('메시지 로드 실패:', error);
            messages = [];
            displayMessages();
        });
}

function displayMessages() {
    // 현재 사용자 식별자 보장 시도
    if (!currentUser) getCurrentUser();

    const box = document.getElementById('chatMessages');
    if (!box) return;
    box.innerHTML = '';
    
    (messages || []).slice(-20).forEach(m => {
        const div = document.createElement('div');
        const me = (m.senderId || '').trim().toLowerCase();
        const meNow = (currentUser || '').trim().toLowerCase();
        const isSentByMe = me && meNow && me === meNow;
        div.className = 'message ' + (isSentByMe ? 'sent' : 'received');
        
        if (!m.isRead && !isSentByMe) {
            div.classList.add('unread');
            markMessageAsRead(m.id);
        }
        
        const messageHeader = isSentByMe ? 
            `<strong>To: ${m.receiverId === 'ALL' ? '전체' : (m.receiverName || m.receiverId || '')}</strong>` :
            `<strong>From: ${m.senderName || m.senderId}</strong>`;
        
        div.innerHTML = `
            <div class="message-header">
                ${messageHeader}
                <div class="message-actions">
                    ${!isSentByMe ? 
                        `<button class="reply-btn" onclick="replyToMessage('${(m.senderId || '').replace(/'/g, "&#39;")}', '${(m.senderName || m.senderId || '').replace(/'/g, "&#39;")}')">답장</button>` : 
                        ''}
                    <button class="delete-btn" onclick="deleteMessage(${m.id})">삭제</button>
                </div>
            </div>
            <div class="message-content">${m.text || ''}</div>
            <div class="message-info">
                ${formatKoreanDate(m.timestamp)}
                ${m.isRead ? '<span class="read-mark">읽음</span>' : ''}
            </div>
        `;
        
        box.appendChild(div);
    });
    
    box.scrollTop = box.scrollHeight;
}

function sendMessage() {
    const input = document.getElementById('messageInput');
    const receiverSelect = document.getElementById('messageReceiver');
    if (!input || !receiverSelect) return;
    
    const text = (input.value || '').trim();
    if (!text) return;
    
    const receiverId = receiverSelect.value;
    const messageType = receiverId === 'ALL' ? 'BROADCAST' : 'PERSONAL';

    // 서버에 메시지 전송 요청
    fetch('GroupwareServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'command=sendMessage' +
              '&content=' + encodeURIComponent(text) +
              '&messageType=' + messageType +
              '&receiverId=' + encodeURIComponent(receiverId)
    })
    .then(response => {
        if (response.status === 401) {
            throw new Error('로그인이 필요합니다.');
        }
        return response.text();
    })
    .then(result => {
        if (result === 'success') {
            input.value = '';
            loadMessagesFromServer();
        } else {
            alert('메시지 전송에 실패했습니다.');
        }
    })
    .catch(error => {
        alert(error.message);
        console.error('Error:', error);
    });
}

function deleteMessage(messageId) {
    if (!confirm('메시지를 삭제하시겠습니까?')) return;
    
    fetch('GroupwareServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'command=deleteMessage&messageId=' + messageId
    })
    .then(response => {
        if (response.status === 401) {
            throw new Error('로그인이 필요합니다.');
        }
        return response.text();
    })
    .then(result => {
        if (result === 'success') {
            loadMessagesFromServer();
        } else {
            alert('메시지 삭제에 실패했습니다.');
        }
    })
    .catch(error => {
        alert(error.message);
        console.error('Error:', error);
    });
}

function markMessageAsRead(messageId) {
    fetch('GroupwareServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'command=markMessageRead&messageId=' + messageId
    })
    .then(response => {
        if (response.status === 401) return;
        return response.text();
    })
    .then(result => {
        if (result === 'success') {
            loadMessagesFromServer();
        }
    })
    .catch(error => console.error('Error:', error));
}

function replyToMessage(userId, userName) {
    const receiverSelect = document.getElementById('messageReceiver');
    if (receiverSelect) {
        receiverSelect.value = userId;
    }
    const input = document.getElementById('messageInput');
    if (input) {
        input.focus();
        input.value = `@${userName} `;
    }
}

// ===== 근태 (서버 연동, text/plain) =====
function checkIn() {
  fetch('GroupwareServlet', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'command=checkIn'
  })
  .then(r => r.text())
  .then(t => {
    if ((t || '').indexOf('success') !== -1) {
      alert('출근 처리되었습니다.');
      loadAttendanceFromServer();
    } else {
      alert('이미 출근했거나 처리에 실패했습니다.');
    }
  })
  .catch(() => alert('출근 처리 중 오류가 발생했습니다.'));
}

function checkOut() {
  fetch('GroupwareServlet', {
    method: 'POST',
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
    body: 'command=checkOut'
  })
  .then(r => r.text())
  .then(t => {
    if ((t || '').indexOf('success') !== -1) {
      alert('퇴근 처리되었습니다.');
      loadAttendanceFromServer();
    } else {
      alert('퇴근 처리에 실패했습니다.');
    }
  })
  .catch(() => alert('퇴근 처리 중 오류가 발생했습니다.'));
}

function loadAttendanceFromServer() {
  fetch('GroupwareServlet?command=getTodayAttendance')
    .then(r => r.text())
    .then(text => {
      attendance = {};
      if (text && text.trim() !== '') {
        text.split(',').forEach(kv => {
          const [k, ...rest] = kv.split(':');
          attendance[k] = rest.join(':');
        });
      }
      // 근무시간 계산(퇴근 전이면 실시간 갱신)
      if (attendance.checkInTime && (!attendance.checkOutTime || attendance.checkOutTime.trim() === '')) {
        updateWorkingTimeLive();
      }
      displayAttendance();
      // 서버에서 가져온 데이터만 저장 (로컬 데이터는 사용하지 않음)
      saveToStorage('attendance', attendance);
    })
    .catch(() => {
      console.log('서버에서 출퇴근 정보를 가져올 수 없습니다.');
      // 서버 요청 실패 시 빈 출퇴근 정보로 초기화
      attendance = {};
      displayAttendance();
    });
}

function updateWorkingTimeLive() {
  const t = attendance.checkInTime;
  if (!t) return;
  const start = new Date(t);
  if (isNaN(start.getTime())) return;
  const now = new Date();
  const diff = now - start;
  if (diff < 0) return;
  const h = Math.floor(diff / 3600000);
  const m = Math.floor((diff % 3600000) / 60000);
  attendance.workingTime = String(h).padStart(2, '0') + ':' + String(m).padStart(2, '0');
}

function displayAttendance() {
  const ci = document.getElementById('checkinTime');
  const co = document.getElementById('checkoutTime');
  const wt = document.getElementById('workingTime');
  const st = document.getElementById('attendanceStatus');
  
  if (ci) ci.textContent = attendance.checkInTime ? formatKoreanDate(attendance.checkInTime) : '미등록';
  if (co) co.textContent = (attendance.checkOutTime && attendance.checkOutTime.trim() !== '') 
    ? formatKoreanDate(attendance.checkOutTime) : '-';
  
  // 상태 판단 로직 수정: 빈 문자열도 고려
  if (st) {
    if (!attendance.checkInTime || attendance.checkInTime.trim() === '') {
      st.textContent = '대기중';
    } else if (!attendance.checkOutTime || attendance.checkOutTime.trim() === '') {
      st.textContent = '근무중';
    } else {
      st.textContent = '퇴근완료';
    }
  }
  
  if (wt) wt.textContent = attendance.workingTime || '00:00';
}

// ===== 캘린더 =====
let events = [];  // 일정 데이터 저장

function generateCalendar() {
    const year = currentDate.getFullYear();
    const month = currentDate.getMonth();
    const firstDay = new Date(year, month, 1).getDay();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const today = new Date();

    const curr = document.getElementById('currentMonth');
    if (curr) curr.textContent = year + '년 ' + (month + 1) + '월';

    // 월별 일정 로드
    loadMonthlyEvents(year + '-' + String(month + 1).padStart(2, '0'));

    const body = document.getElementById('calendarBody');
    if (!body) return;
    body.innerHTML = '';

    let dateNum = 1;
    for (let i = 0; i < 6; i++) {
        const row = document.createElement('tr');
        for (let j = 0; j < 7; j++) {
            const cell = document.createElement('td');
            if (i === 0 && j < firstDay) {
                cell.textContent = '';
                cell.className = 'other-month';
            } else if (dateNum > daysInMonth) {
                cell.textContent = '';
                cell.className = 'other-month';
            } else {
                cell.textContent = dateNum;
                if (year === today.getFullYear() && month === today.getMonth() && dateNum === today.getDate()) {
                    cell.className = 'today';
                }
                
                // 일정 추가를 위한 클릭 이벤트 (클로저로 currentDateNum 캡처)
                const currentDateNum = dateNum;
                cell.addEventListener('click', function(e) {
                    e.preventDefault();
                    e.stopPropagation();
                    console.log('날짜 클릭됨:', year, month + 1, currentDateNum); // 디버깅용
                    showEventForm(year, month, currentDateNum);
                });
                
                // 마우스 포인터 표시
                cell.style.cursor = 'pointer';
                
                dateNum++;
            }
            row.appendChild(cell);
        }
        body.appendChild(row);
        if (dateNum > daysInMonth) break;
    }
}

function loadMonthlyEvents(yearMonth) {
    fetch('GroupwareServlet?command=getMonthlyEvents&yearMonth=' + yearMonth)
        .then(response => {
            if (response.status === 401) {
                console.log('로그인이 필요합니다.');
                return '';
            }
            return response.text();
        })
        .then(text => {
            events = [];
            if (!text) return;
            
            text.split('%%%').forEach(eventStr => {
                const [id, title, desc, type, start, end, allDay, loc] = eventStr.split('|');
                events.push({
                    eventId: parseInt(id),
                    title: title || '',
                    description: desc || '',
                    eventType: type || 'PERSONAL',
                    startDate: start || '',
                    endDate: end || '',
                    isAllDay: allDay === 'Y',
                    location: loc || ''
                });
            });
            
            // 캘린더 UI 업데이트
            updateCalendarUI();
        })
        .catch(error => console.error('일정 로드 실패:', error));
}

function updateCalendarUI() {
    const cells = document.querySelectorAll('#calendarBody td');
    cells.forEach(cell => {
        // 셀에 텍스트 내용이 있고, other-month 클래스가 없는 경우만 처리
        if (cell.textContent && !cell.classList.contains('other-month')) {
            const date = parseInt(cell.textContent);
            
            // 해당 날짜의 모든 일정 찾기
            const dayEvents = events.filter(event => {
                const eventDate = new Date(event.startDate);
                return eventDate.getDate() === date &&
                       eventDate.getMonth() === currentDate.getMonth() &&
                       eventDate.getFullYear() === currentDate.getFullYear();
            });
            
            if (dayEvents.length > 0) {
                cell.classList.add('has-events');
                
                // 기존 날짜 텍스트 저장
                const dateText = cell.textContent;
                
                // 기존 클릭 이벤트 리스너 보존을 위해 기존 내용 확인
                let dateDiv = cell.querySelector('.calendar-date');
                let dotsContainer = cell.querySelector('.event-dots');
                
                // 날짜 div가 없으면 새로 생성
                if (!dateDiv) {
                    // 셀 내용 초기화
                    cell.textContent = '';
                    
                    // 날짜 표시
                    dateDiv = document.createElement('div');
                    dateDiv.className = 'calendar-date';
                    dateDiv.textContent = dateText;
                    cell.appendChild(dateDiv);
                }
                
                // 기존 도트 컨테이너 제거 후 새로 생성
                if (dotsContainer) {
                    dotsContainer.remove();
                }
                
                // 일정 도트 컨테이너 생성
                dotsContainer = document.createElement('div');
                dotsContainer.className = 'event-dots';
                
                // 일정 종류별로 그룹화
                const eventsByType = {};
                dayEvents.forEach(event => {
                    if (!eventsByType[event.eventType]) {
                        eventsByType[event.eventType] = [];
                    }
                    eventsByType[event.eventType].push(event);
                });
                
                // 각 일정 종류별로 하나의 도트만 표시
                Object.keys(eventsByType).forEach(type => {
                    const dot = document.createElement('span');
                    dot.className = `calendar-event-dot event-${type}`;
                    dot.title = `${eventsByType[type].length}개의 ${type} 일정`;
                    
                    // 도트 클릭시 일정 목록 보기
                    dot.addEventListener('click', function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        showDayEvents(currentDate.getFullYear(), currentDate.getMonth(), date, dayEvents);
                    });
                    
                    dotsContainer.appendChild(dot);
                });
                
                cell.appendChild(dotsContainer);
            } else {
                cell.classList.remove('has-events');
                
                // 일정이 없는 경우 기본 날짜만 표시
                const dateDiv = cell.querySelector('.calendar-date');
                const dotsContainer = cell.querySelector('.event-dots');
                
                if (dateDiv || dotsContainer) {
                    // 기존 구조가 있으면 도트만 제거하고 날짜는 유지
                    if (dotsContainer) {
                        dotsContainer.remove();
                    }
                    if (!dateDiv) {
                        const dateText = cell.textContent;
                        cell.textContent = '';
                        const newDateDiv = document.createElement('div');
                        newDateDiv.className = 'calendar-date';
                        newDateDiv.textContent = dateText;
                        cell.appendChild(newDateDiv);
                    }
                }
            }
        }
    });
}

// 특정 날짜의 일정 목록을 보여주는 함수
function showDayEvents(year, month, date, dayEvents) {
    console.log('일정 목록 표시:', year, month + 1, date, dayEvents);
    
    // 기존 모달 제거
    const existingModal = document.querySelector('.event-list-modal');
    if (existingModal) existingModal.remove();
    
    const modal = document.createElement('div');
    modal.className = 'event-list-modal';
    
    let eventsHtml = '';
    dayEvents.forEach(event => {
        const startTime = new Date(event.startDate).toLocaleTimeString('ko-KR', {
            hour: '2-digit',
            minute: '2-digit'
        });
        const endTime = new Date(event.endDate).toLocaleTimeString('ko-KR', {
            hour: '2-digit',
            minute: '2-digit'
        });
        
        const typeText = {
            'PERSONAL': '개인 일정',
            'MEETING': '회의',
            'HOLIDAY': '휴가',
            'COMPANY': '회사 일정'
        }[event.eventType] || event.eventType;
        
        eventsHtml += `
            <div class="event-item">
                <div class="event-header">
                    <span class="event-title">${event.title}</span>
                    <span class="event-type event-type-${event.eventType}">${typeText}</span>
                </div>
                <div class="event-time">
                    ${event.isAllDay ? '종일' : `${startTime} - ${endTime}`}
                </div>
                ${event.description ? `<div class="event-description">${event.description}</div>` : ''}
                ${event.location ? `<div class="event-location">📍 ${event.location}</div>` : ''}
                <div class="event-actions">
                    <button onclick="editEvent(${event.eventId})">수정</button>
                    <button onclick="deleteEventConfirm(${event.eventId})">삭제</button>
                </div>
            </div>
        `;
    });
    
    modal.innerHTML = `
        <div class="event-list-form">
            <div class="event-list-header">
                <h3>${year}년 ${month + 1}월 ${date}일 일정</h3>
                <button onclick="closeEventListModal()" class="close-btn">×</button>
            </div>
            <div class="event-list-content">
                ${eventsHtml}
            </div>
            <div class="event-list-footer">
                <button onclick="closeEventListModal(); showEventForm(${year}, ${month}, ${date})">
                    새 일정 추가
                </button>
                <button onclick="closeEventListModal()">닫기</button>
            </div>
        </div>
    `;
    
    document.body.appendChild(modal);
    
    // 모달 외부 클릭시 닫기
    modal.addEventListener('click', e => {
        if (e.target === modal) closeEventListModal();
    });
}

// 일정 목록 모달 닫기
function closeEventListModal() {
    const modal = document.querySelector('.event-list-modal');
    if (modal) modal.remove();
}

// 일정 삭제 확인
function deleteEventConfirm(eventId) {
    if (confirm('이 일정을 삭제하시겠습니까?')) {
        deleteEvent(eventId);
    }
}

// 일정 삭제 처리
function deleteEvent(eventId) {
    fetch('GroupwareServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'command=deleteEvent&eventId=' + encodeURIComponent(eventId)
    })
    .then(response => {
        if (response.status === 401) {
            throw new Error('로그인이 필요합니다.');
        }
        return response.text();
    })
    .then(result => {
        if (result === 'success') {
            alert('일정이 삭제되었습니다.');
            closeEventListModal();
            const yearMonth = currentDate.getFullYear() + '-' + String(currentDate.getMonth() + 1).padStart(2, '0');
            loadMonthlyEvents(yearMonth);
        } else {
            alert('일정 삭제에 실패했습니다.');
        }
    })
    .catch(error => {
        alert(error.message);
        console.error('Error:', error);
    });
}

function changeMonth(delta) {
    currentDate.setMonth(currentDate.getMonth() + delta);
    generateCalendar();
}

// 일정 추가 폼 표시
function showEventForm(year, month, date) {
    console.log('showEventForm 호출됨:', year, month + 1, date); // 디버깅용
    
    // 날짜 유효성 검사
    if (!year || !month && month !== 0 || !date) {
        console.error('Invalid parameters:', year, month, date);
        alert('유효하지 않은 날짜입니다.');
        return;
    }

    // 이미 존재하는 모달 제거
    const existingModal = document.querySelector('.event-modal');
    if (existingModal) {
        console.log('기존 모달 제거');
        existingModal.remove();
    }

    console.log('새 모달 생성 중...'); // 디버깅용

    const modal = document.createElement('div');
    modal.className = 'event-modal';
    modal.innerHTML = `
        <div class="event-form">
            <h3>${year}년 ${month + 1}월 ${date}일 일정</h3>
            <input type="text" id="eventTitle" placeholder="일정 제목" required>
            <textarea id="eventDescription" placeholder="일정 설명"></textarea>
            <select id="eventType">
                <option value="PERSONAL">개인 일정</option>
                <option value="MEETING">회의</option>
                <option value="HOLIDAY">휴가</option>
                <option value="COMPANY">회사 일정</option>
            </select>
            <div>
                <label>시작 시간:</label>
                <input type="time" id="eventStartTime" value="09:00">
            </div>
            <div>
                <label>종료 시간:</label>
                <input type="time" id="eventEndTime" value="18:00">
            </div>
            <div>
                <label>
                    <input type="checkbox" id="eventAllDay">
                    종일
                </label>
            </div>
            <input type="text" id="eventLocation" placeholder="장소">
            <div class="modal-buttons">
                <button onclick="saveEvent(${year}, ${month}, ${date})">저장</button>
                <button onclick="closeEventForm()">취소</button>
            </div>
        </div>
    `;
    
    console.log('모달을 body에 추가'); // 디버깅용
    document.body.appendChild(modal);
    
    // 모달이 제대로 추가되었는지 확인
    const addedModal = document.querySelector('.event-modal');
    if (addedModal) {
        console.log('모달이 성공적으로 추가됨');
    } else {
        console.error('모달 추가 실패');
    }
    
    // ESC 키로 모달 닫기
    modal.addEventListener('keydown', e => {
        if (e.key === 'Escape') closeEventForm();
    });
    
    // 백그라운드 클릭으로 모달 닫기
    modal.addEventListener('click', e => {
        if (e.target === modal) closeEventForm();
    });
}

// 일정 폼 모달 닫기
function closeEventForm() {
    const modal = document.querySelector('.event-modal');
    if (modal) {
        console.log('일정 추가 폼 모달 닫기');
        modal.remove();
    }
}

// 일정 저장
function saveEvent(year, month, date) {
    const title = document.getElementById('eventTitle').value.trim();
    if (!title) {
        alert('일정 제목을 입력해주세요.');
        return;
    }
    
    const startTime = document.getElementById('eventStartTime').value;
    const endTime = document.getElementById('eventEndTime').value;
    const isAllDay = document.getElementById('eventAllDay').checked;
    
    const startDate = new Date(year, month, date);
    if (!isAllDay) {
        const [startHour, startMinute] = startTime.split(':');
        startDate.setHours(parseInt(startHour), parseInt(startMinute));
    }
    
    const endDate = new Date(year, month, date);
    if (!isAllDay) {
        const [endHour, endMinute] = endTime.split(':');
        endDate.setHours(parseInt(endHour), parseInt(endMinute));
    } else {
        endDate.setHours(23, 59, 59);
    }
    
    const event = {
        title: title,
        description: document.getElementById('eventDescription').value,
        eventType: document.getElementById('eventType').value,
        startDate: startDate.toISOString().replace('T', ' ').split('.')[0],
        endDate: endDate.toISOString().replace('T', ' ').split('.')[0],
        isAllDay: isAllDay ? 'Y' : 'N',
        location: document.getElementById('eventLocation').value
    };
    
    // 서버에 일정 저장
    fetch('GroupwareServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'command=addEvent' +
              '&title=' + encodeURIComponent(event.title) +
              '&description=' + encodeURIComponent(event.description) +
              '&eventType=' + encodeURIComponent(event.eventType) +
              '&startDate=' + encodeURIComponent(event.startDate) +
              '&endDate=' + encodeURIComponent(event.endDate) +
              '&isAllDay=' + encodeURIComponent(event.isAllDay) +
              '&location=' + encodeURIComponent(event.location)
    })
    .then(response => {
        if (response.status === 401) {
            throw new Error('로그인이 필요합니다.');
        }
        return response.text();
    })
    .then(result => {
        if (result === 'success') {
            alert('일정이 저장되었습니다.');
            closeEventForm();
            loadMonthlyEvents(year + '-' + String(month + 1).padStart(2, '0'));
        } else {
            alert('일정 저장에 실패했습니다.');
        }
    })
    .catch(error => {
        alert(error.message);
        console.error('Error:', error);
    });
}