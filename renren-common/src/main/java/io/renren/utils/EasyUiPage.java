package io.renren.utils;

import java.util.List;

public class EasyUiPage {

	private int total;
	private List<?> rows;
	
	
	public void setRows(List<?> rows) {
		this.rows = rows;
	}
	
	public void setTotal(int total) {
		this.total = total;
	}
	
	public List<?> getRows() {
		return rows;
	}
	
	public int getTotal() {
		return total;
	}
}
