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
	
	<input type="text" id="urlProv"   value="http://127.0.0.1:8081/salon-art-circle/district/register.do" size=70> <br>
	<input type="button" onclick="prov()" value="注册页面的省份信息"/>
	<hr>
	<hr>
	<input type="text" id="param" value='{"districtId":"1"}'> <br>
	<input type="text" id="urlDetail"   value="http://127.0.0.1:8081/salon-art-circle/district/registerDetail.do" size=70> <br>
	<input type="button" onclick="detail()" value="注册页面的详细地址信息"/>
	
	<script type="text/javascript">
		
		function prov(){
			var url_val = $("#urlProv").val();
			$.ajax({
	                    type: "post",
	                    dataType:"text",
	                    url:url_val,
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
	
	
		function detail(){
			var url_val = $("#urlDetail").val();
			var param_val = $("#param").val();
			$.ajax({
	                    type: "post",
	                    dataType:"text",
	                    url:url_val,
	                    data:param_val,
	                    contentType: "application/text; charset=utf-8", //content-type:application/json
	                    beforeSend:function(){
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