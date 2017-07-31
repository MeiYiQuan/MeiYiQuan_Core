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
	
	<input type="text" id="param" value='{"courseId":"1"}'  size=70> <br>
	
	<input type="text" id="courseUrl"   value="http://127.0.0.1:8081/salon-art-circle/course/courseDetail.do" size=70> 课程详情页-视频信息<br>
	<input type="text" id="teacherUrl"   value="http://127.0.0.1:8081/salon-art-circle/teacher/courseDetailTeacher.do" size=70> 课程详情页-讲师信息<br>
	<input type="text" id="courRecomUrl"   value="http://127.0.0.1:8081/salon-art-circle/courseRecommend/courseDetailCourRecom.do" size=70> 课程详情页-相关推荐课程信息<br>
	<input type="text" id="commentUrl"   value="http://127.0.0.1:8081/salon-art-circle/comment/courseDetailComment.do" size=70> 课程详情页-评论信息<br>
	<input type="button" onclick="course()" value="课程"/> <br>
	<input type="button" onclick="teacher()" value="讲师"/> <br>
	<input type="button" onclick="courRecom()" value="相关推荐"/> <br>
	<input type="button" onclick="comment()" value="评论"/> <br>
	<hr>
	<hr>
	<input type="text" id="paramJump" value='{"videoId":"1"}'  size=70> <br>
	<input type="text" id="videoJumpUrl"   value="http://127.0.0.1:8081/salon-art-circle/course/courseDetailJumpVideo.do" size=70> 课程详情页-视频跳转<br>
	<input type="button" onclick="videoJump()" value="评论"/> <br>
	
		
	<script type="text/javascript">
	
	
		function videoJump(){
			var url_val = $("#videoJumpUrl").val();
			var param = $("#paramJump").val();
			$.ajax({
	                    type: "post",
	                    dataType:"text",
	                    url:url_val,
	                    data:param,
	                    contentType: "application/text; charset=utf-8", //content-type:application/json
	                    error: function(dataFromBehind){
	                    	alert('操作错误,请与系统管理员联系!');
	                    	alert(dataFromBehind);
	                    },
	                    beforeSend:function(){
	                    	alert(url_val);
	                    	alert(param);
	                    	return true;
	                    },
	                    success: function(dataFromBehind){
	                    	alert('操作成功');
	                    	alert(dataFromBehind);
	                    }
	                 });
	          }
	
	
	
		function buyVideo(){
			var url_val = $("#buyVideoUrl").val();
			var param = '{"userid":"'+$("#userid").val()+'","videoId":"'+$("#videoId").val()+'","price":"'+$("#price").val()+'"}';
			$.ajax({
	                    type: "post",
	                    dataType:"text",
	                    url:url_val,
	                    data:param,
	                    contentType: "application/text; charset=utf-8", //content-type:application/json
	                    error: function(dataFromBehind){
	                    	alert('操作错误,请与系统管理员联系!');
	                    	alert(dataFromBehind);
	                    },
	                    beforeSend:function(){
	                    	alert(param);
	                    },
	                    success: function(dataFromBehind){
	                    	alert('操作成功');
	                    	alert(dataFromBehind);
	                    }
	                 });
	          }
	
	
		function course(){
			var url_val = $("#courseUrl").val();
			var param_val = $("#param").val();
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
		
		
		function teacher(){
			var url_val = $("#teacherUrl").val();
			var param_val = $("#param").val();
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
		
		function courRecom(){
			var url_val = $("#courRecomUrl").val();
			var param_val = $("#param").val();
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
		
		
		function comment(){
			var url_val = $("#commentUrl").val();
			var param_val = $("#param").val();
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