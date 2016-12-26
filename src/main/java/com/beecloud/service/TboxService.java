package com.beecloud.service;

import com.beecloud.domain.Tbox;
import com.beecloud.util.PagedResult;

import java.util.List;


public interface TboxService {
	void insert(String name, String data);
	
	Tbox selectTboxById(Integer id);
	
	List<Tbox> selectEnableTbox();
	
	List<Tbox> selectTboxByName(String name);

	List<String> selectNameList();

	void delectTboxById(Integer id);

	void updateTboxById(Integer id, String name,String data,Integer delay);
	
	PagedResult<Tbox> queryByPage(String name, Integer pageNo, Integer pageSize);

	PagedResult<Tbox> queryByPage(List<String> names, Integer pageNo, Integer pageSize);
}
