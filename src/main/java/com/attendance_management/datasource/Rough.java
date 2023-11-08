package com.attendance_management.datasource;

public class Rough {

	public static void main(String[] args) {
//		System.out.println(myNumber(1232));
		A a = new A();
		A.AB ab = new A.AB();
		A.AB.B b = ab.new B();
	}

	static public int myNumber(int friendNumber) {
		for (int i = 0;; i++) {
			friendNumber += 1;
			if (isUniqueNumbers(friendNumber)) {
				return friendNumber;
			}
		}
	}

	static private boolean isUniqueNumbers(int friendNumber) {
		String fn = String.valueOf(friendNumber);

		for (int i = 0; i < fn.length() - 1; i++) {
			for (int j = i; j < fn.length(); j++) {
				if (fn.charAt(i) == fn.charAt(j))
					return false;
			}
		}
		return true;
	}
}

class A {
	public static class AB {
		int i1 = 0;
		int i = this.i1 + 1;

		public class B {
			{
				System.out.println("B non-static");
			}
		}
	}
}