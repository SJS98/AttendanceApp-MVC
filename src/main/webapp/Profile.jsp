<%@page import="com.attendance_management.entity.Image"%>
<%@page import="com.attendance_management.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HRM | Profile</title>

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
	max-height: 45px;
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
	width: 85px;
	display: flex;
	align-content: center;
	justify-content: center;
	padding: 10px 20px;
	background: #000;
	border-radius: 3px;
	color: #fff;
	transition: .1s ease-in-out;
	align-content: center;
	justify-content: center;
}

nav ul li:hover {
	background: #fff;
	color: #000;
	font-weight: bold;
	cursor: pointer;
}

nav ul li:active {
	transform: scale(.9);
}

nav span {
	margin-left: 120px;
	font-size: 25px;
	font-weight: bold;
}

.mid-section {
	background: rgba(255, 255, 255, .5);
	height: 350px;
	width: 600px;
	position: absolute;
	top: 150px;
	left: 400px;
	position: absolute;
	top: 150px;
	left: 400px;
	box-shadow: 0 10px 15px -10px black;
}

.mid-section h1 {
	margin-top: 50px;
	text-align: center;
	width: 100vw;
}

.mid-section p {
	text-align: center;
	width: 70vw;
	margin: 0 auto;
	margin-top: 70px;
}

.mid-section .btn {
	width: 100vw;
	display: flex;
	justify-content: center;
	margin-top: 70px;
}

.mid-section .btn button {
	height: 30px;
	width: 100px;
	border: none;
	border-radius: 10px;
	background: #000;
	color: #fff;
	font-weight: bold;
	margin: 25px;
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
	top: 10px;
	width: 100vw;
	display: flex;
	justify-content: center;
	align-items: center;
}

.id-th, .eid-th, .n-th, .e-th, .p-th, .e-th , .r-th, .ni-th{
	width: 300px;
	height: 50px;
	display: flex;
	align-items: center;
	padding-left: 20px;
}

.pi-th {
	font-size: 20px;
	padding: 5px;
}

.pi-td img {
	height: 100px;
	margin-left: -50px;
}

.mid-section table tr {
	width: 600px;
	display: flex;
	justify-content:start;
	align-items: center;
}

.mid-section table tr th {
	width: 200px;
}

.mid-section table tr td {
	width: 300px;
	height: 50px;
	display: flex;
	align-items: center;
	text-align: center;
}
.ni-td{
	margin-left: -150px; 
}
.pi-th {
	height: 170px;
}

.pi-td {
	max-width: 100px;
}

.e-th{
	padding: 0;
}

</style>

</head>

<body>

	<div class="msg-div">
		<span id="msg-dialoge"
			style="display:${msg == null ? 'none':'flex'} ;background: ${msg == null ? '' :msgType}">${msg == null ? '' : msg}</span>
	</div>

	<%
	
	User user = (User) session.getAttribute("currentUser");

	if(user == null){
		request.setAttribute("msg", "Please login first!");
		request.setAttribute("msgType", "orange");
		request.getRequestDispatcher("Login.jsp").forward(request, response);
		return;
	}
	
	Image base64ProfileImage = user.getProfileImage();
	%>

	<nav>
		<span>HRM</span>
		<ul>
			<li class="homepage" onclick="location.href='HRDashboard.jsp'">Home</li>
			<% if(user.getRole().equals("HR")){%>
			<li class="hr" onclick="location.href='trainers'">Trainers</li>
			<%} %><li class="hr">Profile</li>
			<li class="about" onclick="location.href='logout'">Log out</li>
		</ul>
	</nav>

	<div class="mid-section">

		<table>

			<tr>
				<th class="pi-th">Profile</th>
				<td class="pi-td"><img
					src="<%=base64ProfileImage == null ? "https://www.pngall.com/wp-content/uploads/5/Profile.png"
		: ("data:image/jpeg,base64," + base64ProfileImage)%>"></td>
				<th></th>
				<td class="ni-td">
					<h3>
						Name:
						<%=user.getName()%>
					</h3>
				</td>

			</tr>

			<tr>
				<th class="eid-th">EMP ID:</th>
				<td class="eid-td"><%=user.getEmployId()%></td>
				<th class="eid-th"></th>
				<td class="eid-td"></td>
			</tr>

			<tr>
				<th class="r-th">Role:</th>
				<td class="r-td"><%=user.getRole()%></td>
				<th class="e-th">Email:</th>
				<td class="e-td"><%=user.getEmail()%></td>
			</tr>

			<tr>
				<th class="p-th">Password:</th>
				<td class="p-td"><%=user.getPassword()%></td>
				<th class="e-th">Phone:</th>
				<td class="e-td"><%=user.getPhone()%></td>
			</tr>

		</table>
	</div>

</body>
</html>