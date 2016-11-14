package com.beecloud.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.beecloud.dao.MockDao;
import com.beecloud.domain.Mock;
import com.beecloud.domain.MockVo;
import com.beecloud.util.BeanUtil;
import com.beecloud.util.PagedResult;
import com.github.pagehelper.PageHelper;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016-10-24 下午2:01:07
 * @version v1.0
 */
@Service
public class MockServiceImpl implements MockService{
	@Resource
	private MockDao mockDao;

	public Mock selectMockById(Integer id) {
		return mockDao.selectMockById(id);
		
	}

	public List<Mock> selectMockByTitle(String title) {
		List<Mock> list = mockDao.selectMockByTitle(title);
		return list;
	}

	public PagedResult<Mock> queryByPage(String title,Integer pageNo,Integer pageSize ) {
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);
		return BeanUtil.toPagedResult(mockDao.selectMockByTitle(title));
	}

	/* (non-Javadoc)
	 * @see com.lin.service.MockService#updateMockById(java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateMockById(Integer id, String title, String response,int statuscode) {
		mockDao.updateMockById(id, title, response,statuscode);
	}

	/* (non-Javadoc)
	 * @see com.lin.service.MockService#delectMockById(java.lang.Integer)
	 */
	@Override
	public void delectMockById(Integer id) {
		mockDao.delectMockById(id);
	}

	/* (non-Javadoc)
	 * @see com.lin.service.MockService#updateMockStatusById(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void updateMockStatusById(Integer id, Integer status) {
		mockDao.updateMockStatusById(id, status);
	}

	/* (non-Javadoc)
	 * @see com.lin.service.MockService#list()
	 */
	@Override
	public List<MockVo> list() {
		// TODO Auto-generated method stub
		return mockDao.list();
	}

	/* (non-Javadoc)
	 * @see com.beecloud.service.MockService#insert(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void insert(String title, String url, String method,
			int statuscode, String response) {
		mockDao.insert(title, url, method, statuscode, response);
	}


}
