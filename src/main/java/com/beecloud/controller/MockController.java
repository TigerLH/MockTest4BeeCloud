package com.beecloud.controller;


import com.beecloud.domain.Mock;
import com.beecloud.domain.MockVo;
import com.beecloud.domain.Rule;
import com.beecloud.domain.Tbox;
import com.beecloud.service.MockService;
import com.beecloud.service.MqttService;
import com.beecloud.service.RuleService;
import com.beecloud.service.TboxService;
import com.beecloud.util.PagedResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

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
    public String mqttlist(){
        return "bootstrap/tbox";
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
     * 连接
     * @param authMessage
     */
    @RequestMapping(value="/mqtt/connect", method= {RequestMethod.POST})
    @ResponseBody
    public void startMqttServer4AutoTest(String authMessage,String type) {
         mqttService.run(type);
         mqttService.sendAuthReqMessage(authMessage,type);
    }

//    /**
//     * 功能测试连接
//     * @param authMessage
//     */
//    @RequestMapping(value="/mqtt/function/connect", method= {RequestMethod.POST})
//    @ResponseBody
//    public void startMqttServer4Function(String authMessage) {
//        mqttService.run(Type.FUNCTION);
//        mqttService.sendAuthReqMessage(authMessage,Type.FUNCTION);
//    }


    @RequestMapping(value="/mqtt/subscribe", method= {RequestMethod.POST})
    @ResponseBody
    public void subcribeTopic4AutoTest(String topic,String type){
        mqttService.subscribeTopic(topic,type);
    }

//    @RequestMapping(value="/mqtt/function/subscribe", method= {RequestMethod.POST})
//    @ResponseBody
//    public void subcribeTopic4Function(String topic){
//        mqttService.subscribeTopic(topic,Type.FUNCTION);
//    }

    /**
     * 发送消息
     * @param message
     */
    @RequestMapping(value="/mqtt/send", method= {RequestMethod.POST})
    @ResponseBody
    public void sendMessage(String message,String type) {
        mqttService.sendMessaage(message,type);
    }


//    /**
//     * 功能测试中发送消息
//     */
//    @RequestMapping(value="/mqtt/function/send", method= {RequestMethod.POST})
//    @ResponseBody
//    public void sendFunctionMessage(String message,String vin) {
//        mqttService.sendFunctionMessage(message,vin);
//    }

    /**
     * 断开连接
     */
    @RequestMapping(value="/mqtt/disconnect", method= {RequestMethod.GET})
    @ResponseBody
    public void disconnect(String type) {
        mqttService.stop(type);
    }

//    /**
//     * 功能测试断开连接
//     */
//    @RequestMapping(value="/mqtt/function/disconnect", method= {RequestMethod.GET})
//    @ResponseBody
//    public void disconnect4Function() {
//        mqttService.stop(Type.FUNCTION);
//    }

    /**
     * 获取message
     * @param key
     * @param timeOut
     * @return
     */
    @RequestMapping(value="/mqtt/receive", method= {RequestMethod.POST})
    @ResponseBody
    public String getMessage(String key,int timeOut,String type) {
        String message = mqttService.getMessageByKey(key,timeOut, type);
        return mqttResponse(message);
    }

//    /**
//     * 功能测试获取message
//     * @param key
//     * @param timeOut
//     * @return
//     */
//    @RequestMapping(value="/mqtt/function/receive", method= {RequestMethod.POST})
//    @ResponseBody
//    public String getMessage4Function(String key,int timeOut) {
//        String message = mqttService.getMessageByKey(key,timeOut, Type.FUNCTION);
//        return mqttResponse(message);
//    }


    @RequestMapping(value="/tbox/insert", method= {RequestMethod.POST})
    @ResponseBody
    public void tboxInsert(String name,String data) {
        tboxService.insert(name,data);
    }

    @RequestMapping(value="/tbox/update", method= {RequestMethod.POST})
    @ResponseBody
    public void tboxUpdate(Integer id,String name,String data) {
        tboxService.updateTboxById(id,name,data);
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

}
