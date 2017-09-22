package com.zhph.manager.model;

import java.io.Serializable;

public class PageBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    private int totalPage;// 总页数
	
    private int perPageCount = 5;// 每页显示数量
	
    private int currentPage = 1;// 当前页
	
    private int totalNum;// 总数量
	

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getPerPageCount() {
		return perPageCount;
	}

	public void setPerPageCount(int perPageCount) {
		this.perPageCount = perPageCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
}
