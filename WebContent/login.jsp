<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE>
<html lang="zh">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Radioad - Login</title>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap.min.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/bootstrap-theme.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/elegant-icons-style.css"/>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/font-awesome.css"/>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/style-responsive.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript">
function checkForm(){
	if($("#account").val() == "" || $("#password").val() == ""){
		alert("請輸入帳號、密碼");
		return;
	}
	$("#form").submit();
}
</script>
<!-- HTML5 shim and Respond.js IE8 support of HTML5 -->
<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->
</head>

  <body class="login-img3-body">
    <div class="container">
      <form class="login-form" id="form" action="login.action" method="post"> 
        <div class="login-wrap">
        	<h3>瑞迪廣告資訊服務平台</h3>
            <p class="login-img"><img src="img/logo21.png"/></p>
            <div class="input-group">
            	<span class="input-group-addon"><img src="img/user.png"/></span>
              	<input type="text" id="account" name="account" class="form-control" placeholder="Account" autofocus>
            </div>
            <div class="input-group">
                <span class="input-group-addon"><img src="img/pwd.png"/></span>
                <input type="password" id="password" name="password" class="form-control" placeholder="Password">
            </div>
<!--             <div class="input-group"> -->
<!--                 <span class="input-group-addon"><i class="icon_lock-open_alt"></i></span> -->
<!--                 <input type="password" class="form-control" placeholder="CheckNum"> -->
<!--             </div> -->
            <button class="btn btn-primary btn-lg btn-block" type="button" onclick="checkForm();">登入</button>
        </div>
      </form>
    </div>
  </body>
</html>
