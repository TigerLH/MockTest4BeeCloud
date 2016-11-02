package com.beecloud.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.beecloud.dao.RuleDao;
import com.beecloud.domain.Rule;
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
public class RuleServiceImpl implements RuleService{
	@Resource
	private RuleDao ruleDao;
	
	public Rule selectRuleById(Integer id) {
		return ruleDao.selectRuleById(id);
		
	}

	public List<Rule> selectRuleByName(String Name) {
		List<Rule> list = ruleDao.selectRuleByName(Name);
		return list;
	}

	public PagedResult<Rule> queryByPage(String Name,Integer pageNo,Integer pageSize ) {
		pageNo = pageNo == null?1:pageNo;
		pageSize = pageSize == null?10:pageSize;
		PageHelper.startPage(pageNo,pageSize);  //startPage是告诉拦截器说我要开始分页了。分页参数是这两个。
		return BeanUtil.toPagedResult(ruleDao.selectRuleByName(Name));
	}


	/* (non-Javadoc)
	 * @see com.lin.service.RuleService#delectRuleById(java.lang.Integer)
	 */
	@Override
	public void delectRuleById(Integer id) {
		ruleDao.delectRuleById(id);
	}

	/* (non-Javadoc)
	 * @see com.lin.service.RuleService#updateRuleStatusById(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void updateRuleStatusById(Integer id, Integer status) {
		ruleDao.updateRuleStatusById(id, status);
	}

	/* (non-Javadoc)
	 * @see com.lin.service.RuleService#insert(java.lang.String, java.lang.String)
	 */
	@Override
	public void insert(String name, String path,String response_id) {
		ruleDao.insert(name, path,response_id);
	}

	/* (non-Javadoc)
	 * @see com.lin.service.RuleService#selectEnableRule()
	 */
	@Override
	public List<Rule> selectEnableRule() {
		// TODO Auto-generated method stub
		return ruleDao.selectEnableRule();
	}

	/* (non-Javadoc)
	 * @see com.beecloud.service.RuleService#selectUsedMockId()
	 */
	@Override
	public List<Integer> selectUsedMockId() {
		// TODO Auto-generated method stub
		return ruleDao.selectUsedMockId();
	}

}
