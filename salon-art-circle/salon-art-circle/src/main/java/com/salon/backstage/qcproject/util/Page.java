package com.salon.backstage.qcproject.util;

/**
 * 作者：齐潮
 * 创建日期：2016年12月12日
 * 类说明：用于保存分页信息
 */
public class Page {

	/**
	 * 请求的页码，最小值为1
	 */
	private int page;
	
	/**
	 * 每页多少行数据，由页面传入
	 */
	private int eachRows;
	
	/**
	 * 总行数，通过查数据库得出
	 */
	private int sumRows;
	
	/**
	 * 自动计算得出，表示总页数
	 */
	private int sumPages;
	
	private Page(){}
	
	/**
	 * 获取一个page对象
	 * @param page
	 * @param eachRows
	 * @param sumRows
	 * @return
	 */
	public static Page init(int page,int eachRows,int sumRows){
		if(page<=0)
			page = 1;
		Page p = new Page();
		p.eachRows = eachRows;
		p.sumRows = sumRows;
		p.sumPages = sumRows%eachRows == 0 ? (sumRows/eachRows):((sumRows/eachRows) + 1);
		if(page<=0||p.sumRows<1){
			p.page = 1;
		}else if(page>p.sumPages){
			p.page = p.sumPages;
		}else{
			p.page = page;
		}
		return p;
	}
	
	/**
	 * 获得查询的起始下标
	 * @return
	 */
	public int getStartIndex(){
		int index = (page-1)*eachRows;
		return index;
	}

	public int getPage() {
		return page;
	}

	public int getEachRows() {
		return eachRows;
	}

	public int getSumRows() {
		return sumRows;
	}

	public int getSumPages() {
		return sumPages;
	}

}
