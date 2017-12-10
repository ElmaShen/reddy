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
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery-ui-1.8.21.custom.css"/>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-ui-1.10.4.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/i18n/jquery.ui.datepicker-zh-TW.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.scrollTo.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.nicescroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/scripts.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$.datepicker.setDefaults($.datepicker.regional["zh-TW"]);
	  	$.datepicker.setDefaults({ dateFormat: 'yy-mm-dd' });
		$("#shStartDate").datepicker({changeYear : true, changeMonth : true, yearRange : "-10:+1"});
		$("#shEndDate").datepicker({changeYear : true, changeMonth : true, yearRange : "-10:+1"});
	
		var sDate = "<s:property value='shStartDate' />";
		var eDate = "<s:property value='shEndDate' />";
		if(sDate != ""){
			$("#shStartDate").val(sDate);
		}
		if(eDate != ""){
			$("#shEndDate").val(eDate);
		}
	});
	
	function querySysRecords(page){
		$("#shForm").attr("action", "recordList.action");
		$("#page").val(page);
		$("#shForm").get(0).submit();
	}
</script>

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 -->
    <!--[if lt IE 9]>
    <script src="js/html5shiv.js"></script>
    <script src="js/respond.min.js"></script>
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
					<h3 class="page-header"><i class="icon_table"></i> 操作記錄</h3>
				</div>
			</div>
              <!-- page start-->
              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <header class="panel-heading">查詢條件</header>
                          <div class="panel-body">
                              <s:form id="shForm" class="form-inline" role="form" namespace="/" action="recordList" method="post">
                              		<s:hidden name="page" id="page" />
                            		<label class="control-label">姓名</label>&nbsp;
                          			<s:textfield id="shName" name="shName" theme="simple" />&nbsp;&nbsp;
                            		<label class="control-label">日期</label>&nbsp;
                            		<input type="text" id="shStartDate" name="shStartDate" /> ~ <input type="text" id="shEndDate" name="shEndDate" />&nbsp;&nbsp;
                            		<button type="button" class="btn btn-primary" onclick="querySysRecords(0);">查詢</button>&nbsp;
                            		<button type="button" class="btn btn-primary" onclick="$('#shForm').find('input[type=text], textarea').val('');">清除</button>
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
                                  <th>操作記錄</th>
                                  <th>日期</th>
                                  <th>備註</th>
                              </tr>
                              </thead>
                              <tbody>
	                              <s:iterator value="pageBean.list" status="i">
	                              <tr>
	                                  <td width="6%"><s:property value="(pageBean.currentPage-1)*10+(#i.index+1)"/></td>
	                                  <td width="10%"><s:property value="account"/></td>
	                                  <td width="14%"><s:property value="name"/></td>
	                                  <td width="15%"><s:property value="action"/></td>
	                                  <td width="15%"><s:date name="createDate" format="yyyy-MM-dd HH:mm"/></td>
	                                  <td width="30%"><s:property value="remark"/></td>
	                              </tr>
	                              </s:iterator>
                              </tbody>
                          </table>
                          <div class="panel-body">
		                      <div class="text-center">
                          	  <s:if test="%{pageBean.list.size() > 0}">
		                          <ul class="pagination">
								           目前顯示第
							        <select name="currentPage" id="currentPage" onChange="querySysRecords(this.value)">
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
  </body>
</html>
