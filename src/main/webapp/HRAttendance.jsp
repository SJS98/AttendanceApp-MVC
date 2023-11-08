<%@page import="java.util.Base64"%>
<%@page import="com.attendance_management.entity.Image"%>
<%@page import="com.attendance_management.entity.Attendance"%>
<%@page import="org.hibernate.internal.build.AllowSysOut"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="com.attendance_management.entity.User"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.util.List"%>
<%@page import="com.attendance_management.entity.Batch"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html>
<html>
<%
List<Attendance> attendances = (List<Attendance>) session.getAttribute("attendances");

if (attendances == null) {
	request.setAttribute("msg", "Please login first!");
	request.setAttribute("msgType", "orange");
	request.getRequestDispatcher("Login.jsp").forward(request, response);
	return;
}
%>

<head>
<meta charset="UTF-8">
<title>HRM | Attendance</title>

<style>
* {
	margin: 0;
	padding: 0;
	font-family: 'Courier New', Courier, monospace;
}

body {
	background: linear-gradient(5deg, skyblue 0%, skyblue 50%, lightgreen 50%,
		lightgreen 100%);
	overflow: hidden; /* Hide scrollbars */
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
	width: 80px;
	display: flex;
	align-content: center;
	justify-content: center;
	padding: 10px 20px;
	background: #000;
	border-radius: 3px;
	color: #fff;
	transition: .1s ease-in-out;
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
	height: 450px;
	width: 80vw;
	margin: 150px auto;
	border-radius: 10px;
	box-shadow: 0 0 10px -5px;
	padding-top: 20px;
}

.mid-section table {
	
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

.mid-section .nav {
	width: 100%;
	display: flex;
	align-items: center;
	justify-content: space-between;
	height: 35px;
}

.mid-section .nav button {
	margin: 20px 20px;
	height: 35px;
	border: none;
	border-radius: 5px;
	background: #fff;
	height: 35px;
	padding: 10px;
	transition: .05s ease-in-out;
}

.mid-section .nav button:hover {
	cursor: pointer;
}

.mid-section .nav button:active {
	transform: scale(.9);
	opacity: .7;
}

.mid-section table {
	margin-top: 20px;
	margin-left: 9.5vw;
	padding: 1vw;
	box-shadow: inset 0 0 10px -5px;
	overflow: scroll;
	border-radius: 5px;
	max-height: <%=attendances.isEmpty() ? "" : "350px"%>;
	min-height: <%=attendances.isEmpty() ? "" : "350px"%>;
}

.mid-section .tr-header {
	padding: 0;
	display: flex;
	align-items: center;
	background: #fff;
	height: 35px;
	border-radius: 5px;
	width: 60vw;
	margin-top: 10px;
}

.mid-section .tr-data {
	padding: 0;
	display: flex;
	align-items: center;
	justify-content: center;
	background: #fff;
	height: 35px;
	border-radius: 5px;
	width: 60vw;
	margin-top: 10px;
}

.mid-section table tr th, .mid-section table tr td {
	text-align: center;
}

.mid-section table tr .id-th {
	width: 150px;
}

.id-td {
	width: 100px;
}

.mid-section table tr .sub-th {
	width: 250px;
}

.mid-section table tr .sdt-th, .cdt-th {
	width: 250px;
}

.mid-section table tr .sub-td {
	width: 250px;
}

.mid-section table tr .sdt-td, .cdt-td {
	width: 250px;
	border-radius: 5px;
}

.mid-section table tr .st-td, .st-th {
	padding: 5px 10px;
	border-radius: 5px;
	transition: .15s;
	width: 120px;
}

.mid-section table tr .st-td:hover {
	background: gray;
	color: white;
	cursor: pointer;
}

.mid-section table tr .ss-td img {
	position: absolute;
	top: 20vh;
	left: calc(50vw - 400px);
	height: 70vh;
	width: 800px;
	border-radius: 10px;
	box-shadow: 0 0 10px -7px;
	cursor: crosshair;
}

.mid-section table tr .ss-td img:active {
	transform: scale(.5);
}

.no-batches {
	width: 100%;
	margin-top: 50px;
	text-align: center;
}

#new-batch-popup {
	display: none;
	margin-top: -120vh;
	height: 100vh;
	width: 100wv;
	background: rgba(0, 0, 0, .2);
	backdrop-filter: blur(1px);
	z-index: 11;
	position: relative;
}

#new-batch-popup .popup {
	position: absolute;
	height: 190px;
	width: 400px;
	background: #fff;
	top: 220px;
	left: 480px;
	border-radius: 10px;
	box-shadow: 0 0 10px -5px;
}

#new-batch-popup .popup p {
	margin: 10px;
	width: 100%;
	text-align: center;
}

#close-btn {
	min-height: 30px;
	min-width: 30px;
	max-width: 30px;
	display: flex;
	justify-content: center;
	align-items: center;
	border-radius: 0 10px 0 10px;
	background: lightgray;
	position: absolute;
	right: 0;
	box-shadow: inset 0 0 5px -2px;
}

#close-btn:hover {
	cursor: pointer;
}

.add-batch input {
	text-align: center;
	height: 30px;
	width: 300px;
	border: 1px solid;
	border-radius: 5px;
	outline: none;
	box-sizing: border-box;
	margin: 5px 45px;
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
	z-index: 9;
}

[id^=image] {
	transition: .5s;
	display: none;
}

[id^=image]:hover {
	cursor: crosshair;
}
</style>

</head>
<body>

	<div class="msg-div">
		<span id="msg-dialoge"
			style="display:${msg == null ? 'none':'flex'} ;background: ${msg == null ? '' :msgType}">${msg == null ? '' : msg}</span>
	</div>

	<nav>
		<span>HRM</span>
		<ul>
			<li class="homepage" onclick="location.href='HRDashboard.jsp'">Home</li>
			<% 
			User user = (User) session.getAttribute("currentUser");

			if(user.getRole().equals("HR")){%>
			<li class="hr" onclick="location.href='trainers'">Trainers</li>
			<%} %>
			<li class="hr" onclick="location.href='profile'">Profile</li>
			<li class="about" onclick="location.href='logout'">Log out</li>
		</ul>
	</nav>

	<div class="mid-section">

		<div class="nav">
			<button
				onclick="location.href='togglebatchattendance?flag=false&id=0'">Back</button>
		</div>

		<table>
			<tr class="tr-header">
				<th class="id-th">ID</th>
				<th class="sub-th">Number of Students</th>
				<th class="sdt-th">Started On</th>
				<th class="st-th">Screenshot</th>
			</tr>

			<%
			if (attendances != null && !attendances.isEmpty()) {

				for (Attendance attendance : attendances) {

					LocalDateTime createdDateTime = attendance.getCreatedDateTime();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

					//Decoding image
					Image image = attendance.getImage();
					String base64Image = Base64.getEncoder().encodeToString(image.getFile());
			%>
			<tr class="tr-data">
				<td class="id-td"><%=attendance.getId()%></td>
				<td class="sub-td"><%=attendance.getNumOfStudent()%></td>
				<td class="cdt-td"><%=createdDateTime.format(formatter)%></td>
				<td class="st-td" id="trData"
					onclick="showImage('image<%=attendance.getId()%>')">View</td>
				<td class="ss-td"><img id="image<%=attendance.getId()%>"
					onclick="hideImage('image<%=attendance.getId()%>')"
					src="data:image/jpeg;base64, <%=base64Image%>" alt="Image" /></td>
			</tr>
			<%
			}
			}
			%>
		</table>
		<%
		if (attendances == null || attendances.isEmpty()) {
		%>
		<h2 class="no-batches">No attendance!</h2>
		<%
		}
		%>
	</div>


	<script type="text/javascript">
		function showImage(imageId) {
			console.log(document.getElementById("trData"));
			document.getElementById(imageId).style.display = "block";
		}

		function hideImage(imageId) {
			document.getElementById(imageId).style.display = "none";
		}
	</script>
</body>
</html>