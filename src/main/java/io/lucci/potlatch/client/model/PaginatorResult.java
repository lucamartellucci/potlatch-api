package io.lucci.potlatch.client.model;

import java.io.Serializable;
import java.util.List;

public class PaginatorResult<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private List<T> result;
	private int totalPages;
	private int currentPage;
	private int pageSize;
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}
	public int getTotalPages() {
		return totalPages;
	}
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
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
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PaginatorResult [result=");
		builder.append(result);
		builder.append(", totalPages=");
		builder.append(totalPages);
		builder.append(", currentPage=");
		builder.append(currentPage);
		builder.append(", pageSize=");
		builder.append(pageSize);
		builder.append("]");
		return builder.toString();
	}
	
	
}
