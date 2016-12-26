package com.beecloud.dao;

import com.beecloud.domain.TboxGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hong.lin
 * @description
 * @date 2016/12/22.
 */
public interface TboxGroupDao {
    TboxGroup selectTboxGroupById(@Param("id")Integer id);
    List<TboxGroup> selectTboxGroupByName(@Param("name")String name);
    List<TboxGroup> selectEnableTboxGroup();
    List<String> selectNameList();
    void insert(@Param("name")String name, @Param("description")String description);
    void delectTboxGroupById(@Param("id")Integer id);
    void updateTboxGroupById(@Param("id")Integer id,@Param("name")String name, @Param("description")String description,@Param("tboxs")String tboxs);
}
