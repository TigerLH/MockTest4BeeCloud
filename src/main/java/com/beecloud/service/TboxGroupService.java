package com.beecloud.service;

import com.beecloud.domain.TboxGroup;
import com.beecloud.util.PagedResult;

import java.util.List;
import java.util.Map;

/**
 * @author hong.lin
 * @description
 * @date 2016/12/22.
 */
public interface TboxGroupService {
    TboxGroup selectTboxGroupById(Integer id);
    List<TboxGroup> selectTboxGroupByName(String name);
    Map<String,String> selectNameAndTboxsMap();
    List<String> selectNameList();
    void insert(String name,String description);
    void delectTboxGroupById(Integer id);
    void updateTboxGroupById(Integer id,String name,String description,String tboxs);
    PagedResult<TboxGroup> queryByPage(String name, Integer pageNo, Integer pageSize);
}
