// main.js - 공통 JavaScript 함수들

// 전역 변수
/*let currentDate = new Date();*/


// 현재 시간 업데이트
function updateCurrentTime() {
    const now = new Date();
    const timeString = now.toLocaleTimeString('ko-KR', {
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    });
    
    const currentTimeElement = document.getElementById('currentTime');
    if (currentTimeElement) {
        currentTimeElement.textContent = timeString;
    }
}

// 시간 형식 변환 함수
function formatTime(timestamp) {
    if (!timestamp) return '미등록';
    return new Date(timestamp).toLocaleTimeString('ko-KR');
}

// 날짜 형식 변환 함수
function formatDate(date) {
    if (!date) return '';
    return new Date(date).toLocaleDateString('ko-KR');
}

// 알림 표시 함수
function showAlert(message, type = 'info') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type}`;
    alertDiv.textContent = message;
    
    // 스타일 설정
    alertDiv.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 20px;
        border-radius: 5px;
        color: white;
        font-weight: bold;
        z-index: 1000;
        animation: slideIn 0.3s ease-out;
    `;
    
    // 타입별 색상 설정
    switch(type) {
        case 'success':
            alertDiv.style.backgroundColor = '#28a745';
            break;
        case 'error':
            alertDiv.style.backgroundColor = '#dc3545';
            break;
        case 'warning':
            alertDiv.style.backgroundColor = '#ffc107';
            alertDiv.style.color = '#212529';
            break;
        default:
            alertDiv.style.backgroundColor = '#17a2b8';
    }
    
    document.body.appendChild(alertDiv);
    
    // 3초 후 자동 제거
    setTimeout(() => {
        alertDiv.style.animation = 'slideOut 0.3s ease-in';
        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.parentNode.removeChild(alertDiv);
            }
        }, 300);
    }, 3000);
}

// CSS 애니메이션 추가
function addAlertStyles() {
    if (!document.getElementById('alert-styles')) {
        const style = document.createElement('style');
        style.id = 'alert-styles';
        style.textContent = `
            @keyframes slideIn {
                from { transform: translateX(100%); opacity: 0; }
                to { transform: translateX(0); opacity: 1; }
            }
            @keyframes slideOut {
                from { transform: translateX(0); opacity: 1; }
                to { transform: translateX(100%); opacity: 0; }
            }
        `;
        document.head.appendChild(style);
    }
}

// 페이지 로드 시 스타일 추가
document.addEventListener('DOMContentLoaded', function() {
    addAlertStyles();
});
