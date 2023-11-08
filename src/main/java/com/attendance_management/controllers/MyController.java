package com.attendance_management.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.attendance_management.datasource.Admin;
import com.attendance_management.entity.Attendance;
import com.attendance_management.entity.Batch;
import com.attendance_management.entity.Image;
import com.attendance_management.entity.User;
import com.attendance_management.service.HrService;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Controller
public class MyController {

	@Autowired
	@Qualifier("serviceBean")
	private HrService hrService;

	@GetMapping("/saveattendance")
	public String saveAttendance() {
		System.out.println("hello");
		return "index.jsp";
	}

	@GetMapping("/login")
	public String getDashboard(ModelMap modelMap, HttpServletRequest req, @RequestParam String email,
			@RequestParam String password) {
		
		User user = hrService.findUserByEmailAndPassword(email, password);

		if (user == null) {
			modelMap.addAttribute("msg", "Invalid user details!");
			modelMap.addAttribute("msgType", "orange");
			return "Login.jsp";
		}

		req.getSession().setAttribute("currentUser", user);

		List<Batch> batches = null;

		if (user.getRole().equals("HR")) {
			batches = hrService.findAllBatches();
//			for (int i = 0; i < batches.size(); i++) {
//				Batch b = batches.get(i);
//				LocalDateTime currentSavedDate = b.getCreatedDateTime();
//				if (!LocalDateTime.now().withDayOfMonth(0).withYear(0).withMonth(0).isBefore(currentSavedDate)) {
//					b.setStatus(true);
//				}
//			}

		} else if (user.getRole().equals("Trainer"))
			batches = user.getBatches();

		req.getSession().setAttribute("allBatches", batches);

		modelMap.addAttribute("msg", "Welcome " + user.getName().split(" ")[0] + "!");
		modelMap.addAttribute("msgType", "white");
		return "HRDashboard.jsp";
	}

	@PostMapping("/createaccount")
	public String createAccount(ModelMap modelMap, HttpServletRequest req, @RequestParam String email,
			@RequestParam String password, @RequestParam String role, @RequestParam String username,
			@RequestParam long phone) {

		// Verifying does user already exist
		User user = hrService.findUserByEmail(email);
		User userByPhone = hrService.findUserByPhone(phone);

		if (user != null || userByPhone != null) {
			modelMap.addAttribute("msg", "Email or phone not available");
			modelMap.addAttribute("msgType", "orange");
			return "CreateAccount.jsp";
		}

		user = new User();

		user.setEmail(email);
		user.setName(username);
		user.setPassword(password);
		user.setPhone(phone);
		user.setRole(role);

		// Storing new user into session
		// so that we can save user after OTP verification
		req.getSession().setAttribute("newUser", user);

		// sending verification OTP

		String from = Admin.EMAIL;
		String to = email;
		String subject = "HRM Verification: You have recieved OTP for account verification from Attendance Management SPRING-MVC-WEB-APP!";
		String OTP = generateOTP();
		String content = "Verification OTP : " + OTP;

		boolean isSend = sendEmail(from, to, subject, content);

		if (isSend) {
			modelMap.addAttribute("OTP", OTP);
			modelMap.addAttribute("msg", "OTP sent on email!");
			modelMap.addAttribute("msgType", "white");
			return "OTPVerification.jsp";
		}

		modelMap.addAttribute("msg", "OTP generation failed!");
		modelMap.addAttribute("msgType", "orange");
		return "CreateAccount.jsp";
	}

	private String generateOTP() {
		while (true) {
			int otp = (int) (Math.random() * 1000000) + 1;
			if (otp > 99999)
				return String.valueOf(otp);
		}
	}

	@GetMapping("/verifyOTP")
	public String verifyOTP(ModelMap modelMap, HttpServletRequest req, @RequestParam String OTP,
			@RequestParam String ogOTP) {

		// OTP Verification failed
		if (!OTP.equals(ogOTP)) {
			modelMap.addAttribute("msg", "Incorrect OTP!");
			modelMap.addAttribute("msgType", "orange");

			req.getSession().invalidate();
			return "CreateAccount.jsp";
		}

		// Verification success
		System.out.println("OTP Verification successfull");

		// save user
		User user = (User) req.getSession().getAttribute("newUser");
		System.out.println(user);
		user = hrService.saveUser(user);

		if (user == null) {
			modelMap.addAttribute("msg", "Server Error!");
			modelMap.addAttribute("msgType", "orange");

			req.getSession().invalidate();
			return "CreateAccount.jsp";
		}

		// mail sent successfully
		req.getSession().invalidate();

		modelMap.addAttribute("msg", "Account has been created successfully!");
		modelMap.addAttribute("msgType", "white");

		// navigate to login
		return "Login.jsp";
	}

	public boolean sendEmail(String from, String to, String subject, String content) {

		Properties properties = new Properties();

		// smtp properties
		properties.put("mail.smtp.auth", true);
		properties.put("mail.smtp.starttls.enable", true);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.host", "smtp.gmail.com");

		// session

		final String username = Admin.EMAIL;
		final String password = Admin.PASSWORD;

		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		// message

		Message message = new MimeMessage(session);

		try {
			// From
			InternetAddress addressFrom = new InternetAddress(from);
			message.setFrom(addressFrom);

			// To
			InternetAddress addressTo = new InternetAddress(to);
			message.setRecipient(Message.RecipientType.TO, addressTo);

			message.setSubject(subject);
			message.setText(content);

			// Sending message
			Transport.send(message);

			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

	@PostMapping("addbatch")
	public String saveNewBatch(ModelMap modelMap, HttpServletRequest req, @RequestParam String subject,
			@RequestParam String empId) {

		User trainer = hrService.findUserByEmployId(empId);

		if (trainer == null || !trainer.getRole().equals("Trainer")) {
			modelMap.addAttribute("msg", "Invalid Trainer ID");
			modelMap.addAttribute("msgType", "orange");
			return "HRDashboard.jsp";
		}

		LocalDateTime dateTime = LocalDateTime.now().plusHours(24 * 5);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-YYYY");
		String encryptedDate = Admin.encryptDate(dateTime.format(formatter));

		System.out.println("Date of Assigning from addBatch : " + encryptedDate);

		// Sending mail to the trainer
		String from = Admin.EMAIL;
		String to = trainer.getEmail();
		String mailSubject = "HRM Invitation | New " + subject + " Batch is goining to start";

		// Recreating URL by replacing Spaces with %20
		String subjectURL = "";
		String[] subjectURLArray = subject.split(" ");
		for (int i = 0; i < subjectURLArray.length; i++) {
			boolean isLast = (i == subjectURLArray.length - 1);
			String str = subjectURLArray[i];
			subjectURL += isLast ? str : (str + "%20");
		}

		modelMap.addAttribute("msg", "Batch added, waiting for trainer response!");
		modelMap.addAttribute("msgType", "white");

		User user = (User) req.getSession().getAttribute("currentUser");

		if (user == null) {
			req.setAttribute("msg", "Please login first!");
			req.setAttribute("msgType", "orange");
			return "Login.jsp";
		}

		modelMap.addAttribute("currentUser", user);

		Batch batch = hrService.saveBatch(subject);

		String URL = Admin.BaseURL + "BatchDate.jsp?empId=" + empId + "&subject=" + subjectURL + "&batchId="
				+ batch.getId();
		String content = "Open the link and set batch starting date - " + URL;

		boolean isSent = sendEmail(from, to, mailSubject, content);

		if (!isSent) {
			modelMap.addAttribute("msg", "Email wasn't sent!");
			modelMap.addAttribute("msgType", "orange");
			return "Trainers.jsp";
		}

		List<Batch> trainerBatches = trainer.getBatches();
		trainerBatches.add(batch);
		trainer.setBatches(trainerBatches);
		hrService.updateUser(trainer);

		List<Batch> batches = (List<Batch>) req.getSession().getAttribute("allBatches");
		batches.add(batch);
		req.getSession().setAttribute("allBatches", batches);
		return "Trainers.jsp";
	}

	@GetMapping("logout")
	public String logout(ModelMap modelMap, HttpServletRequest req) {

		req.getSession().invalidate();
		modelMap.addAttribute("msg", "Log out successful!");
		modelMap.addAttribute("msgType", "white");

		return "index.jsp";
	}

	@GetMapping("togglebatchstatus")
	public String toggleBatchStatus(ModelMap modelMap, HttpServletRequest req, @RequestParam int id) {

		List<Batch> batches = (List<Batch>) req.getSession().getAttribute("allBatches");

		if (batches == null) {
			req.setAttribute("msg", "Please login first!");
			req.setAttribute("msgType", "orange");
			return "Login.jsp";
		}

		for (int i = 0; i < batches.size(); i++) {
			Batch b = batches.get(i);
			if (b.getId() == id) {
				b.setStatus(!b.isStatus());
				hrService.updateBatch(b);
				break;
			}
		}

		req.getSession().setAttribute("allBatches", batches);

		return "HRDashboard.jsp";
	}

	@GetMapping("/cancelrequest")
	public String cancelRequest(HttpServletRequest req) {
		req.getSession().invalidate();
		return "index.jsp";
	}

	@PostMapping(path = "/setbatchdate", consumes = { "MULTIPART/FORM-DATA" })
	public String setBatchdate(ModelMap modelMap, HttpServletRequest req, @RequestParam("empId") String empId,
			@RequestParam String batchDate, @RequestParam String subject,
			@RequestParam("screenshot") MultipartFile screenshot) throws IOException {

		// Check if the file exists and is not a directory
		if (screenshot == null || screenshot.isEmpty()) {
			modelMap.addAttribute("msg", "Screenshot file is missing or invalid.");
			modelMap.addAttribute("msgType", "orange");
			return "index.jsp";
		}

		try {
			System.out.println("Servlet: " + empId);
			System.out.println("Screenshot: " + screenshot.getName());
			System.out.println("Batch Date: " + batchDate);

			int yyyy = Integer.parseInt(batchDate.split("-")[0]);
			int mm = Integer.parseInt(batchDate.split("-")[1]);
			int dd = Integer.parseInt(batchDate.split("-")[2]);

			LocalDateTime startOn = LocalDateTime.of(yyyy, mm, dd, 0, 0, 0);

			System.out.println("Screenshot bytes: " + Arrays.toString(screenshot.getBytes()));

			User trainer = hrService.findUserByEmployId(empId);

			if (trainer == null) {
				modelMap.addAttribute("msg", "Trainer not found.");
				modelMap.addAttribute("msgType", "orange");
				return "index.jsp";
			}

			int batchId = Integer.parseInt(req.getParameter("batchId"));
			Batch batch = hrService.findBatchById(batchId);

			for (Batch b : trainer.getBatches()) {

				System.out.println(b);

				// Considering one trainer can't have multiple batches
				// of same subject at different starting dates
				if (b.getSubject().equals(subject) && b.getCreatedDateTime() == null)
					batch = b;

				// Write code for another scenario

			}

			if (batch == null) {
				modelMap.addAttribute("msg", "Multiple batches for same subject found.");
				modelMap.addAttribute("msgType", "orange");
				return "index.jsp";
			}

			if (batch.isStatus()) {
				modelMap.addAttribute("msg", "Batch already started.");
				modelMap.addAttribute("msgType", "orange");
				return "index.jsp";
			}

			batch.setCreatedDateTime(startOn);

			Attendance attendance = new Attendance();

			Image image = new Image();

			image.setFile(screenshot.getBytes());

			attendance.setImage(image);

			List<Attendance> attendances = new ArrayList<Attendance>();
			attendances.add(attendance);

			batch.setAttendances(attendances);
			batch.setStatus(true);

			hrService.updateBatch(batch);

			req.getSession().invalidate();
			modelMap.addAttribute("msg", "Batch will be started on " + batchDate);
			modelMap.addAttribute("msgType", "white");
		} catch (Exception e) {
			modelMap.addAttribute("msg", "Error: " + e.getMessage());
			modelMap.addAttribute("msgType", "orange");
		}

		return "index.jsp";
	}

	/**
	 * 
	 * @param flag
	 * @return true if request came from HRDashboard.jsp, else false
	 */

	@GetMapping("togglebatchattendance")
	public String toggleBatchAttendance(HttpServletRequest req, @RequestParam boolean flag, @RequestParam int id) {

		User user = (User) req.getSession().getAttribute("currentUser");

		if (user == null) {
			req.setAttribute("msg", "Please login first!");
			req.setAttribute("msgType", "orange");
			return "Login.jsp";
		}

		if (flag) {
			List<Attendance> attendances = hrService.findBatchById(id).getAttendances();
			req.getSession().setAttribute("attendances", attendances);
			return "HRAttendance.jsp";
		}
		return "HRDashboard.jsp";
	}

	@GetMapping("profile")
	public String getProfile(HttpServletRequest req) {

		return "Profile.jsp";
	}

	@GetMapping("trainers")
	public String getTrainers(HttpServletRequest req) {
		List<User> attendances = hrService.findUserByEmployeeByRole("Trainer");
		req.getSession().setAttribute("trainerslist", attendances);
		return "Trainers.jsp";
	}

	@PostMapping("addtrainer")
	public String createTrainer(ModelMap modelMap, HttpServletRequest req, @RequestParam String email,
			@RequestParam String password, @RequestParam String username, @RequestParam long phone) {

		// Verifying does user already exist
		User user = hrService.findUserByEmail(email);
		User userByPhone = hrService.findUserByPhone(phone);

		if (user != null || userByPhone != null) {
			modelMap.addAttribute("msg", "Email or phone not available");
			modelMap.addAttribute("msgType", "orange");
			return "Trainers.jsp";
		}

		user = new User();

		user.setEmail(email);
		user.setName(username);
		user.setPassword(password);
		user.setPhone(phone);
		user.setRole("Trainer");

		user = hrService.saveUser(user);
		
		List<User> attendances = hrService.findUserByEmployeeByRole("Trainer");
		req.getSession().setAttribute("trainerslist", attendances);
		
		modelMap.addAttribute("msg", "Trainer added successfully!");
		modelMap.addAttribute("msgType", "white");
		return "Trainers.jsp";
	}

}