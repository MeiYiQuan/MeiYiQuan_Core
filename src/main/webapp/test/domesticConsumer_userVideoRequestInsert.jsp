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
	
	<!-- 测试登录 -->
	<input type="text" id="url"   value="http://127.0.0.1:8080/salon-art-circle/user/userVideoRequestAdd.do" size=70> <br>
	<input type="text" id="userid" value="40287381585e0c3401585e0edb4f0001"><br>
	<input type="text" id="teacherId" value="1"><br>
	课程名称:<input type="text" id="question" value="1"> <br>
	您的描述:<input type="text" id="courseName" value="5"> <br>
	
	<input type="button" onclick="testSend()" value="测试"/>
	
	<script type="text/javascript">
		
		function testSend(){
			var url_val = $("#url").val();
			var param_val = '{"userid":"'+$("#userid").val()+'","question":"'+$("#question").val()+'","courseName":"'+$("#courseName").val()+'","teacherId":"'+$("#teacherId").val()+'"}';
			$.ajax({
	                    type: "post",
	                    dataType:"text",
	                    url:url_val,
	                    data:param_val,
	                    contentType: "application/text; charset=utf-8", //content-type:application/json
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