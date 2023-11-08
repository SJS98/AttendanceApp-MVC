<%@page import="com.attendance_management.entity.Batch"%>
<%@page import="com.attendance_management.dao.BatchDao"%>
<%@page import="com.attendance_management.datasource.Admin"%>
<%@page import="org.hibernate.internal.build.AllowSysOut"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="org.springframework.format.annotation.DateTimeFormat"%>
<%@page import="java.time.LocalDateTime"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HRM | Log In</title>

<style>
* {
	margin: 0;
	padding: 0;
	font-family: 'Courier New', Courier, monospace;
}

body {
	background: linear-gradient(5deg, skyblue 0%, skyblue 50%, lightgreen 50%,
		lightgreen 100%);
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
	height: 340px;
	width: 300px;
	background: rgba(255, 255, 255, .5);
	text-align: center;
	box-shadow: 0 0 10px -7px;
	border-radius: 4px;
}

.loginform form input, select, .loginform form input[type="button"],
	.loginform form input[type="submit"] {
	width: 250px;
	height: 30px;
	border: none;
	outline: none;
	border-radius: 5px;
	margin-top: 20px;
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

.loginform a {
	color: #000;
	position: absolute;
	bottom: 5px;
	left: 585px;
}

#msg-dialoge {
	padding: 10px 15px;
	font-weight: bold;
	border-radius: 10px;
	margin-top: 10px;
	text-align: center;
	box-shadow: 0 0 10px -8px black;
}

.msg-div {
	position: absolute;
	width: 100vw;
	display: flex;
	justify-content: center;
	align-items: center;
}

.loginform label {
	padding: 10px;
	width: 230px;
	background: lightgray;
	position: absolute;
	bottom: 90px;
	left: 553px;
	border-radius: 10px;
	cursor: pointer;
	box-shadow: 0 0 10px -7px;
}

.loginform label p {
	transition: .15s;
}

.loginform label:hover>p {
	transform: scale(.8);
}

.loginform input[type="date"], .loginform input[type="text"] {
	text-align: center;
}

.loginform input[type="file"] {
	visibility: hidden;
}

.loginform input[type="text"] {
	border: 1px solid gray;
}
</style>

</head>
<body>
	<%
	LocalDateTime date = LocalDateTime.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	String todayDate = date.format(formatter);
	%>

	<%
	String empId = request.getParameter("empId");
	String subject = request.getParameter("subject");
	int bid = Integer.parseInt(request.getParameter("batchId"));
	Batch batch = new BatchDao().findById(bid);
	String expirydate = batch.getCreatedDateTime().format(formatter);

	//expirydate = Admin.decryptDate(expirydate);

	int dd = Integer.parseInt(expirydate.split("-")[2]);
	int mm = Integer.parseInt(expirydate.split("-")[1]);
	int yyyy = Integer.parseInt(expirydate.split("-")[0]);

	LocalDateTime expiryDate = LocalDateTime.of(yyyy, mm, dd, 0, 0).plusHours(24 * 4);

	String next5thDate = expiryDate.format(formatter);

	boolean isExpired = date.isAfter(expiryDate);
	%>
	<div class="msg-div">
		<span id="msg-dialoge"
			style="display:${msg == null ? 'none':'flex'} ;background: ${msg == null ? '' :msgType}">${msg == null ? '' : msg}</span>
		<span id="msg-dialoge"
			style="background: orange; display: <%=isExpired ? "flex" : "none"%>">Link
			is no longer active!<%=expiryDate%></span>
	</div>

	<nav>
		<span>HRM</span>
		<ul>
			<li class="homepage" onclick="location.href='index.jsp'">Home</li>
			<li class="about">About</li>
		</ul>
	</nav>

	<div class="loginform">
		<form action="setbatchdate" method="POST"
			enctype="multipart/form-data">

			<h3>Set Batch Date</h3>
			<input type="text" name="empId" value="<%=empId%>" readonly>
			<input type="hidden" name="batchId" value="<%=bid%>">
			<input type="text" name="subject" value="<%=subject%>" readonly>
			<input type="date" name="batchDate" placeholder="Batch Date"
				min="<%=todayDate%>" max="<%=next5thDate%>" required
				<%=isExpired ? "disabled" : ""%>> <label for="screenshot"><p>Screenshot</p></label>
			<input type="file" id="screenshot" name="screenshot"><input
				type="submit" value="Set Date" <%=isExpired ? "disabled" : ""%>>
		</form>
	</div>

</body>
</html>