package com.beecloud.service;

import com.beecloud.domain.Mock;
import com.beecloud.domain.MockVo;
import com.beecloud.util.PagedResult;

import java.util.List;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016-10-28 上午10:18:37
 * @version v1.0
 */
public interface MockService {
	
	Mock selectMockById(Integer id);
	
	List<MockVo> list();
	
	List<Mock> selectMockByTitle(String title);
	
	void updateMockById(Integer id,String title,String response,int statuscode);
	
	void delectMockById(Integer id);
	
	void updateMockStatusById(Integer id,Integer status);

	void insert(String title,String url, String method,int statuscode,String response);
	
	PagedResult<Mock> queryByPage(String title,Integer pageNo,Integer pageSize);

}
