package com.salon.backstage.common.util;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class Paging<T> {
	private final List<T> list;
	private final int start;
	private final int pageSize;
	private final int count;
	private final int pageCount;
	private final int currentPage;
	private final int startOfPreviousPage;
	private final int startOfNextPage;
	private final int startOfLastPage;
	private final boolean hasPrevious;
	private final boolean hasNext;
	private final int previousPage;
	private final int nextPage;

	public Paging(List<T> _list, int _start, int _pageSize, int _count) {
		this.list = _list;
		this.start = _start;
		this.pageSize = _pageSize > 1 ? _pageSize : 1;
		this.count = _count;
		this.pageCount = (count % pageSize > 0) ? (count / pageSize + 1) : (count / pageSize);
		this.currentPage = (start / pageSize + 1) > pageCount ? pageCount : (start / pageSize + 1);
		this.startOfPreviousPage = (start - pageSize) > 0 ? (start - pageSize) : 0;
		this.startOfNextPage = (start + pageSize) > count ? start : (start + pageSize);
		this.startOfLastPage = (pageCount - 1) * pageSize;
		this.hasPrevious = currentPage > 1;
		this.hasNext = pageCount > currentPage;
		this.previousPage = (currentPage - 1) > 0 ? (currentPage - 1) : currentPage;
		this.nextPage = (currentPage + 1) > pageCount ? pageCount : currentPage + 1;
	}

	public static void main(String[] args) {
		Paging<?> paging = new Paging<>(null, 0, 10, 100);
		System.out.println(JSONObject.toJSONString(paging));
	}

	public List<T> getList() {
		return list;
	}

	public int getStart() {
		return start;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getCount() {
		return count;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getStartOfPreviousPage() {
		return startOfPreviousPage;
	}

	public int getStartOfNextPage() {
		return startOfNextPage;
	}

	public int getStartOfLastPage() {
		return startOfLastPage;
	}

	public boolean isHasPrevious() {
		return hasPrevious;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public int getPreviousPage() {
		return previousPage;
	}

	public int getNextPage() {
		return nextPage;
	}
}
