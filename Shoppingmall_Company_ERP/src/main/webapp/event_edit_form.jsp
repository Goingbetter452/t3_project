<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, java.text.*"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>일정 수정 - B2B Company ERP</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/calendar.css">
</head>
<body>
    <!-- 헤더 포함 -->
    <%@ include file="common-jsp/header.jsp"%>

    <div class="page-header">
        <h1>📅 일정 수정</h1>
        <p>일정 정보를 수정하세요</p>
    </div>

    <div class="event-modal" style="position: relative; background: none; display: block;">
        <div class="event-form">
            <h3 id="eventFormTitle">일정 수정</h3>
            
            <form id="eventEditForm" method="post" action="GroupwareServlet">
                <input type="hidden" name="command" value="updateEvent">
                <input type="hidden" id="eventId" name="eventId" value="">
                
                <div class="form-group">
                    <label for="eventTitle">일정 제목:</label>
                    <input type="text" id="eventTitle" name="title" placeholder="일정 제목" required>
                </div>
                
                <div class="form-group">
                    <label for="eventDescription">일정 설명:</label>
                    <textarea id="eventDescription" name="description" placeholder="일정 설명" rows="4"></textarea>
                </div>
                
                <div class="form-group">
                    <label for="eventType">일정 유형:</label>
                    <select id="eventType" name="eventType">
                        <option value="PERSONAL">개인 일정</option>
                        <option value="MEETING">회의</option>
                        <option value="HOLIDAY">휴가</option>
                        <option value="COMPANY">회사 일정</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="eventStartTime">시작 시간:</label>
                    <input type="time" id="eventStartTime" name="startTime" value="09:00">
                </div>
                
                <div class="form-group">
                    <label for="eventEndTime">종료 시간:</label>
                    <input type="time" id="eventEndTime" name="endTime" value="18:00">
                </div>
                
                <div class="form-group checkbox-group">
                    <label>
                        <input type="checkbox" id="eventAllDay" name="isAllDay" value="Y">
                        종일
                    </label>
                </div>
                
                <div class="form-group">
                    <label for="eventLocation">장소:</label>
                    <input type="text" id="eventLocation" name="location" placeholder="장소">
                </div>
                
                <div class="modal-buttons">
                    <button type="submit" class="btn-primary">수정</button>
                    <button type="button" class="btn-secondary" onclick="history.back()">취소</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        // URL 파라미터에서 일정 정보 가져오기
        const urlParams = new URLSearchParams(window.location.search);
        const eventId = urlParams.get('eventId');
        const year = urlParams.get('year');
        const month = urlParams.get('month');
        const date = urlParams.get('date');
        
        // 폼 제목 업데이트
        if (year && month && date) {
            document.getElementById('eventFormTitle').textContent = 
                `${year}년 ${month}월 ${date}일 일정 수정`;
        }
        
        // 일정 ID 설정
        if (eventId) {
            document.getElementById('eventId').value = eventId;
            
            // 서버에서 일정 정보 로드
            loadEventData(eventId);
        }
        
        // 일정 데이터 로드 함수
        function loadEventData(eventId) {
            fetch(`GroupwareServlet?command=getEvent&eventId=${eventId}`)
                .then(response => response.text())
                .then(data => {
                    if (data && data !== 'error') {
                        const eventData = parseEventData(data);
                        populateForm(eventData);
                    }
                })
                .catch(error => {
                    console.error('일정 로드 실패:', error);
                    alert('일정 정보를 불러올 수 없습니다.');
                });
        }
        
        // 일정 데이터 파싱
        function parseEventData(data) {
            const [id, title, desc, type, start, end, allDay, loc] = data.split('|');
            return {
                eventId: parseInt(id),
                title: title || '',
                description: desc || '',
                eventType: type || 'PERSONAL',
                startDate: start || '',
                endDate: end || '',
                isAllDay: allDay === 'Y',
                location: loc || ''
            };
        }
        
        // 폼에 데이터 채우기
        function populateForm(eventData) {
            document.getElementById('eventTitle').value = eventData.title;
            document.getElementById('eventDescription').value = eventData.description;
            document.getElementById('eventType').value = eventData.eventType;
            document.getElementById('eventLocation').value = eventData.location;
            document.getElementById('eventAllDay').checked = eventData.isAllDay;
            
            // 시간 설정
            if (eventData.startDate) {
                const startDate = new Date(eventData.startDate);
                const startTime = startDate.toTimeString().slice(0, 5);
                document.getElementById('eventStartTime').value = startTime;
            }
            
            if (eventData.endDate) {
                const endDate = new Date(eventData.endDate);
                const endTime = endDate.toTimeString().slice(0, 5);
                document.getElementById('eventEndTime').value = endTime;
            }
        }
        
        // 폼 제출 처리
        document.getElementById('eventEditForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            const formData = new FormData(this);
            const isAllDay = document.getElementById('eventAllDay').checked;
            
            // 날짜 및 시간 처리
            const startTime = document.getElementById('eventStartTime').value;
            const endTime = document.getElementById('eventEndTime').value;
            
            const startDate = new Date(year, month - 1, date);
            const endDate = new Date(year, month - 1, date);
            
            if (!isAllDay) {
                const [startHour, startMinute] = startTime.split(':');
                const [endHour, endMinute] = endTime.split(':');
                startDate.setHours(parseInt(startHour), parseInt(startMinute));
                endDate.setHours(parseInt(endHour), parseInt(endMinute));
            } else {
                endDate.setHours(23, 59, 59);
            }
            
            formData.set('startDate', startDate.toISOString().replace('T', ' ').split('.')[0]);
            formData.set('endDate', endDate.toISOString().replace('T', ' ').split('.')[0]);
            formData.set('isAllDay', isAllDay ? 'Y' : 'N');
            
            // 서버에 전송
            fetch('GroupwareServlet', {
                method: 'POST',
                body: formData
            })
            .then(response => response.text())
            .then(result => {
                if (result === 'success') {
                    alert('일정이 수정되었습니다.');
                    window.location.href = 'groupware.jsp';
                } else {
                    alert('일정 수정에 실패했습니다.');
                }
            })
            .catch(error => {
                alert('오류가 발생했습니다: ' + error.message);
                console.error('Error:', error);
            });
        });
        
        // 종일 체크박스 이벤트
        document.getElementById('eventAllDay').addEventListener('change', function() {
            const timeInputs = document.querySelectorAll('input[type="time"]');
            timeInputs.forEach(input => {
                input.disabled = this.checked;
            });
        });
    </script>
</body>
</html>