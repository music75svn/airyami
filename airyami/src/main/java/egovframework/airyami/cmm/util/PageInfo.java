package egovframework.airyami.cmm.util;

public class PageInfo {
    /** 처음 선택 시작 페이지 */
    public static final int CURR_PAGE = 1; 
    
    /** 한 페이지에 표시 할 열수 */
    public static final int ONE_PAGE_ROWS = 15;
    
    /** 한 페이지에 표시 할 페이지 수 */
    public static final int TOTAL_PAGES = 10;
    
    private int perUnit;
    private int perPage;
    private int startUnit;
    private int endUnit;
    private int totalPage;
    private int startPage;
    private int endPage;
    private int beforePage;
    private int nextPage;
    private int current;
    private int total;
    private int startPerPage;
    private int endPerPage;
    private Long groupNum;
    private String dbType;
    

	public PageInfo() {
        setPerUnit(0);
        setPerPage(0);
        setDbType("oracle");
    }

    public PageInfo(int current, int total) {
        this(0, 0, current, total);
    }
    
    public PageInfo(int current, int total, String dbType) {
    	this(0, 0, current, total);
    	setDbType(dbType);
    	//setDbType("mysql");
    }
    
    public PageInfo(int perUnit, int  perPage, int current, int total) {
        setPerUnit(perUnit);
        setPerPage(perPage);
        setCurrent(current);
        setTotal(total);
        setDbType("oracle");
        calculate();
    }
    
    public PageInfo(int perUnit, int  perPage, int current, int total, Long groupNum) {
        this(perUnit, perPage, current, total);
        setGroupNum(groupNum);
    }
    
    public void calculate() {
        totalPage = (int) Math.ceil((double) total / (double) perUnit);
        
        int current = getCurrent();
        
        startUnit = ((current - 1) * perUnit) + 1;
        endUnit   = current * perUnit;
        
        if (total <= endUnit) endUnit = total;
        
        startPage = (int) Math.ceil((double) current / (double) perPage) * perPage - (perPage - 1);
        endPage   = (int) Math.ceil((double) current / (double) perPage) * perPage;
        
        startPerPage = startUnit;
        endPerPage   = totalPage;
        
        beforePage = current - 1;
        nextPage   = current + 1;
    }
    
    public int getPerUnit() {
        return perUnit;
    }
    
    public void setPerUnit(int perUnit) {
        if (perUnit <= 0) {
            perUnit = ONE_PAGE_ROWS;
        }
        this.perUnit = perUnit;
    }
    
    public int getPerPage() {
        return perPage;
    }
    
    public void setPerPage(int perPage) {
        if (perPage <= 0) {
            perPage = TOTAL_PAGES;
        }
        this.perPage = perPage;
    }

    public int getCurrent() {
        int current = this.current;
        if (current < 1) {
            current = 1;
        }
        return current;
    }
    
    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return total;
    }
    
    public void setTotal(int total) {
        this.total = total;
    }

    public Long getGroupNum() {
        return groupNum;
    }
    
    public void setGroupNum(Long groupNum) {
        this.groupNum = groupNum;
    }
    
    public int getTotalPage() {
        return totalPage;
    }
    
    public int getStartUnit() {
    	// mysql은 0부터 시작이다.때문에 1을 빼야한다.
    	if("mysql".equals(this.dbType))
    		return startUnit -1;
    	
        return startUnit;
    }

    public int getEndUnit() {
    	// limit 에서 사용하기 위해서.
    	if("mysql".equals(this.dbType))
    		return perUnit;
    	
        return endUnit;
    }

    public int getStartPage() {
        return startPage;
    }
    
    public int getEndPage() {       
        if(this.endPage > this.totalPage)
            return totalPage;
        else 
            return endPage;
    }
    
    public int getStartPerPage() {
        return startPerPage;
    }
    
    public int getEndPerPage() {
        return endPerPage;
    }

    public int getBeforePage() { 
        if (this.beforePage < 1)
            return this.beforePage = 1;
        else
            return this.beforePage; 
    }
    
    public int getNextPage() {
        if (this.nextPage <=  this.totalPage)  //선택페이지가 전체페이지수보다 작을때
            return this.nextPage; 
        else 
            return this.nextPage-1;
    }

    public String getDbType() {
    	return dbType;
    }
    
    public void setDbType(String dbType) {
    	this.dbType = dbType;
    }
    
    /**
     * 페이징 네비게이션 HTML문으로 변환한다.
     * @param clickHandler  클릭시 호출할 JavaScript 함수명
     * @return HTML문
     */
     public String toHtml(String clickHandler) {
         // 아이콘 디렉토리 URL
         String iconDir = "/images";
         
         int current   = getCurrent();
         int startPage = getStartPage();
         int endPage   = getEndPage();
         int totalPage = getTotalPage();
         
         StringBuilder html = new StringBuilder();
         
         if (1 <= startPage && startPage <= current && current <= endPage && endPage <= totalPage) {
             html.append("<div class=\"paginate\">").append("\n");
             
             // 1번 페이지이면 '처음', '이전' 버튼을 감춘다.
             String visibility = "";
             if (current <= 1) {
                 visibility = "visibility:hidden;";
             }
             
             // '처음' 버튼 추가
             html.append("<a href=\"#\" class=\"pre_end\" style=\""+visibility+"\" onclick=\""+clickHandler+"('"+1+"'); return false;\">");
             html.append("<img src=\""+iconDir+"/btn_ppre.gif\" alt=\"처음페이지\"/>");
             html.append("</a>").append("\n");
             
             // '이전' 버튼 추가
             html.append("<a href=\"#\" class=\"pre\"     style=\""+visibility+"\" onclick=\""+clickHandler+"('"+getBeforePage()+"'); return false;\">");
             html.append("<img src=\""+iconDir+"/btn_pre.gif\" alt=\"이전페이지\"/>");
             html.append("</a>").append("\n");
             
             // 표시할 첫페이지부터 끝페이지까지 페이지번호를 추가한다.
             for (int i = startPage; i <= endPage; i++) {
                 if (i == current) {
                     html.append("<a href=\"#\" class=\"active\">"+i+"</a>").append("\n");
                 } else {
                     html.append("<a href=\"#\" onclick=\""+clickHandler+"('"+i+"'); return false;\">"+i+"</a>").append("\n");
                 }
             }
             
             // 마지막 페이지이면 '다음', '끝으로' 버튼을 감춘다.
             visibility = "";
             if (current >= totalPage) {
                 visibility = "visibility:hidden;";
             }
             
             // '다음' 버튼 추가
             html.append("<a href=\"#\" class=\"next\"     style=\""+visibility+"\" onclick=\""+clickHandler+"('"+getNextPage()+"'); return false;\">");
             html.append("<img src=\""+iconDir+"/btn_next.gif\" alt=\"다음페이지\"/>");
             html.append("</a>").append("\n");
             
             // '끝으로' 버튼 추가
             html.append("<a href=\"#\" class=\"next_end\" style=\""+visibility+"\" onclick=\""+clickHandler+"('"+totalPage+"'); return false;\">");
             html.append("<img src=\""+iconDir+"/btn_nnext.gif\" alt=\"마지막페이지\"/>");
             html.append("</a>").append("\n");
             
             html.append("</div>").append("\n");
         }
         
         return html.toString();
     }
}
