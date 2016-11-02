/**
 * 
 */
package com.beecloud.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.beecloud.domain.Mock;
import com.beecloud.domain.Rule;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016-10-28 下午1:31:41
 * @version v1.0
 */
public interface RuleDao {
	
	/**
	 * 
	 * @param name
	 * @param path
	 */
	void insert(@Param("name")String name,@Param("path")String path,@Param("response_id")String response_id);
	 /**
	  * 
	  * @param id
	  * @return
	  */
	 Rule selectRuleById(Integer id);
	 
	 /**
	  * 
	  * @param title
	  * @return
	  */
	 List<Rule> selectRuleByName(@Param("name") String name);
	  
	 
	 
	 /**
	  * 
	  * @return
	  */
	 List<Rule> selectEnableRule();
	 
	 /**
	  * 
	  * @param id
	  */
	 void delectRuleById(@Param("id")Integer id);
	 
	 /**
	  * 
	  * @param id
	  * @param status
	  */
	 void updateRuleStatusById(@Param("id")Integer id,@Param("status")Integer status);
	 
	
	 List<Integer> selectUsedMockId();
}
