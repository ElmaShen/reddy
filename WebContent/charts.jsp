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
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/chart-master/Chart.js"></script>
<%-- <script type="text/javascript" src="<%=request.getContextPath()%>/js/chartjs-custom.js"></script> --%>
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
		queryCharts();
	});
	
	function queryCharts(){
		$.get("chartsReport.action",
		      {"shStartDate" : $("#shStartDate").val(),
	    	   "shEndDate" : $("#shEndDate").val()},
	    	   
		   	  function(data){ 
	    		 if(data.jsonStr == null){
	    			 alert("查詢期間無統計資料");
	    			 return;
	    		 }
	    		 
	    		 var colors = ["#69D2E7","#F38630","#E0E4CC","","","","","","","",
	    			 		   "","","","","","","","","","",
	    			 		   "","","","","","","","","",""]
	    		 var labels = [];
	    		 var datas = [];
	    		 var pieArray = [];
	    		 var obj = null;
	    		 var idx = 0;
				 JSON.parse(data.jsonStr, function(key, value) {
				 	if(key != "" && value != ""){
				 		obj = new Object();
				 		obj.value = value;
				 		obj.color = colors[idx];
				 		pieArray.push(obj);
				 		idx++;
				 		
				 		labels.push(key);
				 		datas.push(value);
	              	}
				 });

				var pieData = pieArray;
	  	    	var barChartData = {
					labels : labels,
		  	        datasets : [
		  	            {
		  	            	fillColor : "rgba(151,187,205,0.5)",
		  	  				strokeColor : "rgba(151,187,205,1)",
		  	            	data : datas
		  	            }
	  	        	]
	  	    	};
		  	    new Chart(document.getElementById("bar").getContext("2d")).Bar(barChartData);
		  	    new Chart(document.getElementById("pie").getContext("2d")).Pie(pieData);
	      	  },
	      	  "json"
  		);
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

      <!--main content start-->      
      <section id="main-content">
        <section class="wrapper">
		<div class="row">
			<div class="col-lg-12">
				<h3 class="page-header"><i class="icon_piechart"></i> 統計圖表</h3>
			</div>
		</div>
        <div class="row">
          <div class="col-lg-12">
              <section class="panel">
                  <header class="panel-heading">查詢條件</header>
                  <div class="panel-body">
                      <s:form id="shForm" class="form-inline" role="form">
	                      <label class="control-label">日期</label>&nbsp;
	                   	  <input type="text" id="shStartDate" /> ~ <input type="text" id="shEndDate" />&nbsp;&nbsp;
	                   	  <button type="button" class="btn btn-primary" onclick="queryCharts();">查詢</button>&nbsp;
	                   	  <button type="button" class="btn btn-primary" onclick="$('#shForm').find('input[type=text], textarea').val('');">清除</button>
                      </s:form>
                  </div>
       		  </section>
       	  </div>
        </div>
        <div class="row">
        	<div class="col-lg-12">
        		<section class="panel">
                    <div class="tab-pane" id="chartjs">
	                  <div class="row">
	                      <!-- Pie -->
	                      <div class="col-lg-6">
	                          <section class="panel">
	                              <header class="panel-heading">圓餅圖</header>
	                              <div class="panel-body text-center">
	                                  <canvas id="pie" height="300" width="400"></canvas>
	                              </div>
	                          </section>
	                      </div>                
	                      <!-- Bar -->
	                      <div class="col-lg-6">
	                          <section class="panel">
	                              <header class="panel-heading">長條圖</header>
	                              <div class="panel-body text-center">
	                                  <canvas id="bar" height="300" width="500"></canvas>
	                              </div>
	                          </section>
	                      </div>
	                  </div>
              		</div>
           	  	</section>   
        	</div>
        </div>
      </section>
    <!-- container section end -->
  </body>
</html>
