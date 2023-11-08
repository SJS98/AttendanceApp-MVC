package com.attendance_management.datasource;

public interface Admin {
	String EMAIL = "sshaikh1671@gmail.com";
	String PASSWORD = "njzadgnuvrbdecuq";
	String BaseURL = "http://localhost:8080/attendance-mvc-app/";

	public static String encryptDate(String date) {

		String encryptDate = "";

		for (char c : date.toCharArray()) {
			if (c != '-') {
				c += c + 1;
				encryptDate += c + "|";
			} else {
				encryptDate += "-";
			}
		}

		return encryptDate;
	}

	public static String decryptDate(String encryptedDate) {

		String originalDate = "";
		
		String[] dateArr = encryptedDate.split("-");
		String dd = dateArr[0];
		char ddValue = ((char)(dd.charAt(0)-1)); 
		System.out.println(ddValue);
		String mm = dateArr[1];
		String yyyy = dateArr[2];

		
		
		
		System.out.println("Encrypted Date: " + encryptedDate);
		System.out.println("Original Date: " + originalDate);

		return originalDate;
	}
}
