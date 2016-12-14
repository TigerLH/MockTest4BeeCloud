/**
 * 
 */
package com.beecloud.dao;

import com.beecloud.domain.Tbox;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016-10-28 下午1:31:41
 * @version v1.0
 */
public interface TboxDao {

	/**
	 *
	 * @param name
	 * @param data
     */
	void insert(@Param("name") String name, @Param("data") String data);
	 /**
	  * 
	  * @param id
	  * @return
	  */
	 Tbox selectTboxById(Integer id);
	 
	 /**
	  * 
	  * @param name
	  * @return
	  */
	 List<Tbox> selectTboxByName(@Param("name") String name);

	 /**
	  * 
	  * @return
	  */
	 List<Tbox> selectEnableTbox();

	/**
	 * @return
     */
	 List<String> selectNameList();

	 /**
	  * 
	  * @param id
	  */
	 void delectTboxById(@Param("id") Integer id);

	 void updateTboxById(@Param("id")Integer id,@Param("name")String name,@Param("data")String data,@Param("delay")Integer delay);
}
