function gfn_addPaging(pageMap, clickHandler, trgObj, style ) {
    var iconDir = "<%=UrlUtil.getCommonImagesRoot()%>";
    
    var current = Number(pageMap.current);
    var startPage = Number(pageMap.startPage);
    var endPage = Number(pageMap.endPage);
    var totalPage = Number(pageMap.totalPage);
    
    var styles = "";

    if(!gfn_isNull(style))
    	styles = " style='"+style+"' ";
    
    var pagingHtml = "";
    
    if (1 <= startPage && startPage <= current && current <= endPage && endPage <= totalPage) {
         pagingHtml += "<div class=\"paging\"" + styles +">" + "\n";
    
         // 1번 페이지이면 '처음', '이전' 버튼을 감춘다.
         var visibility = "";
         if (current <= 1) {
             visibility = "visibility:hidden;";
         }
         
         // '처음' 버튼 추가
         pagingHtml += "<a href=\""+pageMap.responseURL+"&pageNo=1\" class=\"btn_first\" style=\""+visibility+"\" onclick=\"javascript:"+clickHandler+"('"+1+"');return false;\">";
         //pagingHtml += "<img src=\"/dWise/images/admin/paginate_first_FF.gif\" alt=\"처음\" border=\"0\"/>";
         pagingHtml += "</a>" + "\n";
         
         // '이전' 버튼 추가
         pagingHtml += "<a href=\""+pageMap.responseURL+"&pageNo="+getBeforePage(current)+"\" class=\"btn_prev\"     style=\""+visibility+"\" onclick=\"javascript:"+clickHandler+"('"+getBeforePage(current)+"');return false;\">";
         //pagingHtml += "<img src=\"/dWise/images/admin/paginate_prev_FF.gif\" alt=\"이전페이지\"/>";
         pagingHtml += "</a>" + "\n";
         
         // 표시할 첫페이지부터 끝페이지까지 페이지번호를 추가한다.
         for (var i = startPage; i <= endPage; i++) {
             if (i == current) {
                     pagingHtml += "<strong>"+i+"</strong>" + "\n";
             } else {
                     pagingHtml += "<a href=\""+pageMap.responseURL+"&pageNo="+i+"\" onclick=\"javascript:"+clickHandler+"('"+i+"');return false;\">"+i+"</a>" + "\n";
             }
         }
         
         // 마지막 페이지이면 '다음', '끝으로' 버튼을 감춘다.
         visibility = "";
         if (current >= totalPage) {
             visibility = "visibility:hidden;";
         }
         
         // '다음' 버튼 추가
         pagingHtml += "<a href=\""+pageMap.responseURL+"&pageNo="+getNextPage(current, totalPage)+"\" class=\"btn_next\"     style=\""+visibility+"\" onclick=\"javascript:"+clickHandler+"('"+getNextPage(current, totalPage)+"');return false;\">";
         //pagingHtml += "<img src=\"/dWise/images/admin/paginate_next_FF.gif\" alt=\"다음페이지\"/>";
         pagingHtml += "</a>" + "\n";
         
         // '끝으로' 버튼 추가
         pagingHtml += "<a href=\""+pageMap.responseURL+"&pageNo="+totalPage+"\" class=\"btn_last\" style=\""+visibility+"\" onclick=\"javascript:"+clickHandler+"('"+totalPage+"');return false;\">";
         //pagingHtml += "<img src=\"/dWise/images/admin/paginate_last_FF.gif\" alt=\"마지막페이지\"/>";
         pagingHtml += "</a>" + "\n";
         
         pagingHtml += "</div>" + "\n";
     }
    if( !trgObj ){
    	$("#pagingNav").html(pagingHtml);
    } else {
    	$("#" + trgObj ).html(pagingHtml);
    }
     
}

function getBeforePage(current) { 
    if (current -1 < 1)
        return 1;
    else
        return current - 1; 
}

function getNextPage(current, totalPage) {
    if (current + 1 <=  totalPage)  //선택페이지가 전체페이지수보다 작을때
        return current + 1; 
    else 
        return current;
}