<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!Doctype html>
<html>
<head>
<meta charset="utf-8" />
<title>请求</title>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta content="width=device-width,minimum-scale=0.1,maximum-scale=8.0,user-scalable=yes" name="viewport" />

<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
</head>
<body>
<p>url:<%=request.getParameter("url") %></p>
<p>data:<%=request.getParameter("data") %></p>
<div class="data">
	
</div>

<script type="text/javascript">
$(function(){
	testSend();
})
function testSend(){
	var url="<%=request.getParameter("url") %>";
	var data='<%=request.getParameter("data") %>';
	if(url=="")
		return; 
	if(data=="")
		return; 
	$.ajax({
                type: "post",
                dataType:"text",
                url:url,
                //'{"phone":"17710663181"}'
                data:data,
                contentType: "application/text; charset=utf-8", //不能用 content-type:application/json
                error: function(dataFromBehind){
                	alert('操作错误,请与系统管理员联系!');
                },
                success: function(dataFromBehind){
                	$(".data").html(dataFromBehind);
                }
             });
      }

</script>
</body>
</html>
