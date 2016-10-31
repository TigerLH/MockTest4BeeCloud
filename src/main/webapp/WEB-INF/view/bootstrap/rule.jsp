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
	             <a href="#">Rule管理</a>
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
						新增规则
					</h4>
				</div>
				<div class="modal-body">
					<div class="control-group"><label class="control-label" for="name" >名称</label>
						<div class="controls" ><input id="name" placeholder="名称" type="text" style="width:300px;"/></div>
					</div>
					
					<div class="control-group"><label class="control-label"  for="path">规则</label>
						<div class="controls"><input id="path" placeholder="规则" type="text" style="width:300px;"/></div>
					</div>
					
					<div class="form-group">
						<label for="name">绑定Mock值</label>
						<select id="myselect" class="form-control">
						</select>
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
	   
	<div id = "queryDiv">
		<input id = "textInput" type="text" placeholder="请输入标题" >
		<button id = "queryButton" class="btn btn-primary" type="button">查询</button>
		<button id = "createButton" class="btn btn-primary" type="button" data-toggle="modal" data-target="#myModal" onclick="bindselect()">新建</button>
	</div>
	<form id="form1">
		<table class="table table-hover table-bordered table-striped" id = 'tableResult'  style="word-break:break-all; word-wrap:break-all;">
			<caption>查询结果</caption>
			<thead>
				<tr>
					<th width=10%>id</th>
					<th width=20%>name</th>
					<th width=40%>rule</th>
					<th width=10%>mid</th>
					<th width=20%>operation</th>
				</tr>
			</thead>
			<tbody id="tableBody">
			</tbody>
		</table>
		<!-- 底部分页按钮 -->
		<div id="bottomTab"></div>
	</form>
	<script type='text/javascript'>
	   var map = {};
	   function bindselect(){
	  	 	 $.ajax({  
                	type: "post",
                    url: "mock/list",  
                    dataType: 'json',  
                    contentType: "application/x-www-form-urlencoded; charset=utf-8",
                    success: function (data) { 
                   	 $.each(data,function(index,element){ 
                   	 	 var key = element.title;
                   	 	 var value = element.id;
                   	  	 var content = '<option>' +element.title + '</option>';
                   	  	 map[key] = value;
                   	  	 $("#myselect").append(content); 
						});  
                    }
                });
	   	} 
               
		
		function status(object){
			var index = $(object).attr("id");
			var row = index%10;
			if(row==0){
				row=10;  //如果能被整除,则取最后一条
			}
			var id = document.getElementById("tableResult").rows[row].cells[0].innerText;
			if(object.innerText=="运行"){
				$.ajax({
					type: "post",
					url: "rule/update/status",
					data: "id=" + id+ "&status=" + 1,
					dataType: 'html',
					contentType: "application/x-www-form-urlencoded; charset=utf-8",
					success: function(result) {
						object.setAttribute("class", "btn btn-danger");
						object.innerText="停止";
					}
				});
			}else{
				$.ajax({
					type: "post",
					url: "rule/update/status",
					data: "id=" + id+ "&status=" + 0,
					dataType: 'html',
					contentType: "application/x-www-form-urlencoded; charset=utf-8",
					success: function(result) {
						object.setAttribute("class", "btn btn-info");
						object.innerText="运行";
					}
				});
			}
		}
		
		
		
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
			
		//插入规则
		function insert() {
			var index=myselect.selectedIndex ; 
			var value= myselect.options[index].value;
			var name = $('#name').val();
			var path = $('#path').val();
			var response_id = map[value];
			console.log(response_id);
			$.ajax({
				type: "post",
				url: "rule/insert",
				data: "name=" + name + "&path=" + path+"&response_id=" + response_id,
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
				url: "rule/delete",
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
            	buildTable(name,page,PAGESIZE);//默认每页最多10条
            }  
        }  

        //获取当前项目的路径
        var urlRootContext = (function () {
            var strPath = window.document.location.pathname;
            var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
            return postPath;
        })();
        
       
        //生成表格
        function buildTable(name,pageNumber,pageSize) {
        	 var url =  urlRootContext + "/rule/list.do"; //请求的网址
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
         }             	           
         $('#bottomTab').bootstrapPaginator("setOptions",newoptions); //重新设置总页面数目
         var dataList = data.dataList;
         $("#tableBody").empty();//清空表格内容
         if (dataList.length > 0 ) {
             $(dataList).each(function(index,element){//重新生成
                    var content = '<tr class=success>';
                    index = index+1;
                    content += '<td>' + element.id + '</td>';
                    content += '<td>' + element.name + '</td>';
                    content += '<td>' + element.path + '</td>';
                    content += '<td>' + element.response_id + '</td>';
                    content += '<td>';
                    if(element.status==0){
                     	content += '<button class="btn btn-info" type="button"'+'id="'+index+'"onclick=status(this)>运行</button>';
                    }else{
                    	content += '<button class="btn btn-danger" type="button"'+'id="'+index+'"onclick=status(this)>停止</button>';
                    }
                    content += '<button class="btn btn-danger" type="button"'+'id="'+index+'"onclick=del(this)>删除</button>';
                    content += '</td>';
                    content += '</tr>';
                    $("#tableBody").append(content);
             	    });
             	    } else {             	            	
             	          $("#tableBody").append('<tr><th colspan ="4"><center>查询无数据</center></th></tr>');
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