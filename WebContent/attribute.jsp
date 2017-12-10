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
<style type="text/css">
	input[type=text], select {
	    padding: 6px 10px;
	    margin: 8px 0;
	    display: inline-block;
	    border: 1px solid #ccc;
	    border-radius: 4px;
	    box-sizing: border-box;
	}
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.scrollTo.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.nicescroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/scripts.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#shForm').find('input[type=text], textarea').val('');
		var message = "<s:property value='message' />";
		var success = "<s:property value='success' />";
		if(message != ""){
			alert(message);
		}
		if(success == "Y"){
			location.href = "<s:url namespace='/' action='attributeList' />";
		}
	});
	
	function queryAttributeList(page){
		$("#shForm").attr("action", "attributeList.action");
		$("#page").val(page);
		$("#shForm").get(0).submit();
	}
	
	function setSecType(val){
		if(val != "" && val == "category"){
			$("#secType").css("display", "inline");
		}else{
			$("#secType").css("display", "none");
		}
	}
	
	function editAttribute(id){
		$.get("queryAttribute.action",
		      {"id" : id},
		   	  function(data){ 
		    	 $("#attrId").val(data.attribute.id);
		    	 $("#type").val(data.attribute.type);
		    	 $("#attrKey").val(data.attribute.attrKey);
		    	 $("#attrName").val(data.attribute.attrName);
		    	 $("input[name=isuse][value='"+data.attribute.isuse+"']").attr('checked',true); 
		    	 if(data.attribute.parentKey != "" && data.attribute.parentKey != "0"){
		    		 $("#secType").css("display", "inline");
		    		 $("#parentKey").val(data.attribute.parentKey);
		    	 }else{
		    		 $("#secType").css("display", "none");
		    		 $("#parentKey").val("");
		    	 }
		      },
		      "json"
	  	);
	}
	
	function submitForm(){
		var msg = "";
		if($("#type").val() == ""){
			msg += "屬性\n"
		}
		if($("#attrKey").val() == ""){
			msg += "代碼\n"
		}
		if($("#attrName").val() == ""){
			msg += "名稱\n"
		}
		if($("#type").val() == "category" && isNaN(parseInt($("#parentKey").val()))){
			msg += "產業類別\n"
		}
		
		if(msg != ""){
			alert("請填入以下資訊:\n" + msg);
			return;
		}

		$.post(
			"<s:url namespace='/' action='editAttribute' />",
			{'attribute.id': $("#attrId").val(),
			 'attribute.type': $("#type").val(),
			 'attribute.attrKey': $("#attrKey").val(),
			 'attribute.attrName': $("#attrName").val(),
			 'attribute.isuse': $('input[name=isuse]:checked', '#sform').val(),
			 'attribute.parentKey': $("#parentKey").val()},
		    function(data){
				 if(data.success == "N"){
					 alert("錯誤訊息：" + data.message);
					 return;
				 }
				 alert(data.message);
				 location.href = "<s:url namespace='/' action='attributeList' />?shType="+$("#type").val();
	    	}	
		);
	}
	
	function deleteAttribute(id){
		if(confirm("確定刪除檔案!")){
			location.href="deleteAttribute.action?id="+id;
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
					<h3 class="page-header"><i class="icon_table"></i> 屬性設定</h3>
					
					<!-- 權限 -->
	                <s:iterator value="#session.LOGIN_USER.authority" status="stat">
	                	<s:if test="%{key eq 'F07'}">
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
                              <s:form id="shForm" class="form-inline" role="form" namespace="/" action="attributeList" method="post">
                              	  <s:hidden name="page" id="page" />
                              	  <s:hidden name="shTypeName" id="shTypeName" />
                       			  <label class="control-label">屬性</label>&nbsp;
                                  <s:select id="shType" name="shType" headerKey="" headerValue="-All-" theme="simple" list="attrCombo()" onchange="$('#shTypeName').val(this.options[this.selectedIndex].text);"/>&nbsp;&nbsp;
                       			  <label class="control-label">名稱</label>&nbsp;
                                  <s:textfield id="shAttrName" name="shAttrName" theme="simple" />&nbsp;&nbsp;
                                  	<button type="button" class="btn btn-primary" onclick="queryAttributeList(0);">查詢</button>&nbsp;
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
                                  <th>屬性</th>
                                  <th>代碼</th>
                                  <th>名稱</th>
                                  <th>啟用</th>
                                  <th>Action</th>
                              </tr>
                              </thead>
                              <tbody>
	                              <s:iterator value="pageBean.list" status="i">
	                              <tr>
	                                  <td><s:property value="(pageBean.currentPage-1)*10+(#i.index+1)"/></td>
	                                  <td><s:property value="typeName"/></td>
	                                  <td><s:property value="attrKey"/></td>
	                                  <td><s:property value="attrName"/></td>
	                                  <td><s:if test='%{isuse eq "Y"}'>是</s:if><s:else>否</s:else></td>
	                                  <td>
									  <div class="btn-group">
	                                  	<s:if test='%{#added eq "Y"}'>
	                                      <a class="btn btn-primary" href="#edit_dg" data-toggle="modal" data-backdrop="false" onclick="editAttribute(<s:property value="id" />);">
	                                      	<i class="icon_pencil-edit" title="編輯"></i>
	                                      </a>
	                                    </s:if>
	                                    <s:if test='%{#deleted eq "Y"}'>
	                                  	  <a class="btn btn-danger" href="#" onclick="deleteAttribute(<s:property value="id" />);">
	                                  	  	<i class="icon_close_alt2" title="刪除"></i>
	                                  	  </a>
	                                  	</s:if>
	                                  </div>
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
								        <select name="currentPage" id="currentPage" onChange="queryAttributeList(this.value)">
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
			    <h4 class="modal-title">屬性設定</h4>
			</div>
			<div class="modal-body">
			    <s:form id="sform">
			    	<s:hidden id="attrId" name="attribute.id"/>
		        	<font color="red">*</font>屬性:　<s:select id="type" headerKey="" headerValue="-All-" theme="simple" 
		        													list="attrCombo()" onchange="setSecType(this.value);" /><br/>
		        	<font color="red">*</font>代碼:　<s:textfield id="attrKey" theme="simple" /><br/>
		        	<font color="red">*</font>名稱:　<s:textfield id="attrName" theme="simple" /><br/>
		        	啟用:　<input type="radio" name="isuse" value="Y" checked /> 是
			               <input type="radio" name="isuse" value="N" /> 否
			        <br>
		        	<div id="secType" style="display:none;">
		        		產業類別:　<s:select id="parentKey" headerKey="" headerValue="-All-" theme="simple" list="sectionCombo()" />&nbsp;
		        	</div>
			    </s:form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" onclick="submitForm();">儲存</button>
			    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
			</div>
		</div>
	</div>
</div>
  </body>
</html>
