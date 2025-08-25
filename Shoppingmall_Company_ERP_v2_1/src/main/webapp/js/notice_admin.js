// notice_admin.js - 공지사항 관련 JavaScript 함수들

// 공지사항 배열
let notices = [];

// 공지사항 등록
function addNotice() {
    const title = document.getElementById('noticeTitle').value.trim();
    const content = document.getElementById('noticeContent').value.trim();
    
    if (!title || !content) {
        showAlert('제목과 내용을 모두 입력해주세요.', 'warning');
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
    .then(response => response.text())
    .then(data => {
        if (data.includes('success')) {
            showAlert('공지사항이 등록되었습니다.', 'success');
            document.getElementById('noticeTitle').value = '';
            document.getElementById('noticeContent').value = '';
            loadNoticesFromServer();
        } else {
            showAlert('공지사항 등록에 실패했습니다.', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('공지사항 등록 중 오류가 발생했습니다.', 'error');
    });
}

// 서버에서 공지사항 로드
function loadNoticesFromServer() {
    fetch('GroupwareServlet?command=getNotices')
    .then(response => response.text())
    .then(data => {
        // 서버에서 받은 데이터를 파싱하여 notices 배열에 저장
        parseNoticesFromServer(data);
        displayNotices();
    })
    .catch(error => {
        console.error('Error:', error);
        // 오류 시 기존 방식으로 fallback
        loadNotices();
    });
}

// 서버 응답을 파싱하여 notices 배열에 저장
function parseNoticesFromServer(data) {
    notices = [];
    // 간단한 파싱 로직 (서버에서 보내는 형식에 따라 조정 필요)
    if (data && data.trim() !== '') {
        // 예시: "제목1|내용1|작성자1|2024-01-01,제목2|내용2|작성자2|2024-01-02"
        const noticeStrings = data.split(',');
        noticeStrings.forEach(noticeStr => {
            if (noticeStr.trim()) {
                const parts = noticeStr.split('|');
                if (parts.length >= 4) {
                    const notice = {
                        title: parts[0],
                        content: parts[1],
                        authorName: parts[2],
                        createDate: parts[3]
                    };
                    notices.push(notice);
                }
            }
        });
    }
}

// 공지사항 표시
function displayNotices() {
    const noticeList = document.getElementById('noticeList');
    if (!noticeList) return;
    
    noticeList.innerHTML = '';

    notices.forEach(notice => {
        const noticeItem = document.createElement('div');
        noticeItem.className = 'notice-item';
        
        // 날짜 형식 변환
        const createDate = notice.createDate ? new Date(notice.createDate).toLocaleString('ko-KR') : '날짜 없음';
        
        noticeItem.innerHTML = 
            '<div class="notice-header">' +
                '<span class="notice-title">' + notice.title + '</span>' +
                '<span class="notice-date">' + createDate + '</span>' +
            '</div>' +
            '<div class="notice-content">' + notice.content + '</div>' +
            '<div class="notice-author">작성자: ' + notice.authorName + '</div>';
        noticeList.appendChild(noticeItem);
    });
}

// 기존 localStorage 기반 공지사항 로드 (fallback)
function loadNotices() {
    const localNotices = getLocalNotices();
    const noticeList = document.getElementById('noticeList');
    if (!noticeList) return;
    
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

// localStorage에서 공지사항 가져오기 
function getLocalNotices() {
    const stored = localStorage.getItem('notices');
    if (!stored) return [];
    
    try {
        // 간단한 파싱 로직
        const notices = [];
        const lines = stored.split('\n');
        lines.forEach(line => {
            if (line.trim()) {
                const parts = line.split('|');
                if (parts.length >= 3) {
                    notices.push({
                        title: parts[0],
                        content: parts[1],
                        date: parts[2],
                        author: parts[3] || '작성자'
                    });
                }
            }
        });
        return notices;
    } catch (e) {
        return [];
    }
}

// 공지사항 삭제 (관리자용)
function deleteNotice(noticeId) {
    if (!confirm('정말로 이 공지사항을 삭제하시겠습니까?')) {
        return;
    }

    fetch('GroupwareServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'command=deleteNotice&noticeId=' + noticeId
    })
    .then(response => response.text())
    .then(data => {
        if (data.includes('success')) {
            showAlert('공지사항이 삭제되었습니다.', 'success');
            loadNoticesFromServer();
        } else {
            showAlert('공지사항 삭제에 실패했습니다.', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('공지사항 삭제 중 오류가 발생했습니다.', 'error');
    });
}

// 공지사항 수정 (관리자용)
function editNotice(noticeId) {
    const notice = notices.find(n => n.noticeId === noticeId);
    if (!notice) return;

    // 수정 폼 표시
    const noticeForm = document.getElementById('noticeForm');
    if (noticeForm) {
        document.getElementById('editNoticeId').value = noticeId;
        document.getElementById('editNoticeTitle').value = notice.title;
        document.getElementById('editNoticeContent').value = notice.content;
        noticeForm.style.display = 'block';
    }
}

// 공지사항 수정 저장
function updateNotice() {
    const noticeId = document.getElementById('editNoticeId').value;
    const title = document.getElementById('editNoticeTitle').value.trim();
    const content = document.getElementById('editNoticeContent').value.trim();
    
    if (!title || !content) {
        showAlert('제목과 내용을 모두 입력해주세요.', 'warning');
        return;
    }

    fetch('GroupwareServlet', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: 'command=updateNotice&noticeId=' + noticeId + '&title=' + encodeURIComponent(title) + '&content=' + encodeURIComponent(content)
    })
    .then(response => response.text())
    .then(data => {
        if (data.includes('success')) {
            showAlert('공지사항이 수정되었습니다.', 'success');
            document.getElementById('noticeForm').style.display = 'none';
            loadNoticesFromServer();
        } else {
            showAlert('공지사항 수정에 실패했습니다.', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('공지사항 수정 중 오류가 발생했습니다.', 'error');
    });
}
