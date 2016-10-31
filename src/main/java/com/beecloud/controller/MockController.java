package com.beecloud.controller;


import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.beecloud.domain.Mock;
import com.beecloud.domain.MockVo;
import com.beecloud.domain.Rule;
import com.beecloud.service.MockService;
import com.beecloud.service.RuleService;
import com.beecloud.util.PagedResult;

/**
 * 
 * @description //TODO
 * @author hong.lin@beecloud.com
 * @date 2016-10-31 下午6:16:56
 * @version v1.0
 */
@Controller
public class MockController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Resource
	private MockService mockService;
	
	@Resource
	private RuleService ruleService;
	
	@RequestMapping("/")  
    public ModelAndView getIndex(){    
		ModelAndView mav = new ModelAndView("index"); 
		Mock mock = mockService.selectMockById(1);
	    mav.addObject("mock", mock); 
        return mav;  
    }  
	

	@RequestMapping("/list")  
	public String mocklist(){
		return "bootstrap/list";
	}
	
	
	@RequestMapping("/rule")
	public String rulelist(){
		return "bootstrap/rule";
	}
	
    /**
     * 分页查询用户信息
     * @author linbingwen
     * @since  2015年10月23日 
     * @param page
     * @return
     */
    @RequestMapping(value="/mock/list.do", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String mockList(Integer pageNumber,Integer pageSize ,String title) {
		logger.info("分页查询用户信息列表请求入参：pageNumber{},pageSize{}", pageNumber,pageSize);
		try {
			PagedResult<Mock> pageResult = mockService.queryByPage(title, pageNumber,pageSize);
    	    return responseSuccess(pageResult);
    	} catch (Exception e) {
			return responseFail(e.getMessage());
		}
    }
    
    
    @RequestMapping(value="/mock/list", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public List<MockVo> list(){
    	return mockService.list();
    }
    
    /**
     * 分页查询用户信息
     * @author linbingwen
     * @since  2015年10月23日 
     * @param page
     * @return
     */
    @RequestMapping(value="/rule/list.do", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String ruleList(Integer pageNumber,Integer pageSize ,String name) {
		logger.info("分页查询用户信息列表请求入参：pageNumber{},pageSize{}", pageNumber,pageSize);
		try {
			PagedResult<Rule> pageResult = ruleService.queryByPage(name, pageNumber,pageSize);
    	    return responseSuccess(pageResult);
    	} catch (Exception e) {
			return responseFail(e.getMessage());
		}
    }
    
    /**
     * 更新mock值
     * @param id
     * @param title
     * @param response
     */
    @RequestMapping(value="/mock/update/all", method= {RequestMethod.POST})
    @ResponseBody
    public void update(Integer id, String title, String response,String statuscode) {
    	mockService.updateMockById(id, title, response,statuscode);
    }
    
    
    
    /**
     * 插入规则
     * @param name
     * @param path
     */
    @RequestMapping(value="/rule/insert", method= {RequestMethod.POST})
    @ResponseBody
    public void insert(String name,String path,String response_id) {
    	ruleService.insert(name, path,response_id);
    }
    
    
    @RequestMapping(value="/mock/delete", method= {RequestMethod.POST})
    @ResponseBody
    public void mockDelete(Integer id) {
    	mockService.delectMockById(id);
    }
    
    
    @RequestMapping(value="/rule/delete", method= {RequestMethod.POST})
    @ResponseBody
    public void ruleDelete(Integer id) {
    	ruleService.delectRuleById(id);
    }
    
    /**
     * 更新规则状态
     * @param id
     * @param status
     */
    @RequestMapping(value="/rule/update/status", method= {RequestMethod.POST})
    @ResponseBody
    public void ruleUpdateStatus(Integer id,Integer status) {
    	ruleService.updateRuleStatusById(id, status);
    }
    
    
    @RequestMapping(value="/rule/get/enable", method= {RequestMethod.GET})
    @ResponseBody
    public List<Rule> getEnableStatusRule() {
    	return ruleService.selectEnableRule();
    }
    
}
