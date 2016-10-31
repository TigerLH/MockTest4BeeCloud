/**
 * 
 */
package com.beecloud.domain;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016-10-28 下午11:40:07
 * @version v1.0
 */
public class MockVo {
	private String id;
	private String title;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MockVo [id=" + id + ", title=" + title + "]";
	}
	
}
