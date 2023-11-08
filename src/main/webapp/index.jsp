<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
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
	position: absolute;
	top: 150px;
	box-shadow: 0 0 10px -7px black;
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
			<li class="hr" onclick="location.href='Login.jsp'">Log In</li>
			<li class="about">About</li>
		</ul>
	</nav>

	<div class="mid-section">
		<h1>Welcome to the Attendance Management</h1>
		<p>Attendance is the action or state of being present at a place
			of work according to the company's policies. The opposite of
			attendance is absence from work.</p>
		<div class="btn">
			<button class="explore">Explore</button>
			<button class="readmore">Read More</button>
		</div>
	</div>

</body>
</html>