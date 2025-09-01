<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>상품 상세 정보</title>
<link rel="stylesheet" href="<%= request.getContextPath() %>/css/product.css">
   
</head>
<body>
    <div class="detail-container">
        <div class="detail-header">
            <h2>상품 상세 정보</h2>
        </div>
        <div class="detail-content">
            <div id="loading" class="loading">데이터를 불러오는 중...</div>
            <div id="error" class="error" style="display:none;">상품 정보를 불러올 수 없습니다.</div>
            <div id="productInfo" style="display:none;">
                <div class="detail-row">
                    <div class="detail-label">상품 ID</div>
                    <div class="detail-value" id="productId"></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">상품명</div>
                    <div class="detail-value" id="productName"></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">가격</div>
                    <div class="detail-value" id="productPrice"></div>
                </div>
                <div class="detail-row">
                    <div class="detail-label">재고</div>
                    <div class="detail-value">
                        <span id="productStock"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        // URL에서 pid 파라미터 가져오기
        const urlParams = new URLSearchParams(window.location.search);
        const pid = urlParams.get('pid');

        console.log('PID from URL:', pid); // 디버깅용

        if (pid && pid.trim() !== '') {
            // 서버에서 상품 정보 가져오기
            const fetchUrl = '<%= request.getContextPath() %>/product?action=details&pid=' + encodeURIComponent(pid);
            console.log('Fetch URL:', fetchUrl); // 디버깅용
            
            fetch(fetchUrl)
                .then(response => {
                    console.log('Response status:', response.status); // 디버깅용
                    if (!response.ok) {
                        throw new Error('HTTP error! status: ' + response.status);
                    }
                    return response.json();
                })
                .then(data => {
                    console.log('Response data:', data); // 디버깅용
                    
                    // 로딩 숨기기
                    document.getElementById('loading').style.display = 'none';
                    
                    if (data.error) {
                        document.getElementById('error').style.display = 'block';
                        document.getElementById('error').textContent = data.error;
                    } else {
                        // 상품 정보 표시
                        document.getElementById('productId').textContent = pid;
                        document.getElementById('productName').textContent = data.pname || '정보 없음';
                        document.getElementById('productPrice').textContent = '₩' + (data.price ? Number(data.price).toLocaleString() : '0');
                        
                        const stockSpan = document.getElementById('productStock');
                        const stockValue = data.stock || 0;
                        stockSpan.textContent = stockValue + '개';
                        
                        // 재고 상태에 따른 클래스 추가
                        if (stockValue < 10) {
                            stockSpan.classList.add('stock-status', 'stock-low');
                        } else if (stockValue < 50) {
                            stockSpan.classList.add('stock-status', 'stock-medium');
                        } else {
                            stockSpan.classList.add('stock-status', 'stock-high');
                        }
                        
                        document.getElementById('productInfo').style.display = 'block';
                    }
                })
                .catch(error => {
                    console.error('Fetch error:', error); // 디버깅용
                    document.getElementById('loading').style.display = 'none';
                    document.getElementById('error').style.display = 'block';
                    document.getElementById('error').textContent = '상품 정보를 불러오는데 실패했습니다: ' + error.message;
                });
        } else {
            document.getElementById('loading').style.display = 'none';
            document.getElementById('error').style.display = 'block';
            document.getElementById('error').textContent = '상품 ID가 제공되지 않았습니다.';
        }
    </script>
</body>
</html>