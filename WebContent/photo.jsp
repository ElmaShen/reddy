<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE>
<html lang="zh">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Reddy | Music Platform</title>

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
	});
	
	function queryPhotoList(page){
		$("#shForm").attr("action", "photoList.action");
		$("#page").val(page);
		$("#shForm").get(0).submit();
	}
	
	function editPhoto(id){
		$.get("queryPhoto.action",
		      {"id" : id},
		   	  function(data){ 
		    	 $("#aid").val(data.audio.id);
		    	 $("#keywords").val(data.audio.keywords);
		    	 $("#gno").val(data.audio.gno);
		    	 $("#gname").val(data.audio.gname);
		      },
		      "json"
	  	);
	}
	
	function addFile(){
		var newRow  = '<tr>'; 
			newRow += '<td><input type="file" name="upload" onchange="checkFile(this);" /></td>';
			newRow += '</tr>';
		$("#tbl tr:last").after(newRow);
	}
	
	function checkFile(o){
		val = o.value;
		var str = val.substring(val.lastIndexOf("\\")+1);
		var ary = str.split(".");
		var str = ary[ary.length-1];
		if(str != "jpg" && str != "gif" && str != "png" && str != "bmp" &&
				str != "JPG" && str != "GIF" && str != "PNG" && str != "BMP"){
			alert("檔案需為圖檔格式!");
			o.value = "";
			return;
		}	
	}
	
	function submitForm(){
		if($("#aid").val() == 0){
			var isOk = false;
			var len = document.getElementsByName("upload").length;
			for(var i=0; i<len; i++){
				if(document.getElementsByName("upload")[i].value != ""){
					isOk = true;
					break;
				}
			}
			
			if(!isOk){
				alert("請選擇需上傳的檔案!");
				return;
			}
		}
		
		$("#mform").submit();
	}
	
	function deletePhoto(id){
		if(confirm("確定刪除檔案!")){
			location.href="deletePhoto.action?id="+id;
		}
	}
	
	function setImg(uri){
		$("#picture").attr("src",uri);
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
					<h3 class="page-header"><i class="icon_images"></i> 照片及影片管理</h3>
					
					<!-- 權限 -->
	                <s:iterator value="#session.LOGIN_USER.authority" status="stat">
	                	<s:if test="%{key eq 'F03'}">
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
                              <s:form id="shForm" class="form-inline" role="form" namespace="/" action="photoList" method="post">
                              	  <s:hidden name="page" id="page" />
                              	  <label class="control-label">關鍵字</label>&nbsp;
                                  <s:textfield id="shKeywords" name="shKeywords" theme="simple" />&nbsp;&nbsp;
                                  <label class="control-label">群組</label>&nbsp;
                                  <s:textfield id="shGname" name="shGname" theme="simple" />&nbsp;&nbsp;
                                  	<button type="button" class="btn btn-primary" onclick="queryPhotoList(0);">查詢</button>&nbsp;
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
                                  <th>關鍵字</th>
                                  <th>群組</th>
                                  <th>檔名</th>
                                  <th>Action</th>
                              </tr>
                              </thead>
                              <tbody>
	                              <s:iterator value="pageBean.list" status="i">
	                              <tr>
	                                  <td><s:property value="%{#i.index+1}"/></td>
	                                  <td><s:property value="keywords"/></td>
	                                  <td><s:property value="gname"/></td>
	                                  <td><s:property value="fileName"/></td>
	                                  <td>
	                                  <div class="btn-group">
	                                  	  <a class="btn btn-warning" href="#view_dg" data-toggle="modal" data-backdrop="false" onclick="setImg('<s:property value="fileUri" />');">
	                                	  	<i class="icon_zoom-in" title="檢視"></i>
	                                	  </a>
	                                  <s:if test='%{#added eq "Y"}'>
	                                  	  <a class="btn btn-primary" href="#edit_dg" data-toggle="modal" data-backdrop="false" onclick="editPhoto(<s:property value="id" />);"><i class="icon_pencil-edit" title="編輯"></i></a>
	                                  </s:if>
	                                  <s:if test='%{#download eq "Y"}'>
	                                  	  <a class="btn btn-success" href="downloadPhoto.action?id=<s:property value="id" />"><i class="icon_cloud-download_alt" title="下載"></i></a>
	                                  </s:if>
	                                  <s:if test='%{#deleted eq "Y"}'>
	                                  	  <a class="btn btn-danger" href="#" onclick="deletePhoto(<s:property value="id" />);"><i class="icon_close_alt2" title="刪除"></i></a>
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
								        <select name="currentPage" id="currentPage" onChange="queryPhotoList(this.value)">
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
				    <h4 class="modal-title">照片/影片上傳</h4>
				</div>
				<s:form id="mform" namespace="/" action="editPhoto" method="post" enctype="multipart/form-data">
					<s:hidden name="audio.id" id="aid"></s:hidden>
					<s:hidden name="audio.gno" id="gno"></s:hidden>
					<div class="modal-body">
						<table>
							<tr>
					    		<td align="right" width="25%">關鍵字:　</td>
					    		<td>
					    			<s:textfield id="keywords" name="audio.keywords" theme="simple" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%">群組:　</td>
					    		<td>
					    			<s:textfield id="gname" name="audio.gname" theme="simple" />
								</td>
							</tr>
							<tr>
					    		<td align="right" width="25%">檔案:　</td>
					    		<td>
									<a class="btn btn-success" href="#" onclick="addFile();"><i class="icon_plus_alt2" title="增加"></i></a>
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
								<table id="tbl">
				    				<tr><td><input type="file" name="upload" onchange="checkFile(this);" /></td></tr>
				    			</table>
							</tr>
					    </table>
					    <div class="modal-footer">
							<button type="button" class="btn btn-default" onclick="submitForm();">儲存</button>
						    <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
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
				    <h4 class="modal-title">預覽圖片</h4>
				</div>
				<div class="modal-body" align="center">
					<img id="picture" src="" width="80%" height="80%">
					
					<div class="modal-footer">
					    <button type="button" class="btn btn-default" data-dismiss="modal">關閉</button>
					</div>
				</div>
			</div>
		</div>
	</div>
  </body>
</html>
