<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<link href="<%=request.getContextPath()%>/resources/css/login-style.css" rel='stylesheet' type='text/css' />
<meta name="viewport" content="width=device-width, initial-scale=1">
<script type="application/x-javascript"> 
    addEventListener("load", function() { setTimeout(hideURLbar, 0); }, false); 
    function hideURLbar(){ window.scrollTo(0,1); }
</script>>
</head>
<body>
	<div class="main">
			<form:form method="post" action="loginForm">
			<h1>
				<lable> SST Login </lable>
			</h1>
			<div class="inset">
				<p>
					<label>EMAIL ADDRESS</label> <form:input path="username" />
				</p>
				<p>
					<label >PASSWORD</label> <form:password path="password" />
				</p>
				 <p class="p-container">
			    <input type="submit" value="Login">
			  </p>
			</div>
		</form:form>
	</div>
</body>
</html>