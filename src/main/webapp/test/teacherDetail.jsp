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
	<input type="text" id="teacherUrl"   value="http://127.0.0.1:8081/salon-art-circle/teacher/teacherDetail.do" size=70>
	<input type="button" onclick="teacherList()" value="教师详情"/> <br>
	
	<input type="text" id="courseUrl"   value="http://127.0.0.1:8081/salon-art-circle/course/teacherDetailCourse.do" size=70>
	<input type="button" onclick="courseList()" value="课程详情"/> <br>
	
	<input type="text" id="activityUrl"   value="http://127.0.0.1:8081/salon-art-circle/activity/teacherDetailActivity.do" size=70>
	<input type="button" onclick="activityList()" value="活动"/> <br>
	
	<input type="text" id="commentUrl"   value="http://127.0.0.1:8081/salon-art-circle/comment/teacherDetailComment.do" size=70> 
	<input type="button" onclick="commentList()" value="评论"/> <br>
	
	
	<script type="text/javascript">
		
		function teacherList(){
			var url_val = $("#teacherUrl").val();
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
		function courseList(){
			var url_val = $("#courseUrl").val();
	        var dataSend = '{"teacherId":"'+ $("#teacherId").val() +'"}'
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
		
		function activityList(){
			var url_val = $("#activityUrl").val();
	        var dataSend = '{"teacherId":"'+ $("#teacherId").val() +'"}'
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
		
		function commentList(){
			var url_val = $("#commentUrl").val();
	        var dataSend = '{"teacherId":"'+ $("#teacherId").val() +'"}'
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