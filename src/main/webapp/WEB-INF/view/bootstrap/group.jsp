<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<title>Mock Test For BeeCloud</title>
	<link href="<%=request.getContextPath()%>/static/js/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/static/css/simple-sidebar.css" rel="stylesheet">
	<link href="<%=request.getContextPath()%>/static/css/multiple-select.css" rel="stylesheet">
	<script src="<%=request.getContextPath()%>/static/js/jQuery/jquery-2.1.4.min.js"></script>
	<script src="<%=request.getContextPath()%>/static/js/bootstrap/js/bootstrap.min.js"></script>
	<script src="<%=request.getContextPath()%>/static/js/bootstrap/js/bootstrap-paginator.min.js"></script>
	<script src="<%=request.getContextPath()%>/static/js/bootstrap/js/multiple-select.js"></script>
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

			 <li class="sidebar-brand">
				 <a href="list">
					 HttpMock
				 </a>
			 </li>

			 <li class="sidebar-brand">
				 <a href="#">
					 TboxMock
				 </a>
			 </li>
				 <ul>
					 <li>
						 <a href="tbox">Tbox管理</a>
					 </li>
					 <li>
						 <a href="#">Tbox测试套</a>
					 </li>
				 </ul>
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

					<div class="control-group"><label class="control-label"  for="description">描述</label>
						<div class="controls">
							<textarea id="description" style="height:200px;width:300px;word-break:break-all; word-wrap:break-all;"></textarea>
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

					 <label class="control-label"  for="message">选择用例名称</label>
					 <select id="select_tbox_list" class="form-control" style="width:300px;" multiple="multiple"></select>

					 <label class="control-label"  for="message">描述</label>
					 <textarea id="message" class="info" style="height:100px;width:300px;word-break:break-all; word-wrap:break-all;"></textarea>

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



	<form id="form1">
		<table class="table table-hover table-bordered table-striped" id = 'tableResult'  style="word-break:break-all; word-wrap:break-all;">
			<caption>查询结果</caption>
			<thead>
				<tr>
					<th width=10%>id</th>
					<th width=20%>name</th>
					<th width=50%>description</th>
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
		var tbox_list = null;
		jQuery.fn.removeSelected = function() {
			this.val("");
		};

		//页面加载完成时初始化车辆下拉框
		$(document).ready(function(){
			$.ajax({
				type: "post",
				url: "tbox/list/name",
				dataType: 'html',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function(result) {
					tbox_list = result.replace("[","").replace("]","").split(",");
				}
			});
		});

		//编辑框关闭时,清空下拉选项框
		$('#editModal').on('hide.bs.modal', function () {
			$("#select_tbox_list").empty();//删除所有的options
		});

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
			//向模态框中传值
			$('#index').val(id);
			$('#title').val(title);
			$('#message').val(message);
			$.each(tbox_list,function(index,value){
				if(""!=value){
					var content = '<option>' +value+ '</option>';
					$("#select_tbox_list").append(content);
				}
			});
			$.ajax({
				type: "post",
				url: "group/get",
				data: "id=" + id,
				dataType: 'html',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function(result) {
					console.log(result);
					var obj = JSON.parse(result);
					var list = obj['tboxs'].split("|");
					console.log(list);
					if(list.length>0){
						//http://wenzhixin.net.cn/p/multiple-select/docs/index.html?locale=zh_CN#methods
						$("#select_tbox_list").multipleSelect('setSelects',list);
					}
				}
			});
			$('#editModal').modal('show');
			$('#select_tbox_list').multipleSelect();
		}

		//获取下拉框选中的字段并合并
		function combile() {
			var obj = document.getElementById("select_tbox_list");
			var combile = "";
			for(var i =0 ;i<obj.options.length;i++){
				if(obj.options[i].selected){
					combile+=obj.options[i].value+"|";
				}
			}
			combile = combile.substr(0,combile.length-1);
			return combile;
		}

		//插入规则
		function insert() {
			var name = $('#name').val();
			var description = $('#description').val();
			$.ajax({
				type: "post",
				url: "group/insert",
				data: "name=" + name + "&description=" + description,
				dataType: 'html',
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
			var description = $('#message').val();
			var combile_str = combile();
			console.log(combile_str);
			$.ajax({
				type: "post",
				url: "group/update",
				data: "id=" + id +"&name=" + name + "&description=" + description+ "&tboxs="+combile_str,
				dataType: 'html',
				contentType: "application/x-www-form-urlencoded; charset=utf-8",
				success: function(result) {
					$('#editModal').modal('hide');
					$('#queryButton').click();
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
				url: "group/delete",
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
        };

        //获取当前项目的路径
        var urlRootContext = (function () {
            var strPath = window.document.location.pathname;
            var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
            return postPath;
        })();
        
       
        //生成表格
        function buildTable(name,pageNumber,pageSize) {
        	 var url =  urlRootContext + "/group/list.do"; //请求的地址
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
                    content += '<td>' + element.description + '</td>';
                    content += '<td>';
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