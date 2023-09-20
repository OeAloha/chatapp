package oe.aloha;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Hello, world!");
		System.out.println("Yongo!");
		Thread.sleep(1000);
		System.out.print("\\033[1F\\33[K");
		System.out.flush();
		System.out.println("Goodbye, everybody! I've got to go!");
	}
}
