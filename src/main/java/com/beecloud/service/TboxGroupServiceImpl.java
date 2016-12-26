package com.beecloud.service;

import com.beecloud.dao.TboxGroupDao;
import com.beecloud.domain.TboxGroup;
import com.beecloud.util.BeanUtil;
import com.beecloud.util.PagedResult;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hong.lin
 * @description
 * @date 2016/12/22.
 */
@Service
public class TboxGroupServiceImpl implements TboxGroupService{
    @Autowired
    TboxGroupDao tboxGroupDao;

    @Override
    public TboxGroup selectTboxGroupById(Integer id) {
        return tboxGroupDao.selectTboxGroupById(id);
    }

    @Override
    public List<TboxGroup> selectTboxGroupByName(String name) {
        return tboxGroupDao.selectTboxGroupByName(name);
    }

    @Override
    public Map<String,String> selectNameAndTboxsMap() {
        Map<String,String> map = new HashMap<String, String>();
        List<TboxGroup> list = tboxGroupDao.selectEnableTboxGroup();
        for(TboxGroup tboxGroup : list){
            String key = tboxGroup.getName();
            String value = tboxGroup.getTboxs();
            if(null!=value){
                map.put(key,value);
            }
        }
        return map;
    }

    @Override
    public List<String> selectNameList() {
        return tboxGroupDao.selectNameList();
    }


    @Override
    public void insert(String name, String description) {
        tboxGroupDao.insert(name,description);
    }

    @Override
    public void delectTboxGroupById(Integer id) {
        tboxGroupDao.delectTboxGroupById(id);
    }

    @Override
    public void updateTboxGroupById(Integer id,String name, String description,String tboxs) {
        tboxGroupDao.updateTboxGroupById(id,name,description,tboxs);
    }

    @Override
    public PagedResult<TboxGroup> queryByPage(String name, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null?1:pageNo;
        pageSize = pageSize == null?10:pageSize;
        PageHelper.startPage(pageNo,pageSize);
        return BeanUtil.toPagedResult(tboxGroupDao.selectTboxGroupByName(name));
    }
}
