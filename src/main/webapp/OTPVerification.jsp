<%@page import="org.hibernate.internal.build.AllowSysOut"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Welcome to HRM</title>

<style>
* {
	margin: 0;
	padding: 0;
	font-family: 'Courier New', Courier, monospace;
}

body {
	background: linear-gradient(5deg , skyblue 0%, skyblue 50%, lightgreen 50%, lightgreen 100%);
	height: 100vh;
}

nav {
	position: absolute;
	top: 50px;
	left: 0;
	height: 45px;
	width: 100%;
	display: flex;
	justify-content: space-between;
	align-items: center;
}

nav ul {
	list-style: none;
	display: flex;
	width: 40%;
	justify-content: space-evenly;
	font-size: 18px;
}

nav ul li {
	width: 70px;
	display: flex;
	align-content: center;
	justify-content: center;
	padding: 10px 20px;
	background: #000;
	border-radius: 3px;
	color: #fff;
	transition: .1s ease-in-out;
	padding: 10px 20px;
}

nav ul li:hover {
	background: #fff;
	color: #000;
	font-weight: bold;
}

nav span {
	margin-left: 120px;
	font-size: 25px;
	font-weight: bold;
}

.loginform {
	position: absolute;
	top: 200px;
	height: 350px;
	width: 100vw;
	display: flex;
	justify-content: center;
	align-items: center;
}

.loginform form {
	padding-top: 60px;
	height: 200px;
	width: 300px;
	background: rgba(255, 255, 255, .5);
	text-align: center;
	box-shadow: 0 0 10px -7px;
	border-radius: 4px;
	
}

.loginform form input[type="number"], .loginform form input[type="button"],
	.loginform form input[type="submit"] {
	width: 250px;
	height: 30px;
	border: none;
	outline: none;
	border-radius: 5px;
	margin-top: 20px;
}
.loginform form input[type="number"]{
	text-align: center;
}
.loginform form input[type="email"], .loginform form input[type="password"]
	{
	padding-left: 15px;
}

.loginform form input[type="button"], .loginform form input[type="submit"]
	{
	background: #000;
	color: #fff;
}

.loginform form input[type="button"]:hover, .loginform form input[type="submit"]:hover
	{
	background: #fff;
	color: #000;
	font-weight: bold;
}
.loginform a{
	color: #000;
	position: absolute;
	bottom: 5px;
	left: 585px;
}
#msg-dialoge{
	padding:10px 15px;
	font-weight:bold;
	border-radius: 10px;
	margin-top: 10px;
	text-align: center;
	box-shadow: 0 0 10px -8px black;
		 
}
.msg-div{
	position: absolute;
	width: 100vw;
	display: flex;
	justify-content: center;
	align-items: center;
}
</style>

</head>
<body>

	<div class="msg-div"><span id="msg-dialoge" style="display:${msg == null ? 'none':'flex'} ;background: ${msg == null ? '' :msgType}">${msg == null ? '' : msg}</span></div>
	<nav>

		<span>HRM</span>
		<ul>
			<li class="homepage" onclick="location.href='index.jsp'">Home</li>
			<li class="hr" style="background-color: lightgray">Log In</li>
			<li class="about">About</li>
		</ul>

	</nav>

	<div class="loginform">
		<form action="verifyOTP">
			<h3>OTP Verification</h3>
			<input type="number" id="otp" name="OTP" placeholder="Enter OTP" required>
			<input type="hidden" name="ogOTP" value="${OTP}">
			<input type="submit" value="Verify"> <input type="button"
				value="Cancle" onclick="location.href='cancelrequest'">
		</form>
	</div>
</body>
</html>