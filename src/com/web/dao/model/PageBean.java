package com.web.dao.model;

import java.util.List;

public class PageBean {
	
	private List list;        			//列表
    
    private int totalCount;         	//總筆數
    private int totalPage;        		//總頁數
    private int currentPage;    		//當前頁
    private int pageSize;        		//每頁筆數
    
    private boolean isFirstPage;    	//是否為第一頁
    private boolean isLastPage;        	//是否為最后一頁
    private boolean hasPreviousPage;    //是否有前一頁
    private boolean hasNextPage;        //是否有下一頁
    
    
    
    /**
     * 計算總頁數
     * @param pageSize 每頁筆數
     * @param totalCount 總筆數
     * @return 
     */
    public static int countTotalPage(int pageSize, int allRow){
        int totalPage = allRow % pageSize == 0 ? allRow/pageSize : allRow/pageSize+1;
        return totalPage;
    }
    
    /**
     * 計算當前頁開始記錄
     * @param pageSize 每頁筆數
     * @param currentPage 當前頁
     * @return 
     */
    public static int countOffset(int pageSize, int currentPage){
        int offset = pageSize*(currentPage-1);
        return offset;
    }
    
    /**
     * 計算當前頁
     * @param page 可能為空(0),則返回1
     * @return 
     */
    public static int countCurrentPage(int page){
        int curPage = (page==0?1:page);
        return curPage;
    }
    
    
    /**
     * 初始化分頁參數
     */
    public void init(){
        this.isFirstPage = isFirstPage();
        this.isLastPage = isLastPage();
        this.hasPreviousPage = isHasPreviousPage();
        this.hasNextPage = isHasNextPage();
    }
	 
	/**
     * 是第一頁
     * @return
     */
    public boolean isFirstPage() {
        return currentPage == 1;    		
    }
    /**
     * 是最後一頁
     * @return
     */
    public boolean isLastPage() {
        return currentPage == totalPage;  
    }
    /**
     * 不是第一頁
     * @return
     */
    public boolean isHasPreviousPage() {
        return currentPage != 1;      
    }
    /**
     * 不是最後一頁
     * @return
     */
    public boolean isHasNextPage() {
        return currentPage != totalPage;   
    }
    
    

    public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
}
