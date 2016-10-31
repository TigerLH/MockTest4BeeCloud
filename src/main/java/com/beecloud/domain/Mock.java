/**
 * 
 */
package com.beecloud.domain;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016-10-24 下午1:48:17
 * @version v1.0
 */
public class Mock {
	/**
	 * id
	 */
	private String id;
	/**
	 * mock标题
	 */
	private String title;
	/**
	 * mock的Url
	 */
	private String url;
	/**
	 * Mock的方法
	 */
	private String method;
	
	/**
	 * 状态码
	 */
	private String statuscode;
	/**
	 * Mock模拟的返回值
	 */
	private String response;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}
	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}
	/**
	 * @return the statuscode
	 */
	public String getStatuscode() {
		return statuscode;
	}
	/**
	 * @param statuscode the statuscode to set
	 */
	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}
	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}
	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Mock [id=" + id + ", title=" + title + ", url=" + url
				+ ", method=" + method + ", statuscode=" + statuscode
				+ ", response=" + response + "]";
	}
	
}
