package com.beecloud.service;

import com.beecloud.dao.TboxDao;
import com.beecloud.domain.Tbox;
import com.beecloud.util.BeanUtil;
import com.beecloud.util.PagedResult;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hong.lin
 * @description
 * @date 2016/11/24.
 */
@Service
public class TboxServiceImpl implements TboxService{
    @Autowired
    private TboxDao tboxDao;

    @Override
    public void insert(String name, String data) {
        tboxDao.insert(name,data);
    }

    @Override
    public Tbox selectTboxById(Integer id) {
        return tboxDao.selectTboxById(id);
    }

    @Override
    public List<Tbox> selectEnableTbox() {
        return tboxDao.selectEnableTbox();
    }

    @Override
    public List<Tbox> selectTboxByName(String name) {
        return tboxDao.selectTboxByName(name);
    }

    @Override
    public void delectTboxById(Integer id) {
        tboxDao.delectTboxById(id);
    }

    @Override
    public void updateTboxById(Integer id,String name,String data,int delay){
        tboxDao.updateTboxById(id,name,data,delay);
    }

    @Override
    public List<String> selectNameList(){
        return tboxDao.selectNameList();
    }

    @Override
    public PagedResult<Tbox> queryByPage(String name, Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null?1:pageNo;
        pageSize = pageSize == null?10:pageSize;
        PageHelper.startPage(pageNo,pageSize);
        return BeanUtil.toPagedResult(tboxDao.selectTboxByName(name));
    }
}
