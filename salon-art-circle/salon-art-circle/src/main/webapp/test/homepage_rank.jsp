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
	<input type="text" id="url"   value="http://127.0.0.1:8081/salon-art-circle/homepage/courseRank.do" size=70> <br>
	<input type="text" id="pageNo" value="1"> <br>
	<input type="text" id="pagesize" value="5"> <br>
	
	<input type="button" onclick="play()" value="热播"/> <br>
	<input type="button" onclick="order()" value="热销"/> <br>
	<input type="button" onclick="comment()" value="热评"/> <br>
	
	<script type="text/javascript">
		
		function play(){
			var url_val = $("#url").val();
	        var dataSend = '{"pageNo":"'+$("#pageNo").val()+'","pagesize":"'+$("#pagesize").val()+'","type":"1"}'
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
		
		function order(){
			var url_val = $("#url").val();
	        var dataSend = '{"pageNo":"'+$("#pageNo").val()+'","pagesize":"'+$("#pagesize").val()+'","type":"2"}'
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
		
		
		function comment(){
			var url_val = $("#url").val();
	        var dataSend = '{"pageNo":"'+$("#pageNo").val()+'","pagesize":"'+$("#pagesize").val()+'","type":"3"}'
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