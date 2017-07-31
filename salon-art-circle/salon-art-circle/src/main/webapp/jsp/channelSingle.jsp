<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<html>
<head>
<base href="${pageContext.request.scheme }://${pageContext.request.serverName }:${pageContext.request.serverPort }${pageContext.request.contextPath }/">
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<input type="text" id="url"   value="http://127.0.0.1:8081/salon-art-circle/course/channelCourse.do" size=70> <br>
	<input type="text" id="timeParam" value='{"pageNo":"1","pagesize":"5","type":"1","channelId":"5"}'  size=70>
	<input type="button" onclick="time()" value="最新"/>
	<hr>
	<input type="text" id="palyParam" value='{"pageNo":"1","pagesize":"5","type":"2","channelId":"5"}'  size=70>
	<input type="button" onclick="play()" value="最热"/>
	<hr>
	<input type="text" id="priceParam" value='{"pageNo":"1","pagesize":"5","type":"3","channelId":"5"}'  size=70>
	<input type="button" onclick="price()" value="最热"/>
	
	<script type="text/javascript">
	
		function price(){
			var url_val = $("#url").val();
			var param_val = $("#priceParam").val();
			$.ajax({
	                    type: "post",
	                    dataType:"text",
	                    url:url_val,
	                    data:param_val,
	                    contentType: "application/text; charset=utf-8", //content-type:application/json
	                    beforeSend:function(){
	                    	alert(url_val);
	                    	alert(param_val);
	                    	return true;
	                    },
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
		
		function play(){
			var url_val = $("#url").val();
			var param_val = $("#palyParam").val();
			$.ajax({
	                    type: "post",
	                    dataType:"text",
	                    url:url_val,
	                    data:param_val,
	                    contentType: "application/text; charset=utf-8", //content-type:application/json
	                    beforeSend:function(){
	                    	alert(url_val);
	                    	alert(param_val);
	                    	return true;
	                    },
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
		
		function time(){
			var url_val = $("#url").val();
			var param_val = $("#timeParam").val();
			$.ajax({
	                    type: "post",
	                    dataType:"text",
	                    url:url_val,
	                    data:param_val,
	                    contentType: "application/text; charset=utf-8", //content-type:application/json
	                    beforeSend:function(){
	                    	alert(url_val);
	                    	alert(param_val);
	                    	return true;
	                    },
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