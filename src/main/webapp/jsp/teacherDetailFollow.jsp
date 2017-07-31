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
	<input type="text" id="teacherId" value="1"> 讲师ID<br>
	<input type="text" id="userid" value="40287381585e0c3401585e0edb4f0001"> 用户ID<br>
	<input type="text" id="followUrl"   value="http://127.0.0.1:8081/salon-art-circle/follow/fromTeacherDetail.do" size=70> <br>
	<input type="button" onclick="followOrNot()" value="关注/取消关注"/> <br>
	<script type="text/javascript">
		
		function followOrNot(){
			var url_val = $("#followUrl").val();
	        var dataSend = '{"userid":"'+ $("#userid").val()+'","teacherId":"'+ $("#teacherId").val() +'"}'
			$.ajax({
	                    type: "post",
	                    dataType:"text",
	                    url:url_val,
	                    //'{"pageNo":"","pagesize":""}'
	                    data:dataSend,
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