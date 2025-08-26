// groupware.js - 그룹웨어 전용 JavaScript (JSON 의존성 제거 버전)

// 전역 상태
let todos = [];
let messages = [];
let attendance = {};
let currentDate = new Date();

// --- 유틸: 로컬스토리지 (Plain Text 직렬화) ---
function saveToStorage(name, value) {
  try {
    if (name === 'todos') {
      // id|completed|text|createdAt per line
      const lines = (Array.isArray(value) ? value : []).map(t => [
        String(t.id || ''),
        t.completed ? '1' : '0',
        (t.text || '').replace(/\n/g, ' '),
        String(t.createdAt || '')
      ].join('|'));
      localStorage.setItem('todos', lines.join('\n'));
      return;
    }
    if (name === 'messages') {
      // id|type|sender|text|timestamp per line
      const lines = (Array.isArray(value) ? value : []).map(m => [
        String(m.id || ''),
        String(m.type || ''),
        String(m.sender || ''),
        (m.text || '').replace(/\n/g, ' '),
        String(m.timestamp || '')
      ].join('|'));
      localStorage.setItem('messages', lines.join('\n'));
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
    if (name === 'messages') {
      // id|type|sender|text|timestamp per line
      return raw.split('\n').filter(Boolean).map(line => {
        const [id, type, sender, text, timestamp] = line.split('|');
        return {
          id: Number(id),
          type: type || 'sent',
          sender: sender || '',
          text: text || '',
          timestamp: timestamp || ''
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
  // 공지: 서버에서 텍스트로 로드 (notice_admin.js 의 함수 사용)
  if (typeof loadNoticesFromServer === 'function') {
    loadNoticesFromServer();
  }

  // 나머지 로컬 데이터 로드
  todos = getFromStorage('todos') || [];
  messages = getFromStorage('messages') || [];
  attendance = getFromStorage('attendance') || {};

  displayTodos();
  displayMessages();
  displayAttendance();

  startClock();
  generateCalendar();
}

// ===== 공지 =====
// 이 파일에서는 addNotice/displayNotices 를 정의하지 않음 (notice_admin.js 사용)

// ===== 할일 =====
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
  const todo = {
    id: Date.now(),
    text: text,
    completed: false,
    createdAt: new Date().toISOString()
  };
  todos.unshift(todo);
  saveToStorage('todos', todos);
  input.value = '';
  displayTodos();
}

function toggleTodo(id) {
  const nid = Number(id);
  todos = (todos || []).map(t => {
    if (!t) return t;
    const match = !isNaN(nid) ? (t.id === nid) : (String(t.id) === String(id));
    return match ? { id: t.id, text: t.text, completed: !t.completed, createdAt: t.createdAt } : t;
  });
  saveToStorage('todos', todos);
  displayTodos();
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

    item.appendChild(cb);
    item.appendChild(span);
    item.appendChild(small);
    list.appendChild(item);
  });
}

// ===== 메신저 =====
function sendMessage() {
  const input = document.getElementById('messageInput');
  if (!input) return;
  const text = (input.value || '').trim();
  if (!text) return;

  const now = new Date();
  const msg = {
    id: Date.now(),
    text: text,
    sender: '나',
    timestamp: now.toLocaleTimeString('ko-KR'),
    type: 'sent'
  };
  messages.push(msg);
  saveToStorage('messages', messages);
  input.value = '';
  displayMessages();

  // 간단한 자동응답
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
  }, 800);
}

function displayMessages() {
  const box = document.getElementById('chatMessages');
  if (!box) return;
  box.innerHTML = '';
  (messages || []).slice(-20).forEach(m => {
    const div = document.createElement('div');
    div.className = 'message ' + (m.type || 'sent');
    div.innerHTML = '<div>' + (m.text || '') + '</div>' +
                    '<div class="message-info">' + (m.sender || '') + ' · ' + (m.timestamp || '') + '</div>';
    box.appendChild(div);
  });
  box.scrollTop = box.scrollHeight;
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
      if (text) {
        text.split(',').forEach(kv => {
          const [k, ...rest] = kv.split(':');
          attendance[k] = rest.join(':');
        });
      }
      // 근무시간 계산(퇴근 전이면 실시간 갱신)
      if (attendance.checkInTime && !attendance.checkOutTime) {
        updateWorkingTimeLive();
      }
      displayAttendance();
      saveToStorage('attendance', attendance);
    })
    .catch(() => {
      // 로컬 데이터로 대체
      attendance = getFromStorage('attendance') || {};
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
  if (co) co.textContent = attendance.checkOutTime ? formatKoreanDate(attendance.checkOutTime) : '미등록';
  if (st) st.textContent = attendance.checkInTime ? (attendance.checkOutTime ? '퇴근완료' : '근무중') : '대기중';
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
                if (year === today.getFullYear() && month === today.getMonth() && date === today.getDate()) {
                    cell.className = 'today';
                }
                
                // 해당 날짜의 일정이 있는지 확인
                const currentDate = new Date(year, month, date);
                const hasEvents = events.some(event => {
                    const eventDate = new Date(event.startDate);
                    return eventDate.getDate() === date && 
                           eventDate.getMonth() === month && 
                           eventDate.getFullYear() === year;
                });
                
                if (hasEvents) {
                    cell.classList.add('has-events');
                }
                
                // 일정 추가를 위한 클릭 이벤트
                cell.addEventListener('click', () => showEventForm(year, month, date));
                
                date++;
            }
            row.appendChild(cell);
        }
        body.appendChild(row);
        if (date > daysInMonth) break;
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
    // 캘린더의 각 셀에 일정 표시 업데이트
    const cells = document.querySelectorAll('#calendarBody td');
    cells.forEach(cell => {
        if (cell.textContent) {
            const date = parseInt(cell.textContent);
            const hasEvents = events.some(event => {
                const eventDate = new Date(event.startDate);
                return eventDate.getDate() === date &&
                       eventDate.getMonth() === currentDate.getMonth() &&
                       eventDate.getFullYear() === currentDate.getFullYear();
            });
            
            if (hasEvents) {
                cell.classList.add('has-events');
            } else {
                cell.classList.remove('has-events');
            }
        }
    });
}

function showEventForm(year, month, date) {
    // 이미 존재하는 모달 제거
    const existingModal = document.querySelector('.event-modal');
    if (existingModal) existingModal.remove();

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
    
    document.body.appendChild(modal);
    
    // ESC 키로 모달 닫기
    modal.addEventListener('keydown', e => {
        if (e.key === 'Escape') closeEventForm();
    });
}

function closeEventForm() {
    const modal = document.querySelector('.event-modal');
    if (modal) modal.remove();
}

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

function changeMonth(delta) {
    currentDate.setMonth(currentDate.getMonth() + delta);
    generateCalendar();
}