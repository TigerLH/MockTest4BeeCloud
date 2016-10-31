/**
 * 
 */
package com.beecloud.domain;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016-10-28 下午1:26:14
 * @version v1.0
 */
public class Rule {
	/**
	 * 规则id
	 */
	private int id;
	/**
	 * 规则名称
	 */
	private String name;
	/**
	 * 规则路径
	 */
	private String path;
	
	/**
	 * 规则状态
	 */
	private int status;
	
	/**
	 * 绑定的response_id
	 */
	private int response_id;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	/**
	 * @return the response_id
	 */
	public int getResponse_id() {
		return response_id;
	}

	/**
	 * @param response_id the response_id to set
	 */
	public void setResponse_id(int response_id) {
		this.response_id = response_id;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Rule [id=" + id + ", name=" + name + ", path=" + path
				+ ", status=" + status + ", response_id=" + response_id + "]";
	}
	
}
