package com.beecloud.service;

import com.beecloud.domain.Tbox;
import com.beecloud.util.PagedResult;

import java.util.List;


public interface TboxService {
	void insert(String name, String data);
	
	Tbox selectTboxById(Integer id);
	
	List<Tbox> selectEnableTbox();
	
	List<Tbox> selectTboxByName(String name);
	
	void delectTboxById(Integer id);
	
	
	PagedResult<Tbox> queryByPage(String name, Integer pageNo, Integer pageSize);

}
