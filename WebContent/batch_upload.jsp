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
<%-- <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/process/css/bootstrap.min.css"/> --%>
<style type="text/css">
  .progress{
	  height: 25px;
	  background: #262626;
	  padding: 5px;
	  overflow: visible;
	  border-radius: 20px;
	  border-top: 1px solid #000;
	  border-bottom: 1px solid #7992a8;
	  margin-top: 50px;
  }
  .progress .progress-bar{
	  border-radius: 20px;
	  position: relative;
	  animation: animate-positive 2s;
  }
  .progress .progress-value{
	  display: block;
	  padding: 3px 7px;
	  font-size: 13px;
	  color: #fff;
	  border-radius: 4px;
	  background: #191919;
	  border: 1px solid #000;
	  position: absolute;
	  top: -40px;
	  right: -10px;
  }
  .progress .progress-value:after{
	  content: "";
	  border-top: 10px solid #191919;
	  border-left: 10px solid transparent;
	  border-right: 10px solid transparent;
	  position: absolute;
	  bottom: -6px;
	  left: 26%;
  }
  .progress-bar.active{
	  animation: reverse progress-bar-stripes 0.40s linear infinite, animate-positive 2s;
  }
  @-webkit-keyframes animate-positive{
	  0% { width: 0; }
  }
  @keyframes animate-positive{
	  0% { width: 0; }
  }
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.scrollTo.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.nicescroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/scripts.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.blockUI.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('#tabs').tab();
		$("#bar").hide();
		
		$('input[name=batchType]').click(function() { 
			if($(this).val() == "M"){
				$("#section").show();
			}else{
				$("#section").hide();
			}
		}); 
		
		//整批轉檔
		$('#batch_btn').click(function() { 
			if($("#batchPath").val() == ""){
				alert("請填入資料夾路徑");
				return;
			}
			$.blockUI({ message: $('#bu_question'), css: { width: '275px' } }); 
		}); 
		$('#yes').click(function() { 
		    // update the block message 
// 		    $.blockUI({ message: "<h4>檔案上傳中...</h4>" }); 
		    $.blockUI({ message: $('#bar') }); 
		    $("#totTime").text("");
			$("#count").text("");
			$("#success").text("");
			$("#fail").text("");
			$('#pTbl tbody').html("");
			$('#fTbl tbody').html("");
			
			var type = $('input[name=batchType]:checked').val();
			if($("#batchPath").val() == ""){
				alert("請填入資料夾路徑");
				return;
			}
			$.get("startBatch.action",
			      {"batchType" : type,
		    	   "batchPath" : $("#batchPath").val()},
			   	  function(data){ 
			    	  alert(data.message);
			    	  if(data.success == "Y"){
			    		  $("#totTime").text(data.totTime);
			    		  $("#count").text(data.slist.length + data.flist.length+"個");
			    		  $("#success").text(data.slist.length+"個");
			    		  $("#fail").text(data.flist.length+"個");
			    		  //成功
			    		  if(data.slist.length != 0){
			    			  for(var i=0; i<data.slist.length; i++){
			    				  var row = "";
					    		  row += "<tr>";
					    		  row += '<td>'+(i+1)+'</td>';
					    		  row += '<td>'+data.slist[i]+'</td>';
					    		  row += "</tr>";
			    				  $('#pTbl tbody').append(row);
			    			  }
			    		  }
			    		  //失敗
			    		  if(data.flist.length != 0){
			    			  for(var j=0; j<data.flist.length; j++){
			    				  var row = "";
					    		  row += "<tr>";
					    		  row += '<td>'+(j+1)+'</td>';
					    		  row += '<td>'+data.flist[j].split("@#@")[0]+'</td>';
					    		  row += '<td>'+data.flist[j].split("@#@")[1]+'</td>';
					    		  row += "</tr>";
			    				  $('#fTbl tbody').append(row);
			    			  }
			    		  }
			    	  }
			    	  $.unblockUI(); 
			      },
			      "json"
		  	);
		}); 
		$('#no').click(function() { 
		    $.unblockUI(); 
		    return false; 
		}); 
		
		//整批刪除
		$('#delete_btn').click(function() { 
			if($("#voices").val() == ""){
				alert("請選擇「產業類別」");
				return;
			}
			$.blockUI({ message: $('#del_question'), css: { width: '350px' } }); 
		}); 
		$('#d_yes').click(function() { 
			$.get("deleteAll.action",
			      {"voices" : $("#voices").val()},
			   	  function(data){ 
			    	  alert(data.message);
			    	  $.unblockUI(); 
			      },
			      "json"
		  	);
		}); 
		$('#d_no').click(function() { 
		    $.unblockUI(); 
		    return false; 
		});
	});
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
            <a href="index.html" class="logo">瑞迪 <span class="lite">廣告資訊服務平台</span></a>
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
				<h3 class="page-header"><i class="icon_link_alt"></i> 批次轉檔</h3>
			</div>
		</div>
        <div class="row">
          <div class="col-lg-12">
              <section class="panel">
                  <div class="panel-body">
				    <form id="bForm" class="form-inline" role="form">
				    	轉檔類型:　<input type="radio" name="batchType" value="M" checked /> 音檔
			               	    　   &nbsp;<input type="radio" name="batchType" value="C" /> 個案<br>
						資料夾路徑:　<s:textfield id="batchPath" theme="simple" size="50%" />&nbsp;
                        <button type="button" class="btn btn-primary" id="batch_btn">整批轉檔</button><br>
                        　　　　　　<font color="blue">(路徑請勿以"/"結尾)</font><br>
                        <span id="section">
						產業類別:　<s:select id="voices" headerKey="" headerValue="-" theme="simple" list="voiceCombo()" />&nbsp;
						<button type="button" class="btn btn-danger" id="delete_btn">整批刪除</button><br>
						</span>
					</form>
					
					歷時 : <font color="blue"><label id="totTime">&nbsp;</label></font><br>
					總筆數 : <font color="blue"><label id="count">&nbsp;</label></font><br>
					成功 : <font color="blue"><label id="success">&nbsp;</label></font><br>
					失敗 : <font color="red"><label id="fail">&nbsp;</label></font><br>
				    <div id="content">
					    <ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
					        <li><a href="#red" data-toggle="tab">成功</a></li>
					        <li class="active"><a href="#orange" data-toggle="tab">失敗</a></li>
					    </ul>
					    <div id="my-tab-content" class="tab-content">
					        <div class="tab-pane" id="red">
					            <table class="table table-hover" id="pTbl">
			                       <thead class="panel-heading">
				                       <tr>
					                       <th>#</th>
					                       <th>檔案名稱</th>
				                       </tr>
			                       </thead>
			                       <tbody></tbody>
			                    </table>
					        </div>
					        <div class="tab-pane active" id="orange">
					            <table class="table table-hover" id="fTbl">
			                       <thead class="panel-heading">
				                       <tr>
					                       <th>#</th>
					                       <th>檔案名稱</th>
					                       <th>錯誤訊息</th>
				                       </tr>
			                       </thead>
			                       <tbody></tbody>
			                    </table>
					        </div>
					    </div>
					</div>
                 </div>
              </section>
          </div>
        </div>
      </section>
 	</section>
    <!-- container section end -->
    <a style="display:scroll;position:fixed;bottom:0px;right:5px;" href="#" title="" onFocus="if(this.blur)this.blur()">
		<img alt='' border='0' onmouseover="this.src='img/Top_medium.png'" src="img/Top_small.png" onmouseout="this.src='img/Top_small.png'" />
	</a>
  </body>
</html>

<div id="bu_question" style="display:none; cursor: default;"> 
	<h4>是否開始執行轉檔?</h4> 
	<input type="button" id="yes" value="Yes" /> 
	<input type="button" id="no" value="No" />
	<p>
</div> 
<div id="del_question" style="display:none; cursor: default;"> 
	<h4>資料及檔案會一併刪除，是否繼續執行?</h4> 
	<input type="button" id="d_yes" value="Yes" /> 
	<input type="button" id="d_no" value="No" />
	<p>
</div> 

<div id="bar" class="progress">
	<div class="progress-bar progress-bar-info progress-bar-striped active" style="width: 100%;">
		<div class="progress-value">100%</div>
	</div>
</div>