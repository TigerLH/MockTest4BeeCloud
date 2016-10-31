package com.beecloud.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.beecloud.domain.Mock;
import com.beecloud.domain.MockVo;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016-10-24 下午1:56:50
 * @version v1.0
 */
public interface MockDao {
	 /**
	  * 
	  * @param id
	  * @return
	  */
	 Mock selectMockById(Integer id);
	 
	 /**
	  * 
	  * @return
	  */
	 List<MockVo> list();
	 /**
	  * 
	  * @param title
	  * @return
	  */
	 List<Mock> selectMockByTitle(@Param("title") String title);
	 
	 
	 void updateMockById(@Param("id")Integer id,@Param("title")String title,@Param("response")String response,@Param("statuscode")String statuscode);
	 
	 void delectMockById(@Param("id")Integer id);
	 
	 void updateMockStatusById(@Param("id")Integer id,@Param("status")Integer status);
}
