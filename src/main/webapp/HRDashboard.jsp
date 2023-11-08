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
User user = (User) session.getAttribute("currentUser");

if (user == null) {
	request.setAttribute("msg", "Please login first!");
	request.setAttribute("msgType", "orange");
	request.getRequestDispatcher("Login.jsp").forward(request, response);
	return;
}

List<Batch> batches = (List<Batch>) session.getAttribute("allBatches");
%>

<head>
<meta charset="UTF-8">
<title>HR | Dashboard</title>

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
	max-height: <%=batches.isEmpty() ? "" : "350px"%>;
	min-height: <%=batches.isEmpty() ? "" : "350px"%>;
}

.mid-section table tr {
	padding: 0;
	display: flex;
	align-items: center;
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
	width: 100px;
}

.mid-section table tr .sub-th {
	width: 200px;
}

.mid-section table tr .sdt-th, .cdt-th {
	width: 200px;
}

.mid-section table tr .id-td {
	width: 100px;
}

.mid-section table tr .sub-td {
	width: 200px;
}

.mid-section table tr .sdt-td, .cdt-td {
	width: 200px;
	border-radius: 5px;
}

.mid-section table tr .st-td, .st-th, .st-td2 {
	text-align: center;
	padding: 5px 10px;
	margin: 0 10px;
	border-radius: 5px;
	padding: 5px 10px;
}

.mid-section table tr .st-sd {
	
}

.mid-section table tr .st-td2:hover {
	padding: 5px 9px;
	display: flex;
	align-items: center;
	border-radius: 5px;
	border: 1px solid gray;
	cursor: pointer;
	box-sizing: border-box;
}

.mid-section table tr .st-td2:active {
	transform: scale(1.1);
}

.no-batches {
	width: 100%;
	margin-top: 50px;
	text-align: center;
}

#new-batch-popup {
	display: none;
	margin-top: -120vh;
	height: 105vh;
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
			<li class="homepage">Home</li>
			<%
			if (user.getRole().equals("HR")) {
			%>
			<li class="hr" onclick="location.href='trainers'">Trainers</li>
			<%
			}
			%><li class="hr" onclick="location.href='profile'">Profile</li>
			<li class="about" onclick="location.href='logout'">Log out</li>
		</ul>
	</nav>

	<div class="mid-section">
		<div class="nav">
			<button>Batches</button>
		</div>
		<table>
			<tr>
				<th class="id-th">ID</th>
				<th class="sub-th">Subject</th>
				<th class="sdt-th">Started On</th>
				<th class="cdt-th">Completed On</th>
				<th class="st-th">Status</th>
				<th class="st-th">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</th>
			</tr>

			<%
			if (batches != null && !batches.isEmpty()) {

				//sorting
				batches = batches.stream().sorted((s1, s2) -> {
					if (s1.isStatus())
				return -1;
					return 1;
				}).toList();

				for (Batch b : batches) {

					LocalDateTime createdDateTime = b.getCreatedDateTime();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			%>
			<tr>
				<td class="id-td"><%=b.getId()%></td>
				<td class="sub-td"><%=b.getSubject()%></td>
				<td class="sdt-td"
					style="background:<%=!b.isStatus() ? "orange" : "lightgray"%>;"><%=!b.isStatus() ? " Response Pending " : createdDateTime%></td>
				<td class="cdt-td">-</td>
				<td class="st-td"
					onclick="<%=!user.getRole().equals("Trainer") ? "" : "location.href='togglebatchstatus?id=" + b.getId() + "'"%>"
					style="background:<%=!b.isStatus() ? "orange" : "lightgray"%>;"><%=b.isStatus() ? "Active" : "Inactive"%></td>
				<td class="st-td2"
					style="display: <%=user.getRole().equals("HR") ? "" : "none"%>"
					onclick="location.href='togglebatchattendance?flag=true&id=<%=b.getId()%>'">
					More</td>
			</tr>
			<%
			}
			}
			%>
		</table>

		<%
		if (batches == null || batches.isEmpty()) {
		%>
		<h2 class="no-batches">No Batches!</h2>
		<%
		}
		%>
	</div>

</body>
</html>