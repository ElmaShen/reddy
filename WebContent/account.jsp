<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE>
<html lang="zh">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Radioad | Music Platform</title>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap-theme.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/elegant-icons-style.css"/>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/font-awesome.min.css"/>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/style-responsive.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.scrollTo.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.nicescroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/scripts.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		var message = "<s:property value='message' />";
		var success = "<s:property value='success' />";
		if(message != ""){
			alert(message);
		}
		if(success == "Y"){
			location.href = "<s:url namespace='/' action='accountList' />";
		}
		
		var fn = "", newRow = "";
		<s:iterator value="allFuncs" status="f">
			var no = '<s:property value="fno"/>';
			var fn = '<s:property value="fname"/>';
			newRow = ""
			newRow += "<tr>";
			newRow += '<td><input type="checkbox" id="fun_<s:property value="%{#f.index+1}"/>" /></td>';
			newRow += '<td>'+no+'.'+fn;
			if(fn.indexOf("音檔") != -1){
				newRow += '<a href="#sec_dg" data-toggle="modal" data-backdrop="false" onclick="querySections(1);"><i class="icon_clipboard" title="產業設定"></i></a>';
			}
			if(fn.indexOf("個案") != -1){
				newRow += '<a href="#sec_dg" data-toggle="modal" data-backdrop="false" onclick="querySections(2);"><i class="icon_clipboard" title="產業設定"></i></a>';
			}
			newRow += '</td>';
			
			if(fn == '統計圖表' || fn == '操作記錄' || fn == '批次轉檔'){
				newRow += '<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>';
			}else{
				newRow += '<td><input type="checkbox" id="add_<s:property value="%{#f.index+1}"/>" /></td>';
				newRow += '<td><input type="checkbox" id="delete_<s:property value="%{#f.index+1}"/>" /></td>';
				
				if(fn.indexOf("音檔") != -1 || fn.indexOf("個案") != -1 || fn.indexOf("照片") != -1){
					newRow += '<td><input type="checkbox" id="down_<s:property value="%{#f.index+1}"/>" /></td>';
				}else{
					newRow += '<td>&nbsp;</td>';
				}
			}
			newRow += "</tr>";
			$("#tbl tr:last").after(newRow);
		</s:iterator>
	});
	
	function querySections(type){
		$.get("querySections.action",
		      {"secType" : type},
		   	  function(data){ 
		    	  $('#sTbl tbody').html("");
		    	  var key = (type=="1")?"voice_":"sec_";
				  //產業設定 全選	
				  $("#checkAll").click(function() {
				  	if($("#checkAll").prop("checked")) {
				  		$('input:checkbox.'+key).prop('checked', true);
				    } else {
				    	$('input:checkbox.'+key).prop('checked', false);
				   	}
				  });
			    	
		    	  for(var i=0; i<data.sections.length; i++){
		    		  var row = "";
		    		  row += "<tr>";
		    		  row += '<td><input type="checkbox" id="'+ key + data.sections[i].split(".")[0] +'" class="'+key+'" /></td>';
		    		  row += '<td>'+data.sections[i]+'</td>';
		    		  row += "</tr>";
		    		  $('#sTbl tbody').append(row);
		    	  }
		    	  
		    	  var ary = null;
		    	  if(type=="1" && $("#voice").val() != ""){
		    		  	ary = $("#voice").val().split(",");
	    		  		for(var i=0; i<ary.length-1; i++){
	    		  			$("#voice_"+ary[i]).prop('checked', true);
	    		  		}
		    	  }
				  if(type=="2" && $("#section").val() != ""){
					  	ary = $("#section").val().split(",");
	    		  		for(var i=0; i<ary.length-1; i++){
	    		  			$("#sec_"+ary[i]).prop('checked', true);
	    		  		}
		    	  }
				  
				  //全選是否附值
				  if(ary != null && data.sections.length == ary.length-1){
					  $("#checkAll").prop("checked", true);
				  }else{
					  $("#checkAll").prop("checked", false);
				  }
		      },
		      "json"
	  	);
	}
	
	function setSections(){
		var type = "", v = "";
		$('#sTbl tr').each(function() {
			var oid = $(this).children().eq(0).children().attr("id");
			type = oid.split("_")[0];
			if($('#'+oid).prop("checked") == true){
				var k = $(this).children().eq(1).text().split(".")[0]
				v += k + ",";
			}
		});
		
		if(type == "voice"){
			$("#voice").val(v);
		}else{
			$("#section").val(v);
		}
	}
	
	function queryAccountList(page){
		$("#shForm").attr("action", "accountList.action");
		$("#page").val(page);
		$("#shForm").get(0).submit();
	}
	
	function editAccount(id){
		$.get("queryAccount.action",
		      {"id" : id},
		   	  function(data){ 
		    	 $("#accId").val(data.account.id);
		    	 $("#account").val(data.account.account);
		    	 $("#name").val(data.account.name);
		    	 $("#password").val(data.account.password);
		    	 $("input[name=isuse][value='"+data.account.isuse+"']").attr('checked',true); 
				 
		    	 if(data.authJson != ""){
		    		 var obj = $.parseJSON(data.authJson);
		    		 for(var i=0; i<obj.length; i++){
		    			 if(obj[i].fno == "F01"){
		    				 $("#voice").val(obj[i].voice);
		    			 }
						 if(obj[i].fno == "F02"){
							 $("#section").val(obj[i].section);
		    			 }
						 
		    			 var idx = parseInt(obj[i].fno.substring(1));
		    			 $("#fun_"+idx).prop('checked', true);
		    			 if(obj[i].added == "Y"){
		    				 $("#add_"+idx).prop('checked', true);
		    			 }
		    			 if(obj[i].deleted == "Y"){
		    				 $("#delete_"+idx).prop('checked', true);
		    			 }
		    			 if(obj[i].download == "Y"){
		    				 $("#down_"+idx).prop('checked', true);
		    			 }
		    		 }
		    	 }
		      },
		      "json"
	  	);
	}
	
	function submitForm(){
		var account = $("#account").val();
		var name = $("#name").val();
		var password = $("#password").val();
		var isuse = $('input[name=isuse]:checked', '#sform').val();
		
		//權限
		var ary = new Array();
		$('#tbl tr').each(function() {
			if($(this).index() != 0){
				var oid = $(this).children().eq(0).children().attr("id");
				if($('#'+oid).prop("checked") == true){
					var obj = new Object();
					obj.fno = $(this).children().eq(1).text().split(".")[0];
					obj.added = $('#add_'+$(this).index()).prop("checked") == true ? "Y" : "N";
					obj.deleted = $('#delete_'+$(this).index()).prop("checked") == true ? "Y" : "N";
					obj.download = $('#down_'+$(this).index()).prop("checked") == true ? "Y" : "N";
					ary.push(obj);
				}
			}
		});

		var msg = "";
		if(account == ""){
			msg += "帳號\n"
		}
		if(name == ""){
			msg += "姓名\n"
		}
		if(password == ""){
			msg += "密碼\n"
		}
		
		if(msg != ""){
			alert("請填入以下資訊:\n" + msg);
			return;
		}

		$.post(
			"<s:url namespace='/' action='editAccount' />",
			{'account.id': $("#accId").val(),
			 'account.account': account,
			 'account.name': name,
			 'account.password': password,
			 'account.isuse': isuse,
			 'authVoice': $("#voice").val(),
			 'authSec': $("#section").val(),
			 'authJson': JSON.stringify(ary)},
		    function(data){
				 if(data.success == "N"){
					 alert("錯誤訊息：" + data.message);
					 return;
				 }
				 alert(data.message);
				 location.href = "<s:url namespace='/' action='accountList' />";
	    	}	
		);
	}
	
	function deleteAccount(id){
		if(confirm("確定刪除檔案!")){
			location.href="deleteAccount.action?id="+id;
		}
	}
</script>
<!-- HTML5 shim and Respond.js IE8 support of HTML5 -->
<!--[if lt IE 9]>
  <script src="js/html5shiv.js"></script>
  <script src="js/respond.min.js"></script>
  <script src="js/lte-ie7.js"></script>
<![endif]-->
</head>

  <body>
  <!-- container section start -->
  <section id="container" class="">
      <!--header start-->
      
      <header class="header dark-bg">
            <div class="toggle-nav">
                <div class="icon-reorder tooltips" data-original-title="Toggle Navigation" data-placement="bottom"><i class="icon_menu"></i></div>
            </div>

            <!--logo start-->
            <a href="index.action" class="logo">瑞迪 <span class="lite">廣告資訊服務平台</span></a>
            <!--logo end-->

            <div class="top-nav notification-row">                
                <ul class="nav pull-right top-menu">
                    <span class="username">
                    	登入帳號:<s:property value="#session.LOGIN_USER.account"/><br>
                    	登入時間:<s:date format="MM/dd HH:mm" name="#session.LOGIN_USER.loginDate"/>
                    </span>
                </ul>
            </div>
      </header>      
      <!--header end-->

      <!--sidebar start-->
      <aside>
          <div id="sidebar"  class="nav-collapse ">
              <!-- sidebar menu start-->
              <ul class="sidebar-menu">  
              	  <s:iterator value="funcs" status="i">
	           	  	<li class="">
	                   <a class="" href="<%=request.getContextPath()%><s:property value="action"/>">
	                       <i class="<s:property value="icon"/>"></i>
	                       <span><s:property value="fname"/></span>
	                   </a>
	               	</li>  
              	  </s:iterator>
                  <li>         
                     <a class="" href="<s:url namespace="/" action="logout" />">
                         <i class="icon_refresh"></i>
                         <span>登出</span>
                     </a>
                  </li>
              </ul>
              <!-- sidebar menu end-->
          </div>
      </aside>
      <!--sidebar end-->

      <section id="main-content">
          <section class="wrapper">
		  <div class="row">
				<div class="col-lg-12">
					<h3 class="page-header"><i class="icon_group"></i> 帳號管理</h3>
					<!-- 權限 -->
	                <s:iterator value="#session.LOGIN_USER.authority" status="stat">
	                	<s:if test='%{key eq "F05"}'>
	                		<s:set var="added" value="value.added" />
			                <s:set var="deleted" value="value.deleted" />
			                <s:set var="download" value="value.download" />
	                	</s:if>
	                </s:iterator>
				</div>
			</div>
              <!-- page start-->
              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <header class="panel-heading">查詢條件</header>
                          <div class="panel-body">
                              <s:form id="shForm" class="form-inline" role="form" namespace="/" action="accountList" method="post">
                              	  <s:hidden name="page" id="page" />
                              	　<label class="control-label">帳號</label>&nbsp;
                              	  <s:textfield id="shAccount" name="shAccount" theme="simple" />
                               	　<label class="control-label">姓名</label>&nbsp;
                                  <s:textfield id="shName" name="shName" theme="simple" />
                       			　<label class="control-label">啟用</label>&nbsp;
                                  <s:select id="shIsuse" name="shIsuse" headerKey="" headerValue="-All-" theme="simple" list="#{'Y':'是', 'N':'否'}"  />&nbsp;&nbsp;
                                  	<button type="button" class="btn btn-primary" onclick="queryAccountList(0);">查詢</button>&nbsp;
                                  <s:if test='%{#added eq "Y"}'>
                                  	<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#edit_dg" data-backdrop="false">新增</button>
                                  </s:if>
                              </s:form>
                          </div>
                      </section>
                  </div>
              </div>
              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <table class="table table-hover">
                              <thead class="panel-heading">
                              <tr>
                                  <th>#</th>
                                  <th>帳號</th>
                                  <th>姓名</th>
                                  <th>啟用</th>
                                  <th>Action</th>
                              </tr>
                              </thead>
                              <tbody>
                              	  <s:iterator value="pageBean.list" status="i">
	                              <tr>
	                                  <td><s:property value="(pageBean.currentPage-1)*10+(#i.index+1)"/></td>
	                                  <td><s:property value="account"/></td>
	                                  <td><s:property value="name"/></td>
	                                  <td><s:if test='%{isuse eq "Y"}'>是</s:if><s:else>否</s:else></td>
	                                  <td>
	                                  <s:if test='%{#added eq "Y"}'>
	                                  	<div class="btn-group">
	                                      <a class="btn btn-primary" href="#edit_dg" data-toggle="modal" data-backdrop="false" onclick="editAccount(<s:property value="id" />);"><i class="icon_pencil-edit" title="編輯"></i></a>
	                                  	</div>
	                                  	<s:if test='%{#deleted eq "Y"}'>
	                                  	  <a class="btn btn-danger" href="#" onclick="deleteAccount(<s:property value="id" />);"><i class="icon_close_alt2" title="刪除"></i></a>
	                                  	</s:if>
	                                  </s:if>
	                                  </td>
	                              </tr>
	                              </s:iterator>
                              </tbody>
                          </table>
                          <div class="panel-body">
		                      <div class="text-center">
		                          <s:if test="%{pageBean.list.size() > 0}">
			                          <ul class="pagination">
									           目前顯示第
								        <select name="currentPage" id="currentPage" onChange="queryAccountList(this.value);">
								           <s:if test="pageBean neq null">
								           		<s:iterator begin="1" end="%{pageBean.totalPage}" status="i">          
									                <option value="<s:property value='#i.count' />" <s:if test="%{#i.count == pageBean.currentPage}">selected="selected"</s:if>><s:property value='#i.count' /></option>
									            </s:iterator>
								           </s:if>
								        </select>&nbsp;             
								        /&nbsp; 共<s:property value="pageBean.totalPage"/>頁，<s:property value="pageBean.totalCount"/>筆資料 
							          </ul>
	                          	  </s:if>
	                          	  <s:else><font color="red">查無資料</font></s:else>
		                      </div>
		                  </div>
                      </section>
                  </div>
              </div>
              <!-- page end-->
          </section>
      </section>
  </section>
  <!-- container section end -->
  <div class="modal fade" id="edit_dg" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
				    <h4 class="modal-title">帳號設定</h4>
				</div>
				<div class="modal-body">
				    <form id="sform">
				    	<s:hidden id="accId" name="accId"></s:hidden>
				    	<s:hidden id="voice" theme="simple" />
				    	<s:hidden id="section" theme="simple" />
			        	<font color="red">*</font>帳號:　<s:textfield id="account" theme="simple" /><br/>
			        	<font color="red">*</font>姓名:　<s:textfield id="name" theme="simple" /><br/>
			        	<font color="red">*</font>密碼:　<s:textfield id="password" theme="simple" /><br/>
			        	啟用:　<input type="radio" name="isuse" value="Y" checked /> 是
			        		 　 <input type="radio" name="isuse" value="N" /> 否	
			        	<br/><br/>
			        	<table class="table table-hover" id="tbl">
                            <thead class="panel-heading">
	                            <tr>
	                                <th>&nbsp;</th>
	                                <th>系統功能</th>
	                                <th>新增</th>
	                                <th>刪除</th>
	                                <th>下載</th>
	                            </tr>
                            </thead>
                       </table>
				    </form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" onclick="submitForm();">儲存</button>
				    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="sec_dg" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
				    <h4 class="modal-title">產業設定</h4>
				</div>
				<div class="modal-body">
					<input type="checkbox" id="checkAll" /><font color="red">全選</font>
		        	<table class="table table-hover" id="sTbl">
		        		<tbody></tbody>
		        	</table>
				</div>
				<div class="modal-footer">
				    <button type="button" class="btn btn-default" data-dismiss="modal" onclick="setSections();">關閉</button>
				</div>
			</div>
		</div>
	</div>
  </body>
</html>
