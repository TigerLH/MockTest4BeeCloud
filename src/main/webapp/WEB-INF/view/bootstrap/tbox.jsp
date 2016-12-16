<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Mock Test For BeeCloud</title>
	<link href="<%=request.getContextPath()%>/static/js/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/static/css/simple-sidebar.css" rel="stylesheet">
	<script src="<%=request.getContextPath()%>/static/js/jQuery/jquery-2.1.4.min.js"></script>
	<script src="<%=request.getContextPath()%>/static/js/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/static/js/bootstrap/js/bootstrap-paginator.min.js"></script>
	<script src="<%=request.getContextPath()%>/static/js/bootstrap/js/mqttws31.min.js"></script>
<style type="text/css">
#queryDiv {
 margin-right: auto;
 margin-left: auto;
 width:800px;
}
#startServer {
	margin-right: auto;
	margin-left: auto;
	width:800px;
}

#textInput {
 margin-top: 10px;
}
#tableResult {
 margin-right: auto;
 margin-left: 280px;
 width:auto;
}
#showmessage {
	margin-right: auto;
	margin-left: 280px;
	width:800px;
	height:400px;
}

</style>
</head>
<body>
<%
	List<String> vehicle_list = new ArrayList<String>();
	try{
		Connection conn = null;
		Statement stat = null;
		//加载数据库驱动类
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		//数据库连接URL
		String url = "jdbc:mysql://10.28.4.35:3306/vehicle?useUnicode=true&characterEncoding=UTF-8";
		//数据库用户名
		String user = "root";
		//数据库密码
		String password = "beecloud2016";
		//根据数据库参数取得一个数据库连接
		conn = DriverManager.getConnection(url, user, password);
		stat = conn.createStatement();
		String sql = "select * from vehicle";
		ResultSet rs = stat.executeQuery(sql);
		while(rs.next()){
			vehicle_list.add(rs.getString("vin"));
		}
		if (rs!=null){
			rs.close();
		}
	}catch (Exception e){
		e.printStackTrace();
	}
%>
<!-- Sidebar -->
	 <div id="sidebar-wrapper">
	     <ul class="sidebar-nav">
	         <li class="sidebar-brand">
	             <a href="#">
	                Mock Server
	             </a>
	         </li>
			 <li class="sidebar-brand">
				 <a href="list">
					 HttpMock
				 </a>
			 </li>

			 <li class="sidebar-brand">
				 <a href="#">
					 TboxMock
				 </a>
			 <li>
				 <a href="tbox">Tbox管理</a>
			 </li>
			 </li>
	     </ul>
	 </div>
	 
	 <!-- 新增模态框（Modal） -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display:none">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" id="myModalLabel">
						新增数据
					</h4>
				</div>
				<div class="modal-body">
					<div class="control-group"><label class="control-label" for="name" >名称</label>
						<div class="controls" ><input id="name" placeholder="名称" type="text" style="width:300px;"/></div>
					</div>

					<div class="control-group"><label class="control-label"  for="data">消息体</label>
						<div class="controls">
							<textarea id="data" style="height:200px;width:300px;word-break:break-all; word-wrap:break-all;"></textarea>
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
					<button type="button" class="btn btn-primary" onclick="insert()">
						提交更改
					</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>

	 <!-- 编辑模态框（Modal） -->
	 <div class="modal fade" id="editModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display:none">
		 <div class="modal-dialog">
			 <div class="modal-content">
				 <div class="modal-header">
					 <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						 &times;
					 </button>
					 <h4 class="modal-title" id="myModalLabel">
						 编辑数据
					 </h4>
				 </div>
				 <div class="modal-body">
					 <div class="control-group"><label class="control-label" for="index" >id</label>
						 <div class="controls" ><input id="index" placeholder="index" type="text" readonly="true" style="width:300px;"/></div>
					 </div>

					 <div class="control-group"><label class="control-label" for="title" >标题</label>
						 <div class="controls" ><input id="title" placeholder="title" type="text" style="width:300px;"/></div>
					 </div>

					 <div class="form-group">
						 <label for="errorCode_select">错误码</label>
						 <select id="errorCode_select" class="form-control" style="width:300px;">
							 <option>OK ("无错误")</option>
							 <option>AUTH_ERR ("AuthToken验证失败")</option>
							 <option>VEHICLE_AUTH_ERR ("车辆认证失败")</option>
							 <option>PROTOCOL_DECODE_ERR ("协议解析错误")</option>
							 <option>CAN_COMMUNICATION_ERR ("CAN通信错误")</option>
							 <option>TBOX_SYS_ERR ("TBox系统忙")</option>
							 <option>INVALID_COMMAND ("无效指令")</option>
							 <option>CRC_CHECK_ERR ("CRC验证失败")</option>
							 <option>TASK_EXECUTE_FAIL ("任务执行失败")</option>
							 <option>TASK_EXECUTE_TIMEOUT ("任务执行超时")</option>
							 <option>RESPONSE_TOOLONG ("应答数据过长")</option>
							 <option>VEHICLE_CONFIGURATION_ERR ("车辆配置错误")</option>
							 <option>TASK_EXCUTE_INVALID ("任务执行条件不满足")</option>
							 <option>VEHICLE_UNCHARGED ("车辆未上电")</option>
							 <option>VOLTAGE_ERR ("电压异常")</option>
						 </select>
					 </div>

					 <div class="form-group">
						 <label for="commandStatus_select">命令状态</label>
						 <select id="commandStatus_select" class="form-control" style="width:300px;">
							 <option>COMPLETE</option>
							 <option>PROCESSING</option>
						 </select>
					 </div>

					 <label for="delay">延时发送</label>
					 <input id="delay" type="number" class="form-control" style="width:300px;">

					 <label class="control-label"  for="message">消息体</label>
					 <textarea id="message" class="info" style="height:300px;width:300px;word-break:break-all; word-wrap:break-all;"></textarea>
				 </div>
			 </div>
			 <div class="modal-footer">
				 <button type="button" class="btn btn-default" data-dismiss="modal">关闭
				 </button>
				 <button type="submit" class="btn btn-primary" onclick="update()">
					 提交更改
				 </button>
			 </div>
		 </div><!-- /.modal-content -->
	 </div><!-- /.modal -->
	 </div>


	<div id = "queryDiv">
		<input id = "textInput" type="text" placeholder="请输入标题" >
		<button id = "queryButton" class="btn btn-primary" type="button">查询</button>
		<button id = "createButton" class="btn btn-primary" type="button" data-toggle="modal" data-target="#myModal">新建</button>
	</div>
	<div id = "startServer">
		<select id="select_start_server" class="form-control"></select>
		<button id = "button_start_server" class="btn btn-success" type="button" onclick="startMockServer()">认证</button>
		<button id = "button_stop_server" class="btn btn-warning" type="button" onclick="stopMockServer()">退订</button>
	</div>



	<form id="form1">
		<table class="table table-hover table-bordered table-striped" id = 'tableResult'  style="word-break:break-all; word-wrap:break-all;">
			<caption>查询结果</caption>
			<thead>
				<tr>
					<th width=10%>id</th>
					<th width=20%>name</th>
					<th width=40%>data</th>
					<th width=10%>delay</th>
					<th width=20%>operation</th>
				</tr>
			</thead>
			<tbody id="tableBody">
			</tbody>
		</table>
		<!-- 底部分页按钮 -->
		<div id="bottomTab"></div>
		<textarea id="showmessage" style="word-break:break-all; word-wrap:break-all;"></textarea>
	</form>
	<script type='text/javascript'>
		var isRunning = true;
		//页面加载完成时初始化车辆下拉框
		$(document).ready(function(){
			var data = '<%=vehicle_list%>';
			var array = data.replace("[","").replace("]","").split(",");
			var count= document.getElementById('select_start_server').options.length;
			if(count>0){
				return;
			}
			$.each(array,function(index,value){
				if(""!=value){
					var content = '<option>' +value+ '</option>';
					$("#select_start_server").append(content);
				}
			});
		});

		//自动更新ErrorCode
		$("#errorCode_select").change(function(){
			var errorCode = $("#errorCode_select").val().split(" ")[0];
			var message = $("#message").val();
			var obj = JSON.parse(message);
			if(null!=obj['error']){
				$("#message").val(message.replace(obj['error'].errorCode,errorCode));
			}
			if(null!=obj['errorInfo']){
				$("#message").val(message.replace(obj['errorInfo'].errorCode,errorCode));
			}
		});

		//自动更新命令状态
		$("#commandStatus_select").change(function(){
			var command_Status = $("#commandStatus_select").val().split(" ")[0];
			var message = $("#message").val();
			var obj = JSON.parse(message);
			if(null!=obj['status']){
				$("#message").val(message.replace(obj['status'].status,command_Status));
			}
		});



		function receiveMessage() {
			var vin= $("#select_start_server").val();
			$.ajax({
				type: "post",
				url: "mqtt/receive/all",
				data: "vin=" + vin,
				dataType: 'html',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function (result) {
					var obj = JSON.parse(result);
					$('#showmessage').val(JSON.stringify(obj, null, 4));
					if (isRunning) {
						setTimeout("receiveMessage()", 5000);
					}
				}
			});
		}

		//启动服务
		function startMockServer(){
			isRunning = true;
			var vin= $("#select_start_server").val();
			var auth = '{ "vin": "'+vin+'", "pid": "BEECLOUD" }';
			$.ajax({
				type: "post",
				url: "mqtt/connect",
				data: "authMessage=" + auth +"&type=FUNCTION",
				dataType: 'html',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function(result) {
					document.getElementById("select_start_server").disabled=true;
					document.getElementById("button_start_server").disabled=true;
					document.getElementById("button_start_server").setAttribute("class","btn btn-default");
					receiveMessage();
				}
			});
		}

		//关闭服务
		function  stopMockServer() {
			var vin= $("#select_start_server").val();
			$.ajax({
				type: "get",
				url: "mqtt/disconnect",
				data: "type=FUNCTION" +"&vin=" + vin,
				dataType: 'html',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function(result) {
					document.getElementById("select_start_server").disabled=false;
					document.getElementById("button_start_server").disabled=false;
					document.getElementById("button_start_server").setAttribute("class","btn btn-success");
					isRunning = false;
				}
			});
		}


		//触发模态框的同时调用此方法
		function edit(obj) {
			var id = $(obj).attr("id");
			//获取表格中的一行数据
			var row = id%10;
			if(row==0){
				row=10;  //如果能被整除,则取最后一条
			}
			var id = document.getElementById("tableResult").rows[row].cells[0].innerText;
			var title = document.getElementById("tableResult").rows[row].cells[1].innerText;
			var message = document.getElementById("tableResult").rows[row].cells[2].innerText;
			var obj = JSON.parse(message);
			var json = JSON.stringify(obj,null,4);
			//向模态框中传值
			$('#index').val(id);
			$('#title').val(title);
			$('#message').val(json);
			$('#editModal').modal('show');
		}
			
		//插入规则
		function insert() {
			var name = $('#name').val();
			var data = $('#data').val();
			$.ajax({
				type: "post",
				url: "tbox/insert",
				data: "name=" + name + "&data=" + data,
				dataType: 'json',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function(result) {
					if(result.isError == true) {
						alert(result.errorMsg);
					}else{
						location.reload();
					}
				},
				error: function(e){
					alert("插入失败:" + e);
				}
			});
		}

		function update(){
			var id =  $('#index').val();
			var name = $('#title').val();
			var data = $('#message').val();
			var delay = $('#delay').val();
			$.ajax({
				type: "post",
				url: "tbox/update",
				data: "id=" + id +"&name=" + name + "&data=" + data + "&delay="+delay,
				dataType: 'html',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function(result) {
					location.reload();
				}
			});
		}

		//删除记录
		function del(obj) {
			var index = $(obj).attr("id");
			//获取表格中的一行数据
			var row = index%10;
			if(row==0){
				row=10;  //如果能被整除,则取最后一条
			}
			var id = document.getElementById("tableResult").rows[row].cells[0].innerText;
			$.ajax({
				type: "post",
				url: "tbox/delete",
				data: "id=" + id,
				dataType: 'html',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function(result) {
					location.reload();
				}
			});
		}



		//倒计时
		function timer(obj,time){
			obj.disabled = true;
			obj.innerText = time+"s";
			setTimeout(function () {
				time --;
				if(time==0){
					obj.innerText = "发送";
					obj.disabled = false;
					return;
				}else{
					obj.innerText = time+"s";
					timer(obj,time);
				}
			},1000);
		}

		//发送消息
		function send(obj) {
			var index = $(obj).attr("id");
			//获取表格中的一行数据
			var row = index%10;
			if(row==0){
				row=10;  //如果能被整除,则取最后一条
			}
			var message = document.getElementById("tableResult").rows[row].cells[2].innerText;
			var delay = document.getElementById("tableResult").rows[row].cells[3].innerText;
			var vin= $("#select_start_server").val();
			if(""!=delay){
				timer(obj,delay);
				setTimeout(function () {
					$.ajax({
						type: "post",
						url: "mqtt/function/send",
						data: "message=" + message +"&vin=" + vin,
						dataType: 'html',
						contentType: "application/x-www-form-urlencoded; charset=utf-8",
						success: function(result) {
							alert("发送成功");
						},error: function(e) {
							alert("发送失败:" + e);
						}
					});
				},delay*1000);
			}else{
				$.ajax({
					type: "post",
					url: "mqtt/function/send",
					data: "message=" + message +"&vin=" + vin,
					dataType: 'html',
					contentType: "application/x-www-form-urlencoded; charset=utf-8",
					success: function(result) {
						alert("发送成功");
					},error: function(e) {
						alert("发送失败:" + e);
					}
				});
			}
		}


	    var PAGESIZE = 10;
        var options = {  
            currentPage: 1,  //当前页数
            totalPages: 10,  //总页数，这里只是暂时的，后头会根据查出来的条件进行更改
            size:"normal",  
            alignment:"center",  
            itemTexts: function (type, page, current) {  
                switch (type) {  
                    case "first":  
                        return "第一页";  
                    case "prev":  
                        return "前一页";  
                    case "next":  
                        return "后一页";  
                    case "last":  
                        return "最后页";  
                    case "page":  
                        return  page;  
                }                 
            },  
            onPageClicked: function (e, originalEvent, type, page) {  
            	var title = $("#textInput").val(); //取内容
            	buildTable(name,page,PAGESIZE);//默认每页最多10条
            }  
        };

        //获取当前项目的路径
        var urlRootContext = (function () {
            var strPath = window.document.location.pathname;
            var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
            return postPath;
        })();
        
       
        //生成表格
        function buildTable(name,pageNumber,pageSize) {
        	 var url =  urlRootContext + "/tbox/list.do"; //请求的地址
             var reqParams = {'name':name, 'pageNumber':pageNumber,'pageSize':pageSize};//请求数据
             $(function () {   
             	  $.ajax({
             	        type:"POST",
             	        url:url,
             	        data:reqParams,
             	        async:false,
             	        dataType:"json",
             	        success: function(data){
             	            if(data.isError == false) {
             	           // options.totalPages = data.pages;
             	        var newoptions = {  
                        currentPage: 1,  //当前页数
                        totalPages: data.pages==0?1:data.pages,  //总页数
                        size:"normal",  
                        alignment:"center",  
                        itemTexts: function (type, page, current) {  
                        switch (type) {  
                            case "first":  
                            return "第一页";  
                            case "prev":  
                            return "前一页";  
                            case "next":  
                            return "后一页";  
                            case "last":  
                            return "最后页";  
                        case "page":  
                        return  page;  
                }                 
            },  
            onPageClicked: function (e, originalEvent, type, page) {  
            	var name = $("#textInput").val(); //取内容
            	buildTable(name,page,PAGESIZE);//默认每页最多10条
            }  
         };
         $('#bottomTab').bootstrapPaginator("setOptions",newoptions); //重新设置总页面数目
         var dataList = data.dataList;
         $("#tableBody").empty();//清空表格内容
         if (dataList.length > 0 ) {
             $(dataList).each(function(index,element){//重新生成
                    var content = '<tr class=success>';
                    index = index+1;
                    content += '<td>' + element.id + '</td>';
                    content += '<td>' + element.name + '</td>';
                    content += '<td>' + element.data + '</td>';
				    content += '<td>' + element.delay + '</td>';
                    content += '<td>';
				    content += '<button class="btn btn-info" type="button"'+'id="'+index+'" ' +
					         'data-container="body" data-toggle="popover" data-placement="top"'+
							 'data-content="发送成功"'+
							 'onclick=send(this)>发送</button>';
				    content += '<button class="btn btn-warning" type="button"'+'id="'+index+'"onclick=edit(this)>编辑</button>';
                    content += '<button class="btn btn-danger" type="button"'+'id="'+index+'"onclick=del(this)>删除</button>';
                    content += '</td>';
                    content += '</tr>';
                    $("#tableBody").append(content);
             	    });
             	    } else {             	            	
             	          $("#tableBody").append('<tr><th colspan ="6"><center>查询无数据</center></th></tr>');
             	    }
             	    }else{
             	          alert(data.errorMsg);
             	            }
             	      },
             	        error: function(e){
             	           alert("查询失败:" + e);
             	        }
             	    });
               });
        }
        
        //渲染完就执行
        $(function() {
        	
        	//生成底部分页栏
            $('#bottomTab').bootstrapPaginator(options);     
        	
        	buildTable("",1,10);//默认空白查全部
        	
            //创建结算规则
            $("#queryButton").bind("click",function(){
            	var name = $("#textInput").val();	
            	buildTable(name,1,PAGESIZE);
            });
        });
    </script>
</body>
</html>