<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
<style type="text/css">
#queryDiv {
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
</style>
</head>
<body>
	 <!-- Sidebar -->
	 <div id="sidebar-wrapper">
	     <ul class="sidebar-nav">
	         <li class="sidebar-brand">
	             <a href="#">
	                Mock Server
	             </a>
	         </li>
	         <li>
	             <a href="list">Mock管理</a>
	         </li>
	       	 <li>
	             <a href="rule">Rule管理</a>
	         </li>
	     </ul>
	 </div>
	 
	 
     <!-- 模态框（Modal） -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display:none">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" id="myModalLabel">
						Mock用例编辑
					</h4>
				</div>
				<div class="modal-body">
					<div class="control-group"><label class="control-label" for="index" >id</label>
						<div class="controls" ><input id="index" placeholder="index" type="text" readonly="true" style="width:300px;"/></div>
					</div>
					
					<div class="control-group"><label class="control-label" for="title" >标题</label>
						<div class="controls" ><input id="title" placeholder="title" type="text" style="width:300px;"/></div>
					</div>
					
					<div class="control-group"><label class="control-label"  for="url">url</label>
						<div class="controls"><input id="url" placeholder="url" type="text" readonly="true" style="width:300px;"/></div>
					</div>
					
					<div class="control-group"><label class="control-label"  for="method">请求方法</label>
						<div class="controls"><input id="method" placeholder="method" type="text" readonly="true" style="width:300px;"/></div>
					</div>
					
					<div class="control-group"><label class="control-label"  for="statuscode">状态码</label>
						<div class="controls"><input id="statuscode" placeholder="statuscode" type="text" style="width:300px;"/></div>
					</div>
					<label class="control-label"  for="response">Response</label>
					<textarea id="response" style="height:200px;width:300px;word-break:break-all; word-wrap:break-all;"></textarea>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
					<button type="button" class="btn btn-primary" onclick="update()">
						提交更改
					</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>
	   
	<div id = "queryDiv">
		<input id = "textInput" type="text" placeholder="请输入标题" >
		<button id = "queryButton" class="btn btn-primary" type="button">查询</button>
		<button id = "createButton" class="btn btn-primary" type="button">新建</button>
	</div>
	<form id="form1">
		<table class="table table-hover table-bordered table-striped" id = 'tableResult'  style="word-break:break-all; word-wrap:break-all;">
			<caption>查询结果</caption>
			<thead>
				<tr>
					<th width=5%>序号</th>
					<th width=10%>标题</th>
					<th width=15%>路径</th>
					<th width=10%>请求方法</th>
					<th width=5%>状态码</th>
					<th width=40%>响应</th>
					<th width=20%>操作</th>
				</tr>
			</thead>
			<tbody id="tableBody">
			</tbody>
		</table>
		<!-- 底部分页按钮 -->
		<div id="bottomTab"></div>
	</form>
	<script type='text/javascript'>
		//触发模态框的同时调用此方法
		function editInfo(obj) {
			var id = $(obj).attr("id");
			//获取表格中的一行数据
			var row = id%10;
			if(row==0){
				row=10;  //如果能被整除,则取最后一条
			}
			var id = document.getElementById("tableResult").rows[row].cells[0].innerText;
			var title = document.getElementById("tableResult").rows[row].cells[1].innerText;
			var url = document.getElementById("tableResult").rows[row].cells[2].innerText;
			var method = document.getElementById("tableResult").rows[row].cells[3].innerText;
			var statuscode = document.getElementById("tableResult").rows[row].cells[4].innerText;
			var response = document.getElementById("tableResult").rows[row].cells[5].innerText;
			//向模态框中传值
			$('#index').val(id);
			$('#title').val(title);
			$('#url').val(url);
			$('#method').val(method);
			$('#statuscode').val(statuscode);
			$('#response').val(response);
			$('#mymodal').modal('show');
		}
		//提交更改
		function update() {
			//获取模态框数据
			var id = $('#index').val();
			var title = $('#title').val();
			var statuscode = $('#statuscode').val();
			var response = $('#response').val();
			$.ajax({
				type: "post",
				url: "mock/update/all",
				data: "id=" + id + "&title=" + title + "&statuscode=" + statuscode + "&response=" + response,
				dataType: 'html',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function(result) {
					location.reload();
				}
			});
		}
		
		
// 		function status(object){
// 			var index = $(object).attr("id");
			//获取表格中的一行数据
// 			var row = index%10;
// 			if(row==0){
// 				row=10;  //如果能被整除,则取最后一条
// 			}
// 			var id = document.getElementById("tableResult").rows[row].cells[0].innerText;
// 			if(object.innerText=="运行"){
// 				$.ajax({
// 					type: "post",
// 					url: "update/status",
// 					data: "id=" + id+ "&status=" + 1,
// 					dataType: 'html',
// 					contentType: "application/x-www-form-urlencoded; charset=utf-8",
// 					success: function(result) {
// 						object.setAttribute("class", "btn btn-danger");
// 						object.innerText="停止";
// 					}
// 				});
// 			}else{
// 				$.ajax({
// 					type: "post",
// 					url: "update/status",
// 					data: "id=" + id+ "&status=" + 0,
// 					dataType: 'html',
// 					contentType: "application/x-www-form-urlencoded; charset=utf-8",
// 					success: function(result) {
// 						object.setAttribute("class", "btn btn-info");
// 						object.innerText="运行";
// 					}
// 				});
// 			}
// 		}
		
		
		
		//运行停止
// 		$(document).on('click','#runcode',function(){
// 			var _this = $(this);
			//运行状态
// 			if(_this.hasClass("btn-danger")){
// 				_this.removeClass("btn-danger").addClass("btn-info");
// 				_this.text("运行");
// 			}
			//停止状态
// 			else{
// 				_this.addClass("btn-danger").removeClass("btn-info");
// 				_this.text("停止");
// 			}
// 		});
			
		
		
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
				url: "mock/delete",
				data: "id=" + id,
				dataType: 'html',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function(result) {
					location.reload();
				}
			});
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
            	buildTable(title,page,PAGESIZE);//默认每页最多10条
            }  
        }  

        //获取当前项目的路径
        var urlRootContext = (function () {
            var strPath = window.document.location.pathname;
            var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
            return postPath;
        })();
        
       
        //生成表格
        function buildTable(title,pageNumber,pageSize) {
        	 var url =  urlRootContext + "/mock/list.do"; //请求的网址
             var reqParams = {'title':title, 'pageNumber':pageNumber,'pageSize':pageSize};//请求数据
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
            	var title = $("#textInput").val(); //取内容
            	buildTable(title,page,PAGESIZE);//默认每页最多10条
            }  
         }             	           
         $('#bottomTab').bootstrapPaginator("setOptions",newoptions); //重新设置总页面数目
         var dataList = data.dataList;
         $("#tableBody").empty();//清空表格内容
         if (dataList.length > 0 ) {
             $(dataList).each(function(index,element){//重新生成
                    var content = '<tr class=success>';
                    index = index+1;
                    content += '<td>' + element.id + '</td>';
                    content += '<td>' + element.title + '</td>';
                    content += '<td>' + element.url + '</td>';
                    content += '<td>' + element.method + '</td>';
                    content += '<td>' + element.statuscode + '</td>';
                    content += '<td>' + element.response + '</td>';
                    content += '<td>';
                    content += '<button class="btn btn-warning" type="button"'+'id="'+index+'"data-toggle="modal" data-target="#myModal" onclick="editInfo(this)">编辑</button>';
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
            	var title = $("#textInput").val();	
            	buildTable(title,1,PAGESIZE);
            });
        });
    </script>
</body>
</html>