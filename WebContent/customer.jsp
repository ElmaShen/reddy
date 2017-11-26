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
			location.href = "<s:url namespace='/' action='customerList' />";
		}
		
		//關閉reset form
		$('.modal').on('hidden.bs.modal', function (e) {
			$(this).find("input,textarea,select").val('').end()
			  	   .find("input[type=checkbox], input[type=radio]").prop("checked", "").end();
			//移除File addRow
			var tblRow = $('#tbl tr').length;
			if(tblRow > 1){
				$("#tbl tr").remove();
				
				var newRow  = '<tr>'; 
					newRow += '<td><input type="file" id="upload" name="upload" /></td>';
					newRow += '</tr>';
				$("#tbl").append(newRow);
			}
		})
		
		
		var sec = "<s:property value='shSection' />";
		var cat = "<s:property value='shCategory' />";
		if(sec != ""){
			changeSelect(document.getElementById("shSection"), 'shCategory');
	   	 	setTimeout(function() {
	 			$("#shCategory").val(cat);
			}, 120);
		}
	});
	
	function changeSelect(o, objId){
		var val = o.value;
		var obj = document.getElementById(objId);
		obj.options.length = 0;
		obj.options.add(new Option("-All-",""));
		if(val == ""){
			return;
		}
		
		$.post(
			"<s:url namespace='/' action='categoryCombo' />",
		    {'parent' : val},
		    function(jsonStr){
		    	 //alert(jsonStr);
		    	 var list = JSON.parse(jsonStr);
				 JSON.parse(jsonStr, function(key, value) {
				 	if(key != "" && value != ""){
						var t = $.trim(value);
						var v = $.trim(key);
						var option = new Option(t,v);        
						obj.options.add(option);
	              	}
				 });
		    }
		);
	}
	
	function addFile(id, tbl){
		var n = id+"upload";
		var newRow  = '<tr>'; 
		if(tbl == "cTbl"){	//素材
			newRow += '<td><input type="file" name="'+n+'" onchange="checkValue(this);"/></td>';
		}else{
			newRow += '<td><input type="file" name="'+n+'" /></td>';
		}
			newRow += '</tr>';
		$("#"+tbl+" tr:last").after(newRow);
	}
	
	function queryCustomerList(page){
		$("#shForm").attr("action", "customerList.action");
		$("#page").val(page);
		$("#shForm").get(0).submit();
	}
	
	function checkForm(){
		var msg = "";
		if($("#custName").val() == ""){
			msg += "客戶\n"
		}
		if($("#section").val() == ""){
			msg += "產業類別\n"
		}
		if($("#category").val() == ""){
			msg += "產品分類\n"
		}
		
		if(msg != ""){
			alert("請填入以下資訊:\n" + msg);
			return;
		}
		$("#mform").submit();
	}
	
	function editCustomer(id){
		$.get("queryCustomer.action",
		      {"id" : id},
		   	  function(data){ 
		    	 $("#custId").val(data.customer.id);
		    	 $("#custNo").val(data.customer.custNo);
		    	 $("#custName").val(data.customer.custName);
		    	 $("#section").val(data.customer.section);
		    	 changeSelect(document.getElementById("section"), 'category');
		   	 	 setTimeout(function() {
		   	 		$("#category").val(data.customer.category);
				 }, 120);
		    	 $("#keywords").val(data.customer.keywords);
		    	 $("#remark").val(data.customer.remark);
		      },
		      "json"
	  	);
	}
	
	function viewCustomer(id){
		$.get("queryCustomer.action",
		      {"id" : id,
			   "flag" : "Y"},
		   	  function(data){ 
		    	 $("#v_custName").text(data.customer.custName);
		    	 $("#v_section").text(data.customer.sectionName);
		    	 $("#v_category").text(data.customer.categoryName);
		    	 $("#v_keywords").text(data.customer.keywords);
		    	 $("#v_remark").text(data.customer.remark);
		    	 //清空頁面原資料
		    	 $('#v_aTbl tbody').html("");
		    	 $('#v_bTbl tbody').html("");
		    	 $('#v_cTbl tbody').html("");
		    	 $('#v_dTbl tbody').html("");
		    	 
		    	 var tbl = "";
		    	 var objs = data.customer.attachs;
		    	 for(var i=0; i<objs.length; i++){
		    		 var newRow  = '<tr>'; 
		    		 if(objs[i].type == "A"){	//提案
		    			tbl = "v_aTbl";
	    				newRow += '<td width="70%">'+objs[i].fileName+'</td>';
		    		 }
					 if(objs[i].type == "B"){	//文案
						 tbl = "v_bTbl";
						 newRow += '<td width="70%">'+objs[i].fileName+'</td>';
		    		 }
					 if(objs[i].type == "C"){	//素材
						 tbl = "v_cTbl";
						 newRow += '<td width="70%">';
						 newRow += objs[i].fileName;
						 newRow += '<br><audio src="'+objs[i].fileUri+'" controls controlsList="nodownload"></audio>';
						 newRow += '</td>';
		    		 }
					 if(objs[i].type == "D"){	//其他
						 tbl = "v_dTbl";
						 newRow += '<td width="70%">'+objs[i].fileName+'</td>';
		    		 }
					 newRow += '<td>';
					 if(objs[i].type != "C"){
						 newRow += '<a class="btn btn-warning" href="onlineReadDoc.action?id='+objs[i].id+'" target="_blank"><i class="icon_zoom-in" title="檢視"></i></a></a>';
					 }
					 newRow += '<a class="btn btn-success" href="downloadCust.action?id='+objs[i].id+'" ><i class="icon_cloud-download_alt" title="下載"></i></a>';
    				 newRow += '<a class="btn btn-danger" href="#" onclick="deleteDocument(this, '+objs[i].id+');"><i class="icon_close_alt2" title="刪除"></i></a>';
    				 newRow += '</td>';
    				 newRow += '</tr>';
	    			 $("#"+tbl+" tbody").append(newRow);
		    	 }
		      },
		      "json"
	  	);
	}
	
	function deleteCust(id){
		if(confirm("會將該客戶檔案一併清除,確定刪除!")){
			location.href="deleteCust.action?id="+id;
		}
	}
	
	function deleteDocument(o, id){
		if(confirm("確定刪除檔案!")){
			$.get(
				"<s:url namespace='/' action='deleteDocument' />",
			    {'attrId' : id},
			    function(data){
		    		if(data.success == "Y"){
		    			$(o).parents("tr").remove();
		    		}
			    }
			);
		}
	}
	function checkValue(o){
		var val = o.value;
		var str = val.substring(val.lastIndexOf("\\")+1);
// 		var str = val.substring(val.lastIndexOf("/")+1);
		var ary = str.split(".");
		if(ary[ary.length-1] != "mp3"){
			alert("素材需為mp3格式!");
			o.value = "";
			return;
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

      <section id="main-content">
          <section class="wrapper">
		  <div class="row">
				<div class="col-lg-12">
					<h3 class="page-header"><i class="icon_genius"></i> 個案管理</h3>
					
					<!-- 權限 -->
	                <s:iterator value="#session.LOGIN_USER.authority" status="stat">
	                	<s:if test="%{key eq 'F02'}">
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
                              <s:form id="shForm" class="form-inline" role="form" namespace="/" action="customerList" method="post">
                              	  <s:hidden name="page" id="page" />
                              	  <label class="control-label">產業類別</label>&nbsp;
                                  <s:select id="shSection" name="shSection" headerKey="" headerValue="-All-" theme="simple" list="sectionCombo()" 
                                  														onchange="changeSelect(this, 'shCategory');" />&nbsp;
                                  <label class="control-label">產品分類</label>&nbsp;
                                  <s:select id="shCategory" name="shCategory" headerKey="" headerValue="-All-" theme="simple" list="{}" />　
                              	　<label class="control-label">客戶名稱</label>&nbsp;
                              	  <s:textfield id="shCust" name="shCust" theme="simple" />
                               	　<label class="control-label">關鍵字</label>&nbsp;
                                  <s:textfield id="shKeyword" name="shKeyword" theme="simple" />&nbsp;&nbsp;
                                  	<button type="button" class="btn btn-primary" onclick="queryCustomerList(0);">查詢</button>&nbsp;&nbsp;
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
 					<font color="blue">可查詢筆數：<s:property value="custCnt"/></font>
             	  </div>
              </div>
              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <table class="table table-hover">
                              <thead class="panel-heading">
                              <tr>
                                  <th>#</th>
                                  <th>客戶名稱</th>
                                  <th>產業類別</th>
                                  <th>產品分類</th>
                                  <th>關鍵字</th>
                                  <th>Action</th>
                              </tr>
                              </thead>
                              <tbody>
                              	<s:iterator value="pageBean.list" status="i">
                              	<tr>
	                                <td><s:property value="(pageBean.currentPage-1)*10+(#i.index+1)"/></td>
	                                <td><s:property value="custName"/></td>
	                                <td><s:property value="sectionName"/></td>
	                                <td><s:property value="categoryName"/></td>
	                                <td><s:property value="keywords"/></td>
	                                <td>
	                                <div class="btn-group">
	                                	<a class="btn btn-warning" href="#view_dg" data-toggle="modal" data-backdrop="false" onclick="viewCustomer(<s:property value="id" />);">
	                                		<i class="icon_zoom-in" title="檢視"></i>
	                                	</a>
	                                <s:if test='%{#added eq "Y"}'>
	                                	<a class="btn btn-primary" href="#edit_dg" data-toggle="modal" data-backdrop="false" onclick="editCustomer(<s:property value="id" />);">
	                                    	<i class="icon_pencil-edit" title="編輯"></i>
	                                    </a>
	                                </s:if>
	                                <s:if test='%{#download eq "Y"}'>
	                                	<a class="btn btn-success" href="downloadAll.action?id=<s:property value="id" />"><i class="icon_cloud-download_alt" title="整批下載"></i></a>
	                                </s:if>
	                                <s:if test='%{#deleted eq "Y"}'>
	                                	<a class="btn btn-danger" href="#" onclick="deleteCust(<s:property value="id" />);"><i class="icon_close_alt2" title="刪除"></i></a>
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
								        <select name="currentPage" id="currentPage" onChange="queryCustomerList(this.value)">
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
  <!-- 編輯 -->
  <div class="modal fade" id="edit_dg" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
				    <h4 class="modal-title">設定個案</h4>
				</div>
				<s:form id="mform" namespace="/" action="editCustomer" method="post" enctype="multipart/form-data">
					<div class="modal-body">
						<table>
							<s:hidden name="customer.id" id="custId"></s:hidden>
							<s:hidden name="customer.custNo" id="custNo"></s:hidden>
							<tr>
					    		<td align="right" width="25%"><font color="red">*</font>客戶名稱:　</td>
					    		<td>
					    			<s:textfield id="custName" name="customer.custName" theme="simple" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%"><font color="red">*</font>產業類別:　</td>
					    		<td>
									<s:select id="section" name="customer.section" headerKey="" headerValue="-All-" theme="simple" list="sectionCombo()" 
                                  														onchange="changeSelect(this, 'category');" />&nbsp;
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%"><font color="red">*</font>產品分類:　</td>
					    		<td>
									<s:select id="category" name="customer.category" headerKey="" headerValue="-All-" theme="simple" list="{}" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%">關鍵字:　</td>
					    		<td>
					    			<s:textfield id="keywords" name="customer.keywords" theme="simple" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%">備註:　</td>
					    		<td>
					    			<s:textfield id="remark" name="customer.remark" theme="simple" />
								</td>
							</tr>
					    </table>
						<div class="container">
							  <ul class="nav nav-tabs">
								    <li class="active"><a data-toggle="tab" href="#home">提案</a></li>
								    <li><a data-toggle="tab" href="#menu1">文案</a></li>
								    <li><a data-toggle="tab" href="#menu2">素材</a></li>
								    <li><a data-toggle="tab" href="#menu3">其他</a></li>
							  </ul>
							  <div class="tab-content">
							  	<p/>
							    <div id="home" class="tab-pane fade in active">
							      	檔案:　<a class="btn btn-success" href="#" onclick="addFile('a','aTbl');"><i class="icon_plus_alt2" title="增加"></i></a><p/>
							      	<table id="aTbl">
					    				<tr><td><input type="file" name="aupload" /></td></tr>
					    			</table>
							    </div>
							    <div id="menu1" class="tab-pane fade">
							      	檔案:　<a class="btn btn-success" href="#" onclick="addFile('b','bTbl');"><i class="icon_plus_alt2" title="增加"></i></a><p/>
							      	<table id="bTbl">
					    				<tr><td><input type="file" name="bupload" /></td></tr>
					    			</table>
							    </div>
							    <div id="menu2" class="tab-pane fade">
							     	檔案:　<a class="btn btn-success" href="#" onclick="addFile('c','cTbl');"><i class="icon_plus_alt2" title="增加"></i></a><p/>
							      	<table id="cTbl">
					    				<tr><td><input type="file" name="cupload" onchange="checkValue(this);"/></td></tr>
					    			</table>
							    </div>
							    <div id="menu3" class="tab-pane fade">
							      	檔案:　<a class="btn btn-success" href="#" onclick="addFile('d','dTbl');"><i class="icon_plus_alt2" title="增加"></i></a><p/>
							      	<table id="dTbl">
					    				<tr><td><input type="file" name="dupload" /></td></tr>
					    			</table>
							    </div>
							  </div>
						</div>
						<div class="modal-footer">
							<button type="button" id="submitForm" class="btn btn-default" onclick="checkForm();">儲存</button>
						    <button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
						</div>
					</div>
				</s:form>
			</div>
		</div>
	</div>
	<!-- 檢視 -->
	<div class="modal fade" id="view_dg" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
				    <h4 class="modal-title">檢視個案</h4>
				</div>
				<div class="modal-body">
					<table>
						<tr>
				    		<td align="right" width="25%">客戶名稱:　</td>
				    		<td>
				    			<span id="v_custName"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">產業類別:　</td>
				    		<td>
								<span id="v_section"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">產品分類:　</td>
				    		<td>
								<span id="v_category"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">關鍵字:　</td>
				    		<td>
				    			<span id="v_keywords"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">備註:　</td>
				    		<td>
				    			<span id="v_remark"></span>
							</td>
						</tr>
				    </table>
				    <br>
					<div class="container">
					  <ul class="nav nav-tabs">
						    <li class="active"><a data-toggle="tab" href="#home_1">提案</a></li>
						    <li><a data-toggle="tab" href="#menu1_1">文案</a></li>
						    <li><a data-toggle="tab" href="#menu2_1">素材</a></li>
						    <li><a data-toggle="tab" href="#menu3_1">其他</a></li>
					  </ul>
					  <div class="tab-content">
					  	<p/>
					    <div id="home_1" class="tab-pane fade in active">
					      	<table id="v_aTbl" width="100%">
					      		<tbody></tbody>
					      	</table>
					    </div>
					    <div id="menu1_1" class="tab-pane fade">
					      	<table id="v_bTbl" width="100%">
					      		<tbody></tbody>
					      	</table>
					    </div>
					    <div id="menu2_1" class="tab-pane fade">
					      	<table id="v_cTbl" width="100%">
					      		<tbody></tbody>
					      	</table>
					    </div>
					    <div id="menu3_1" class="tab-pane fade">
					      	<table id="v_dTbl" width="100%">
					      		<tbody></tbody>
					      	</table>
					    </div>
					  </div>
					</div>
					<div class="modal-footer">
					    <button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
					</div>
				</div>
			</div>
		</div>
	</div>
  </body>
</html>
