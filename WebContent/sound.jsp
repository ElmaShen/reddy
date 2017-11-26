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
			location.href = "<s:url namespace='/' action='soundList' />";
		}
		$("#area").val("台北市");
		
		//關閉reset form
		$('.modal').on('hidden.bs.modal', function (e) {
			$(this).find("input,textarea,select").val('').end()
			  	   .find("input[type=checkbox], input[type=radio]").prop("checked", "").end();
			//移除File addRow
			var tblRow = $('#tbl tr').length;
			if(tblRow > 1){
				$("#tbl tr").remove();
				
				var newRow  = '<tr>'; 
					newRow += '<td><input type="file" id="upload" name="upload" onchange="setFieldValue(this);" /></td>';
					newRow += '</tr>';
				$("#tbl").append(newRow);
			}

			$("#area").val("台北市");
		})
	});
	
	function querySoundList(page){
		$("#shForm").attr("action", "soundList.action");
		$("#page").val(page);
		$("#shForm").get(0).submit();
	}
	function viewSound(id){
		$.get("querySound.action",
		      {"id" : id},
		   	  function(data){ 
		    	 $("#v_year").text(data.sound.year);
		    	 $("#v_month").text(data.sound.month);
		    	 $("#v_custName").text(data.sound.custName);
		    	 $("#v_title").text(data.sound.title);
		    	 $("#v_area").text(data.sound.area);
		    	 $("#v_second").text(data.sound.second);
		    	 $("#v_voice").text(data.sound.sectionName);
		    	 $("#v_role").text(data.sound.role);
		    	 $("#v_skill").text(data.sound.skillName);
		    	 $("#v_tone").text(data.sound.toneName);
		      },
		      "json"
	  	);
	}
	function editSound(id){
		if(id == 0){
			$('#fhead').show();
			$('#fbody').show();
		}else{
			$.get("querySound.action",
			      {"id" : id},
			   	  function(data){ 
			    	 $("#sid").val(data.sound.id);
			    	 $("#year").val(data.sound.year);
			    	 $("#month").val(data.sound.month);
			    	 $("#custName").val(data.sound.custName);
			    	 $("#title").val(data.sound.title);
			    	 $("#area").val(data.sound.area);
			    	 $("#second").val(data.sound.second);
			    	 $("#voice").val(data.sound.section);
			    	 $("#role").val(data.sound.role);
			    	 $("#tone").val(data.sound.tone);
			    	 var ary = data.sound.skill.split(",");
			    	 if(ary.length > 0){
			    		 for(var i=0; i<ary.length; i++){
			    			 $("#skill_"+(i+1)).val(ary[i]);
			    		 }
			    	 }
			      },
			      "json"
		  	);
			$('#fhead').hide();
			$('#fbody').hide();
		}
	}
	function setFieldValue(o){
		var val = o.value;
		var str = val.substring(val.lastIndexOf("\\")+1);
// 		var str = val.substring(val.lastIndexOf("/")+1);
		var ary = str.split(".");
		if(ary[ary.length-1] != "mp3"){
			alert("檔案需為mp3格式!");
			o.value = "";
			return;
		}
		
		var second = 0, title = "", tone = "", role = "", skill = "";
// 		a.年份.產業類別.客戶.篇名.秒數.調性.角色.手法
// 		b.年份.產業類別.客戶.秒數.調性.角色.手法
// 		c.年份.產業類別.客戶.篇名.秒數.調性.手法
// 		d.年份.產業類別.客戶.秒數.調性.手法
		if(ary.length-1 < 6 || ary.length-1 > 8){
			alert("檔名格式不符,無法更新屬性設定");
			return;
		}
		$("#year").val(ary[0]);
		$("#voice").val(ary[1]);
		$("#custName").val(ary[2]);
		
		if(ary[3].indexOf("秒") != -1){
			second = parseInt(ary[3].substring(0,ary[3].indexOf("秒")));
		}else{
			title = ary[3];
			if(ary[4].indexOf("秒") != -1){
				second = parseInt(ary[4].substring(0,ary[4].lastIndexOf("秒")));
			}else{
				tone = ary[4];
			}
		}
		if(ary.length-1 == 6){
			skill = ary[5];
		}
		if(ary.length-1 == 7){
			if(ary[5].length == 1){
				tone = ary[5];
			}else{
				role = ary[5];
			}
			skill = ary[6];
		}
		if(ary.length-1 == 8){
			tone = ary[5];
			role = ary[6];
			skill = ary[7];
		}
		$("#title").val(title);
		$("#second").val(second);
		$("#role").val(role);
		$("#tone").val(tone);
		$("#skill_1").val(skill);
	}
	function addFile(){
		var newRow  = '<tr>'; 
			newRow += '<td><input type="file" id="upload" name="upload" onchange="setFieldValue(this);" /></td>';
			newRow += '</tr>';
		$("#tbl tr:last").after(newRow);
	}
	function checkForm(){
		var msg = "";
		if($("#year").val() == ""){
			msg += "年份\n"
		}
		if($("#custName").val() == ""){
			msg += "客戶\n"
		}
		if($("#title").val() == ""){
			msg += "篇名\n"
		}
		if($("#area").val() == ""){
			msg += "地區\n"
		}
		if($("#second").val() == ""){
			msg += "秒數\n"
		}
		if($("#voice").val() == ""){
			msg += "產業類別\n"
		}
		if($("#skill_1").val() == "" && $("#skill_2").val() == "" && $("#skill_3").val() == ""){
			msg += "手法\n"
		}
		if($("#tone").val() == ""){
			msg += "調性\n"
		}
		
		if(msg != ""){
			alert("請填入以下資訊:\n" + msg);
			return;
		}
		$("#mform").submit();
	}
	
	function deleteSound(id){
		if(confirm("確定刪除檔案!")){
			location.href="deleteSound.action?id="+id;
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
					<h3 class="page-header"><i class="icon_music"></i> 音檔管理</h3>
					
					<!-- 權限 -->
	                <s:iterator value="#session.LOGIN_USER.authority" status="stat">
	                	<s:if test="%{key eq 'F01'}">
	                		<s:set var="added" value="value.added" />
			                <s:set var="deleted" value="value.deleted" />
			                <s:set var="download" value="value.download" />
	                	</s:if>
	                </s:iterator>
				</div>
			</div>
              <!-- page start-->
               <section class="panel">
                   <header class="panel-heading">查詢條件</header>
                   <div class="panel-body">
                   <s:form id="shForm" class="form-inline" role="form" namespace="/" action="soundList" method="post">
                   	  <s:hidden name="page" id="page" />
		              <div class="row">
		                  <div class="col-lg-12">
                              <div class="col-lg-2">
                              	<label class="control-label">年份</label>
                                <s:textfield id="shYear" name="shYear" theme="simple" cssStyle="width:60px;"/>
                               	<s:select id="shMon" name="shMon" headerKey="" headerValue="-" theme="simple" list="monCombo()" style="width:60px;" />
                              </div>
                              <div class="col-lg-2">
                              	<label class="control-label">地區</label>
                               	<s:select id="shArea" name="shArea" headerKey="" headerValue="-All-" theme="simple" list="areaCombo()" />
                              </div>
                              <div class="col-lg-2">
                              	<label class="control-label">客戶</label>
                                <s:textfield id="shCust" name="shCust" theme="simple" cssStyle="width:120px;"/>
                              </div>
                           	  <div class="col-lg-2">
                              	<label class="control-label">篇名</label>
                                <s:textfield id="shTitle" name="shTitle" theme="simple" cssStyle="width:120px;"/>
                              </div>	　　
                              <div class="col-lg-3">
                              	<label class="control-label">產業類別</label>
                              	<s:select id="shSection" name="shSection" headerKey="" headerValue="-All-" theme="simple" list="voiceCombo()" />
                              </div>	
	                  	</div>
	              	</div>
	              	<div class="row">
		                 <div class="col-lg-2">
                         	<label class="control-label">秒數</label>
                          	<s:select id="shSecond" name="shSecond" headerKey="" headerValue="-All-" theme="simple" list="secondCombo()" />
                         </div>
                         <div class="col-lg-3">
                          	<label class="control-label">調性</label>
                            <s:select id="shTone" name="shTone" headerKey="" headerValue="-All-" theme="simple" list="toneCombo()" />
                         </div>
                         <div class="col-lg-2">
                         	<label class="control-label">角色</label>
                            <s:select id="shRole" name="shRole" headerKey="" headerValue="-All-" theme="simple" list="roleCombo()"/>
                         </div>
                    	 <div class="col-lg-2">
                         	<label class="control-label">手法</label>
                          	<s:select id="shSkill" name="shSkill" headerKey="" headerValue="-All-" theme="simple" list="skillCombo()" />
                         </div>	　　
                         <div class="col-lg-3">
                         		<button type="button" class="btn btn-primary" onclick="querySoundList(0);">查詢</button>
                         	<s:if test='%{#added eq "Y"}'>
								<button type="button" class="btn btn-primary" data-toggle="modal" data-target="#edit_dg" data-backdrop="false" onclick="editSound(0);">新增</button>
                         	</s:if>
                         </div>	
	                  	</div>
	              	</div>
	              </s:form>
             	</div>
             </section>
             <div class="row">
                  <div class="col-lg-12">
 					<font color="blue">可查詢筆數：<s:property value="soundCnt"/></font>
             	  </div>
             </div>
              <div class="row">
                  <div class="col-lg-12">
                      <section class="panel">
                          <table class="table table-hover">
                              <thead class="panel-heading">
	                              <tr>
	                                  <th>#</th>
	                                  <th>年份</th>
	                                  <th>客戶</th>
	                                  <th>篇名</th>
	                                  <th>播放</th>
	                                  <th>產業類別</th>
	                                  <th>調性</th>
	                                  <!-- <th>角色</th> -->
	                                  <!-- <th>手法</th> -->
	                                  <th>Action</th>
	                              </tr>
                              </thead>
                              <tbody>
                              	  <s:iterator value="pageBean.list" status="i">
                              	  <tr>
	                                  <td><s:property value="(pageBean.currentPage-1)*10+(#i.index+1)"/></td>
	                                  <td><s:property value="year" /></td>
	                                  <td><s:property value="custName" /></td>
	                                  <td><s:property value="title" /></td>
	                                  <td width="5%">
	                                  	<audio src='<s:property value="fileUri" />' controls controlsList="nodownload"></audio>
	                                  </td>
	                                  <td><s:property value="sectionName" /></td>
	                                  <td><s:property value="toneName" /></td>
	                                  <!-- <td><s:property value="role" /></td> -->
	                                  <!-- <td><s:property value="skillName" /></td> -->
	                                  <td>
	                                  <div class="btn-group">
	                                  	  <a class="btn btn-warning" href="#view_dg" data-toggle="modal" data-backdrop="false" onclick="viewSound(<s:property value="id" />);">
	                                		<i class="icon_zoom-in" title="檢視"></i>
	                                	  </a>
	                                  <s:if test='%{#added eq "Y"}'>
	                                  	  <a class="btn btn-primary" href="#edit_dg" data-toggle="modal" data-backdrop="false" onclick="editSound(<s:property value="id" />);">
	                                      	<i class="icon_pencil-edit" title="編輯"></i>
	                                      </a>
	                                  </s:if>
	                                  <s:if test='%{#download eq "Y"}'>
	                                  	  <a class="btn btn-success" href="downloadSound.action?id=<s:property value="id" />"><i class="icon_cloud-download_alt" title="下載"></i></a>
	                                  </s:if>
	                                  <s:if test='%{#deleted eq "Y"}'>
	                                  	  <a class="btn btn-danger" href="#" onclick="deleteSound(<s:property value="id" />);"><i class="icon_close_alt2" title="刪除"></i></a>
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
							        <select name="currentPage" id="currentPage" onChange="querySoundList(this.value)">
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
				    <h4 class="modal-title">編輯音檔</h4>
				</div>
				<s:form id="mform" namespace="/" action="editSounds" method="post" enctype="multipart/form-data">
					<div class="modal-body">
						<s:hidden name="sound.id" id="sid"></s:hidden>
					    <table id="editTbl">
					    	<tr>
					    		<td align="right" width="25%"><font color="red">*</font>年份:　</td>
					    		<td>
					    			<s:textfield id="year" name="sound.year" theme="simple" cssStyle="width:80px;"/>
					    			<s:select id="month" name="sound.month" headerKey="" headerValue="-" theme="simple" list="monCombo()" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%"><font color="red">*</font>客戶:　</td>
					    		<td>
					    			<s:textfield id="custName" name="sound.custName" theme="simple" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%"><font color="red">*</font>篇名:　</td>
					    		<td>
					    			<s:textfield id="title" name="sound.title" theme="simple" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%"><font color="red">*</font>地區:　</td>
					    		<td>
									<s:select id="area" name="sound.area" headerKey="" headerValue="-" theme="simple" list="areaCombo()" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%"><font color="red">*</font>秒數:　</td>
					    		<td>
									<s:select id="second" name="sound.second" headerKey="" headerValue="-" theme="simple" list="secondCombo()" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%"><font color="red">*</font>產業類別:　</td>
					    		<td>
									<s:select id="voice" name="sound.section" headerKey="" headerValue="-" theme="simple" list="voiceCombo()" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%">角色:　</td>
					    		<td>
									<s:select id="role" name="sound.role" headerKey="" headerValue="-" theme="simple" list="roleCombo()" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%"><font color="red">*</font>手法:　</td>
					    		<td>
									<s:select id="skill_1" name="skills" headerKey="" headerValue="-" theme="simple" list="skillCombo()" />
									<s:select id="skill_2" name="skills" headerKey="" headerValue="-" theme="simple" list="skillCombo()" />
									<s:select id="skill_3" name="skills" headerKey="" headerValue="-" theme="simple" list="skillCombo()" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%"><font color="red">*</font>調性:　</td>
					    		<td>
									<s:select id="tone" name="sound.tone" headerKey="" headerValue="-" theme="simple" list="toneCombo()" />
								</td>
							</tr>
							<tr id="fhead">
					    		<td align="right" width="25%">檔案:　</td>
					    		<td>&nbsp;
					    			<!-- <a class="btn btn-success" href="#" onclick="addFile();"><i class="icon_plus_alt2" title="增加"></i></a> -->
								</td>
							</tr>
							<tr id="fbody">
								<td align="right" width="25%">&nbsp;</td>
								<td>
									<table id="tbl">
					    				<tr><td><input type="file" id="upload" name="upload" onchange="setFieldValue(this);" /></td></tr>
					    			</table>
								</td>
							</tr>
					    </table>
					    <div class="modal-footer">
							<button type="button" id="submitForm" class="btn btn-default" onclick="checkForm();">儲存</button>
			   	 			<button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
						</div>
					</div>
				</s:form>
			</div>
		</div>
	</div>
	
	<div class="modal fade" id="view_dg" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
				    <h4 class="modal-title">檢視音檔</h4>
				</div>
				<div class="modal-body">
				    <table>
				    	<tr>
				    		<td align="right" width="25%">年份:　</td>
				    		<td>
				    			<span id="v_year"></span>
				    			<span id="v_month"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">客戶:　</td>
				    		<td>
				    			<span id="v_custName"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">篇名:　</td>
				    		<td>
				    			<span id="v_title"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">地區:　</td>
				    		<td>
								<span id="v_area"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">秒數:　</td>
				    		<td>
								<span id="v_second"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">產業類別:　</td>
				    		<td>
								<span id="v_voice"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">角色:　</td>
				    		<td>
								<span id="v_role"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">手法:　</td>
				    		<td>
								<span id="v_skill"></span>
							</td>
						</tr>
						<tr>
				    		<td align="right" width="25%">調性:　</td>
				    		<td>
								<span id="v_tone"></span>
							</td>
						</tr>
				    </table>
				    <div class="modal-footer">
		   	 			<button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
					</div>
				</div>
			</div>
		</div>
	</div>
  </body>
</html>
