<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>상품 상세 정보</title>
    <style>
        body { font-family: 'Noto Sans KR', sans-serif; background-color: #f8f9fa; margin:0; padding:20px; }
        .detail-container { background:#fff; border-radius:8px; padding:20px; max-width:500px; margin:0 auto; box-shadow:0 2px 4px rgba(0,0,0,.1); }
        .detail-header { border-bottom:2px solid #6c5ce7; padding-bottom:10px; margin-bottom:20px; }
        .detail-header h2 { margin:0; color:#333; font-size:24px; }
        .detail-row { display:flex; padding:10px 0; border-bottom:1px solid #eee; }
        .detail-label { width:100px; font-weight:bold; color:#666; }
        .detail-value { flex:1; color:#333; }
        .stock-status { padding:4px 8px; border-radius:4px; font-weight:bold; }
        .stock-low { background-color:#ffe0e0; color:#d63031; }
        .stock-medium { background-color:#fff3bf; color:#e17055; }
        .stock-high { background-color:#d4edda; color:#2ecc71; }
    </style>
</head>
<body>
    <div class="detail-container">
        <div class="detail-header">
            <h2>상품 상세 정보</h2>
        </div>
        <div class="detail-content">
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
                <div class="detail-value"><span id="productStock"></span></div>
            </div>
        </div>
    </div>

    <script>
    // EL 없이 스크립틀릿으로 값 삽입
    var ctx = '<%= request.getContextPath() %>';
    var pidFromParam = '<%= request.getParameter("pid") == null ? "" : request.getParameter("pid") %>';

    // pid 유효성(숫자만) 체크
    if (!pidFromParam || !/^\d+$/.test(pidFromParam)) {
        alert('유효한 상품 ID가 없습니다.');
        history.back();
    } else {
        var pid = pidFromParam;

        // Ajax로 상세 정보 조회 (서블릿에서 JSON 응답하도록 구성되어 있어야 함)
        fetch(ctx + '/product?action=details&pid=' + encodeURIComponent(pid))
          .then(function(r){
              if (!r.ok) throw new Error('HTTP ' + r.status);
              return r.json();
          })
          .then(function(data){
              var price = Number(data.price || 0);
              var stock = Number(data.stock || 0);

              document.getElementById('productId').textContent    = data.pid || pid;
              document.getElementById('productName').textContent  = data.pname || '';
              document.getElementById('productPrice').textContent = '₩' + price.toLocaleString('ko-KR');

              var ss = document.getElementById('productStock');
              ss.textContent = String(stock) + '개';
              ss.classList.add('stock-status', stock<10 ? 'stock-low' : (stock<50 ? 'stock-medium' : 'stock-high'));
          })
          .catch(function(e){
              console.error(e);
              alert('상품 정보를 불러오지 못했습니다.');
          });
    }
    </script>
</body>
</html>
