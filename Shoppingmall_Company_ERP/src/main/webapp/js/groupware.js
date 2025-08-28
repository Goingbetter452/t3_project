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
    displayNotices(); // 초기 공지 표시 추가
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

/* ========= 날짜/시간 유틸 ========= */
// ISO 문자열을 한국식으로 보기 좋게
function formatKoreanDate(isoString) {
    if (!isoString) return '';
    const d = new Date(isoString);
    if (isNaN(d.getTime())) return ''; // 파싱 실패 방어
    return d.toLocaleString('ko-KR');
}

/* ========= 공지 ========= */

// 공지사항 추가
function addNotice() {
  // 1) DOM 존재 확인
  var titleEl = document.getElementById('noticeTitle');
  var contentEl = document.getElementById('noticeContent');
  if (!titleEl || !contentEl) {
    console.error('입력 필드를 찾을 수 없습니다. #noticeTitle / #noticeContent 확인');
    alert('입력 필드가 페이지에 없어요. ID를 확인해주세요.');
    return;
  }

  // 2) 값 읽기 + 검증
  var title = (titleEl.value || '').trim();
  var content = (contentEl.value || '').trim();
  if (!title || !content) {
    alert('제목과 내용을 입력해주세요.');
    return;
  }

  // 3) 저장 데이터는 ISO로 (파싱 안전)
  var nowIso = new Date().toISOString();
  var notices = getFromStorage('notices') || [];
  notices.unshift({
    id: Date.now(),
    title: title,
    content: content,
    createdAt: nowIso
  });
  saveToStorage('notices', notices);

  // 4) 입력 초기화 + 목록 갱신
  titleEl.value = '';
  contentEl.value = '';

  if (typeof displayNotices === 'function') {
    displayNotices();
  } else {
    console.warn('displayNotices()가 없습니다. 목록 영역 렌더 함수를 확인하세요.');
  }

  alert('공지사항이 등록되었습니다.');
}


// 공지사항 표시 (XSS 방지: textContent 사용)
function displayNotices() {
    const noticeList = document.getElementById('noticeList');
    if (!noticeList) return;
    
    const notices = getFromStorage('notices') || [];
    noticeList.innerHTML = '';
    
    notices.forEach(notice => {
        const item = document.createElement('div');
        item.className = 'notice-item';
        
        const header = document.createElement('div');
        header.className = 'notice-header';
        
        const strong = document.createElement('strong');
        strong.textContent = notice.title;
        
        const small = document.createElement('small');
        small.textContent = formatKoreanDate(notice.createdAt);
        
        header.appendChild(strong);
        header.appendChild(small);
        
        const body = document.createElement('div');
        body.textContent = notice.content;
        
        item.appendChild(header);
        item.appendChild(body);
        noticeList.appendChild(item);
    });
}

/* ========= 할일 ========= */

// 할일 추가
// 할일 추가 (안전판)
function addTodo() {
  // 1) 입력 요소 확인
  var input = document.getElementById('todoInput');
  if (!input) {
    console.error('입력 요소(#todoInput)를 찾을 수 없습니다.');
    alert('입력창을 찾을 수 없어요. id="todoInput" 확인해주세요.');
    return;
  }

  // 2) 값 읽기 + 검증
  var text = (input.value || '').trim();
  if (!text) {
    alert('할일을 입력해주세요.');
    return;
  }

  // 3) todos 배열 보장
  if (!Array.isArray(todos)) {
    todos = [];
  }

  // 4) 할일 객체 생성 (시간은 ISO로 저장)
  var todo = {
    id: Date.now(),
    text: text,
    completed: false,
    createdAt: new Date().toISOString()
  };

  // 5) 추가 + 저장 (스토리지 실패 방어)
  todos.unshift(todo);
  try {
    saveToStorage('todos', todos);
  } catch (e) {
    console.error('할일 저장 실패:', e);
    alert('저장 중 문제가 발생했어요. 저장공간을 확인해주세요.');
  }

  // 6) 입력 초기화 + 렌더
  input.value = '';
  if (typeof displayTodos === 'function') {
    displayTodos();
  } else {
    console.warn('displayTodos() 함수가 없습니다.');
  }
}


// 할일 완료/미완료 토글 (안전판)
function toggleTodo(id) {
  // 0) todos 보장
  if (!Array.isArray(todos)) {
    todos = [];
  }

  // 1) id 타입 정규화 (숫자로 먼저 시도)
  var targetId = (typeof id === 'string') ? Number(id) : id;
  var hasNumericId = !isNaN(targetId);

  // 2) 새 배열 생성 (불변 패턴 유지)
  var next = todos.map(function (todo) {
    if (!todo) return todo;

    // 숫자 비교 우선, 안 되면 문자열 비교도 백업
    var isMatch = hasNumericId
      ? (todo.id === targetId)
      : (String(todo.id) === String(id));

    if (isMatch) {
      // 스프레드 대신 명시적으로 복사
      return {
        id: todo.id,
        text: todo.text,
        completed: !todo.completed,
        createdAt: todo.createdAt
      };
    }
    return todo;
  });

  todos = next;

  try {
    saveToStorage('todos', todos);
  } catch (e) {
    console.error('저장 실패:', e);
    alert('저장 중 문제가 발생했어요.');
  }

  if (typeof displayTodos === 'function') {
    displayTodos();
  } else {
    console.warn('displayTodos()가 없습니다.');
  }
}


// 할일 목록 표시 (XSS 방지)
function displayTodos() {
    const todoList = document.getElementById('todoList');
    if (!todoList) return;
    
    todoList.innerHTML = '';
    
    todos.forEach(todo => {
        const div = document.createElement('div');
        div.className = 'todo-item ' + (todo.completed ? 'completed' : '');
        
        const checkbox = document.createElement('input');
        checkbox.type = 'checkbox';
        checkbox.checked = !!todo.completed;
        checkbox.addEventListener('change', () => toggleTodo(todo.id));
        
        const span = document.createElement('span');
        span.textContent = todo.text;
        
        const small = document.createElement('small');
        small.textContent = formatKoreanDate(todo.createdAt);
        
        div.appendChild(checkbox);
        div.appendChild(span);
        div.appendChild(small);
        todoList.appendChild(div);
    });
}

/* ========= 근태 ========= */

// 출근 처리
function checkIn() {
    const now = new Date();
    attendance = attendance || {};
    attendance.checkInIso = now.toISOString();
    attendance.checkOutIso = null;
    attendance.workingTime = '00:00';
    attendance.status = '근무중';
    
    saveToStorage('attendance', attendance);
    displayAttendance();
    alert('출근 처리되었습니다.');
}

// 퇴근 처리 (완성본)
function checkOut() {
  const now = new Date();
  attendance = attendance || {};

  // 체크인 없이 퇴근 방지
  if (!attendance.checkInIso) {
    alert('출근(체크인) 기록이 없습니다.');
    return;
  }

  attendance.checkOutIso = now.toISOString();
  attendance.status = '퇴근완료';

  // 근무시간 계산 (ISO 기준)
  const checkIn = new Date(attendance.checkInIso);
  const checkOut = new Date(attendance.checkOutIso);
  const diff = checkOut - checkIn;

  if (!isNaN(diff) && diff >= 0) {
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    attendance.workingTime =
      String(hours).padStart(2, '0') + ':' + String(minutes).padStart(2, '0');
  } else {
    // 시간이 거꾸로거나 파싱 실패 시 기본값
    attendance.workingTime = '00:00';
  }

  saveToStorage('attendance', attendance);
  displayAttendance();
  alert('퇴근 처리되었습니다.');
}


