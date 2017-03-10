package com.beecloud.controller;


import com.beecloud.domain.*;
import com.beecloud.service.*;
import com.beecloud.util.PagedResult;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    @Resource
    private MqttService mqttService;

    @Resource
    private TboxService tboxService;

    @Resource
    private TboxGroupService tboxGroupService;
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

    @RequestMapping("/tbox")
    public String tboxlist(){
        return "bootstrap/tbox";
    }

    @RequestMapping("/group")
    public String tboxGrouplist(){
        return "bootstrap/group";
    }

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
    
    
    @RequestMapping(value="/mock/list", method= {RequestMethod.POST})
    @ResponseBody
    public List<MockVo> list(){
    	return mockService.list();
    }
    
    @RequestMapping(value="/mock/insert", method= {RequestMethod.POST})
    @ResponseBody
    public void insert(String title,String url, String method,int statuscode,String response){
    	mockService.insert(title, url, method, statuscode, response);
    }

    @RequestMapping(value="/mock/delete", method= {RequestMethod.POST})
    @ResponseBody
    public String mockDelete(Integer id) {
        List<Integer> mock_used = ruleService.selectUsedMockId();
        if(mock_used.contains(id)){
            return responseFail("Mock已被绑定,删除对应规则后重试");
        }else{
            mockService.delectMockById(id);
            return responseSuccess("删除成功");
        }
    }
    
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
    public void update(Integer id, String title, String response,int statuscode) {
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


    /**
     *
     * @param threadName
     * @param type
     */
    @RequestMapping(value="/mqtt/connect", method= {RequestMethod.POST})
    @ResponseBody
    public void connect(String threadName,String type) {
         mqttService.connect(threadName, type);
    }


    /**
     * 订阅消息
     * @param threadName
     * @param topic
     * @param type
     */
    @RequestMapping(value="/mqtt/subscribe", method= {RequestMethod.POST})
    @ResponseBody
    public void subcribeTopic(String threadName,String topic,String type){
        mqttService.subscribeTopic(threadName,topic,type);
    }

    /**
     * 退订消息
     * @param threadName
     * @param topic
     * @param type
     */
    @RequestMapping(value="/mqtt/unSubscribe", method= {RequestMethod.POST})
    @ResponseBody
    public void unSubcribeTopic(String threadName,String topic,String type){
        mqttService.unSubscribeTopic(threadName,topic,type);
    }


    /**
     * 发送消息
     * @param message
     */
    @RequestMapping(value="/mqtt/send", method= {RequestMethod.POST})
    @ResponseBody
    public void sendMessage(String threadName,String message,String type) {
        mqttService.sendMessaage(threadName,message,type);
    }


    /**
     * 功能测试中发送消息
     */
    @RequestMapping(value="/mqtt/function/send", method= {RequestMethod.POST})
    @ResponseBody
    public void sendFunctionMessage(String message,String identity) {
        mqttService.sendFunctionMessage(message,identity);
    }

    /**
     * 断开连接
     */
    @RequestMapping(value="/mqtt/disconnect", method= {RequestMethod.GET})
    @ResponseBody
    public void disconnect(String threadName,String type) {
        mqttService.disconnect(threadName,type);
    }


    /**
     *清除缓存Map
     */
    @RequestMapping(value="/mqtt/clean", method= {RequestMethod.GET})
    @ResponseBody
    public void clean(String threadName,String type) {
        mqttService.clean(threadName,type);
    }


    /**
     * 获取message
     * @param key
     * @param timeOut
     * @return
     */
    @RequestMapping(value="/mqtt/receive", method= {RequestMethod.POST})
    @ResponseBody
    public String getMessage(String threadName,String key,int timeOut,String type) {
        String message = mqttService.getMessage(threadName,key,timeOut,type);
        return mqttResponse(message);
    }



    /**
     * 获取和Vin码相关的所有message
     * @return
     */
    @RequestMapping(value="/mqtt/receive/all", method= {RequestMethod.POST})
    @ResponseBody
    public String getMessage(String threadName) {
        String messages = mqttService.getAllMessage4Function(threadName);
        return mqttResponse(messages);
    }


    @RequestMapping(value="/tbox/insert", method= {RequestMethod.POST})
    @ResponseBody
    public String tboxInsert(String name,String data) {
        List<String> list = tboxService.selectNameList();
        if(list.contains(name)){
             return responseFail("名称已存在,请修改后重试");
         }else{
            tboxService.insert(name,data);
            return responseSuccess("插入成功");
         }
    }

    @RequestMapping(value="/tbox/update", method= {RequestMethod.POST})
    @ResponseBody
    public void tboxUpdate(Integer id,String name,String data,Integer delay) {
        tboxService.updateTboxById(id,name,data,delay);
    }

    @RequestMapping(value="/tbox/list/name", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String tboxGetNameList() {
        return tboxService.selectNameList().toString();
    }


    @RequestMapping(value="/tbox/delete", method= {RequestMethod.POST})
    @ResponseBody
    public void tboxDelete(Integer id) {
        tboxService.delectTboxById(id);
    }

    @RequestMapping(value="/tbox/list.do", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String tboxList(Integer pageNumber,Integer pageSize ,String name) {
        try {
            PagedResult<Tbox> pageResult = tboxService.queryByPage(name, pageNumber,pageSize);
            return responseSuccess(pageResult);
        } catch (Exception e) {
            return responseFail(e.getMessage());
        }
    }




    @RequestMapping(value="/group/insert", method= {RequestMethod.POST})
    @ResponseBody
    public String tboxGroupInsert(String name,String description) {
        List<String> list = tboxGroupService.selectNameList();
        if(list.contains(name)){
            return responseFail("名称已存在,请修改后重试");
        }else{
            tboxGroupService.insert(name,description);
            return responseSuccess("插入成功");
        }
    }

    @RequestMapping(value="/group/update", method= {RequestMethod.POST})
    @ResponseBody
    public void tboxGroupUpdate(Integer id,String name,String description,String tboxs) {
        tboxGroupService.updateTboxGroupById(id,name,description,tboxs);
    }


    @RequestMapping(value="/group/get", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String tboxGroupGet(Integer id) {
        Gson gson = new Gson();
        TboxGroup tboxGroup = tboxGroupService.selectTboxGroupById(id);
        return gson.toJson(tboxGroup);
    }


    @RequestMapping(value="/group/delete", method= {RequestMethod.POST})
    @ResponseBody
    public void tboxGroupDelete(Integer id) {
        tboxGroupService.delectTboxGroupById(id);
    }

    @RequestMapping(value="/group/list.do", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String tboxGroupList(Integer pageNumber,Integer pageSize ,String name) {
        try {
            PagedResult<TboxGroup> pageResult = tboxGroupService.queryByPage(name, pageNumber,pageSize);
            return responseSuccess(pageResult);
        } catch (Exception e) {
            return responseFail(e.getMessage());
        }
    }

    /**
     * 获取测试套名称和Tboxs键值关系
     * @return
     */
    @RequestMapping(value="/group/list", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public Map<String,String> tboxGroupList() {
        return tboxGroupService.selectNameAndTboxsMap();
    }


    /**
     * 根据测试套名称获取Tbox列表
     * @param pageNumber
     * @param pageSize
     * @param name
     * @return
     */
    @RequestMapping(value="/group/items", method= {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public String tboxGroupItemList(Integer pageNumber,Integer pageSize ,String name) {
        try {
            String[] array = name.split("\\|");
            List<String> names = Arrays.asList(array);
            PagedResult<Tbox> pageResult = tboxService.queryByPage(names, pageNumber,pageSize);
            return responseSuccess(pageResult);
        } catch (Exception e) {
            return responseFail(e.getMessage());
        }
    }
}
