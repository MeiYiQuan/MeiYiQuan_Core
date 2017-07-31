<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="${pageContext.request.scheme }://${pageContext.request.serverName }:${pageContext.request.serverPort }${pageContext.request.contextPath }/">
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	验证码访问路径:<input type="text" id="idenCode_url"   value="http://127.0.0.1:8081/salon-art-circle/user/getIdenCode.do" size=70> <br>
	注册访问路径:<input type="text" id="register_url"   value="http://127.0.0.1:8081/salon-art-circle/user/register.do" size=70> <br>
	手机号码:<input type="text" id="phone"   value="1" size=70> <br>
	验证码:<input type="text" id="idenCode" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" onclick="testSend()" value="发送验证码"/> <br>
	区域:<input type="text" id = district > <br>
	密码:<input type="text" id="password" > <br>
	<input type="button" onclick="testEdit()" value="注册"/>
	
	<script type="text/javascript">
		//sendNum=0为发送验证码,sendNum=1为修改密码
		function testSend(){
			var url_val = $("#idenCode_url").val();
			var data0 = '{"phone":"'+$("#phone").val()+'","whoNeedIdenCode":"0"}';
			$.ajax({
	                    type: "post",
	                    dataType:"text",
	                    url:url_val,
	                    //'{"phone":"17710663181"}'
	                    data:data0,
	                    contentType: "application/text; charset=utf-8", //不能用 content-type:application/json
	                    error: function(dataFromBehind){
	                    	alert('操作错误,请与系统管理员联系!');
	                    	alert(dataFromBehind);
	                    },
	                    success: function(dataFromBehind){
	                    	alert('操作成功');
	                    	alert(dataFromBehind);
	                    }
	                 });
	          }
		function testEdit(){
			var url_val = $("#register_url").val();
			var data1 = '{"phone":"'+$("#phone").val()+'","password":"'+$("#password").val()+'","idenCode":"'+$("#idenCode").val()+'","district":"'+$("#district").val()+'"}'
			$.ajax({
	                    type: "post",
	                    dataType:"text",
	                    url:url_val,
	                    //'{"phone":"1","password":"2","idenCode":"1234"}'
	                    data:data1,
	                    contentType: "application/text; charset=utf-8", //不能用 content-type:application/json
	                    error: function(dataFromBehind){
	                    	alert('操作错误,请与系统管理员联系!');
	                    	alert(dataFromBehind);
	                    },
	                    success: function(dataFromBehind){
	                    	alert('操作成功');
	                    	alert(dataFromBehind);
	                    }
	                 });
	          }
		
	</script>
</body>
</html>