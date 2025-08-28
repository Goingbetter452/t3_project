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

    .then(response => {
        if (response.status === 401) {
            throw new Error('로그인이 필요합니다.');
        }
        if (!response.ok) {
            throw new Error('서버 오류가 발생했습니다.');
        }
        return response.text();
    })

    .then(data => {
        if (data.includes('success')) {
            showAlert('공지사항이 등록되었습니다.', 'success');
            document.getElementById('noticeTitle').value = '';
            document.getElementById('noticeContent').value = '';
            loadNoticesFromServer();
        } else {

            showAlert('공지사항 등록에 실패했습니다. (서버 응답: ' + data + ')', 'error');

        }
    })
    .catch(error => {
        console.error('Error:', error);

        showAlert(error.message || '공지사항 등록 중 오류가 발생했습니다.', 'error');

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

    if (!data || data.trim() === '') return;

    // 서버 포맷: 레코드 구분자 '%%%', 필드: noticeId|title|content|authorName|createDate|viewCount
    const records = data.split('%%%');
    records.forEach(rec => {
        const parts = rec.split('|');
        if (parts.length >= 6) {
            const n = {
                noticeId: parseInt(parts[0], 10),
                title: parts[1] || '',
                content: parts[2] || '',
                authorName: parts[3] || '',
                createDate: parts[4] || '',
                viewCount: parseInt(parts[5], 10) || 0
            };
            notices.push(n);
        }
    });

}

// 공지사항 표시
function displayNotices() {
    const noticeList = document.getElementById('noticeList');
    if (!noticeList) return;

    noticeList.innerHTML = '';

    notices.forEach(notice => {
        const noticeItem = document.createElement('div');
        noticeItem.className = 'notice-item';


        // 날짜 형식 변환 (빈 값 방어)
        let dateText = '날짜 없음';
        if (notice.createDate) {
            const d = new Date(notice.createDate);
            dateText = isNaN(d.getTime()) ? String(notice.createDate) : d.toLocaleString('ko-KR');
        }

        noticeItem.innerHTML =
		'<div class="notice-header">' +
		                '<span class="notice-title">' + (notice.title || '') + '</span>' +
		                // 날짜와 삭제 버튼을 함께 묶어주는 div를 추가했어요.
		                '<div class="notice-meta">' +
		                    '<span class="notice-date">' + dateText + '</span>' +
		                    '<button class="delete-notice-btn" onclick="deleteNotice(' + notice.noticeId + ')">삭제</button>' +
		                '</div>' +
		            '</div>' +
		            '<div class="notice-content">' + (notice.content || '') + '</div>' +
		            '<div class="notice-author">작성자: ' + (notice.authorName || '') + (typeof notice.viewCount === 'number' ? ' | 조회수: ' + notice.viewCount : '') + '</div>';
		        

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

