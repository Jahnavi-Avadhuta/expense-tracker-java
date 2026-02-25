package com.expensetracker;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ExpenseTracker {

	private static List<Expense> expenses = new ArrayList<>();
	private static final String FILE_NAME = "expenses.txt";

	public static void main(String[] args) {
		System.out.println("===== Expense Tracker Application =====");
		loadFromFile();

		Scanner sc = new Scanner(System.in);

		while (true) {
			System.out.println("\n1. Add Expense");
			System.out.println("2. View All Expenses");
			System.out.println("3. View Total Expense");
			System.out.println("4. View Category-wise Total");
			System.out.println("5. Save & Exit");
			System.out.print("Choose option: ");

			int choice;
			try {
				choice = Integer.parseInt(sc.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid input! Enter a number between 1-5.");
				continue;
			}

			switch (choice) {
			case 1:
				addExpense(sc);
				break;
			case 2:
				viewAll();
				break;
			case 3:
				viewTotal();
				break;
			case 4:
				categoryWiseTotal();
				break;
			case 5:
				saveToFile();
				System.out.println("Data saved. Exiting...");
				return;
			default:
				System.out.println("Invalid choice!");
			}
		}
	}

	private static void addExpense(Scanner sc) {
		try {
			System.out.print("Enter category: ");
			String category;
			while (true) {
				category = sc.nextLine().trim();

				if (category.isEmpty()) {
					System.out.print("Category cannot be empty. Enter again: ");
				} else {
					break;
				}
			}
			category = category.toLowerCase();

			System.out.print("Enter amount: ");
			double amount;
			while (true) {
				try {
					amount = Double.parseDouble(sc.nextLine());
					if (amount <= 0) {
						System.out.print("Amount must be positive. Enter again: ");
						continue;
					}
					break;
				} catch (NumberFormatException e) {
					System.out.print("Invalid amount. Enter again: ");
				}
			}

			LocalDate date = LocalDate.now();

			Expense expense = new Expense(category, amount, date);
			expenses.add(expense);

			System.out.println("Expense added successfully!");

		} catch (Exception e) {
			System.out.println("Invalid input!");
		}
	}

	private static void viewAll() {
		if (expenses.isEmpty()) {
			System.out.println("No expenses recorded.");
			return;
		}

		System.out.println("\n----- All Expenses -----");
		System.out.printf("%-15s %-10s %-15s%n", "Category", "Amount", "Date");
		System.out.println("------------------------------------------");

		for (Expense e : expenses) {
			System.out.printf("%-15s %-10.2f %-15s%n", e.getCategory(), e.getAmount(), e.getDate());
		}
	}

	private static void viewTotal() {
		double total = 0;
		for (Expense e : expenses) {
			total += e.getAmount();
		}
		System.out.printf("Total Expense: %.2f%n", total);
	}

	private static void categoryWiseTotal() {
		Map<String, Double> map = new HashMap<>();

		for (Expense e : expenses) {
			map.put(e.getCategory(), map.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
		}

		System.out.println("\n----- Category Summary -----");
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			System.out.printf("%-15s : %.2f%n", entry.getKey(), entry.getValue());
		}
	}

	private static void saveToFile() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
			for (Expense e : expenses) {
				bw.write(e.toString());
				bw.newLine();
			}
		} catch (IOException e) {
			System.out.println("Error saving file.");
		}
	}

	private static void loadFromFile() {
		File file = new File(FILE_NAME);
		if (!file.exists())
			return;

		try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
			String line;
			while ((line = br.readLine()) != null) {
				expenses.add(Expense.fromString(line));
			}
		} catch (IOException e) {
			System.out.println("Error loading file.");
		}
	}
}
