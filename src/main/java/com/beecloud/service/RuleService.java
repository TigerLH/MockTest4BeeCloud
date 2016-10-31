package com.beecloud.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.beecloud.domain.Mock;
import com.beecloud.domain.Rule;
import com.beecloud.util.PagedResult;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016-10-28 上午10:18:37
 * @version v1.0
 */
public interface RuleService {
	void insert(String name,String path,String response_id);
	
	Rule selectRuleById(Integer id);
	
	List<Rule> selectEnableRule();
	
	List<Rule> selectRuleByName(String name);
	
	void delectRuleById(Integer id);
	
	void updateRuleStatusById(Integer id,Integer status);
	
	PagedResult<Rule> queryByPage(String name,Integer pageNo,Integer pageSize);

}
