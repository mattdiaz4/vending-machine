package com.techelevator;

import com.techelevator.view.Menu;
import org.w3c.dom.ls.LSOutput;

import javax.crypto.spec.PSource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT};

	private static final String [] EDIT_DATA_OPTIONS = {"Feed Money", "Select Product", "Finish Transaction", "Back"};

	private Menu menu;
	private List<Product> products = new ArrayList<>();

	private BigDecimal balance = new BigDecimal(0);
	private LogWriter logWriter = new LogWriter("Log.txt");
	LocalDateTime currentDateTime = LocalDateTime.now();
	DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {

		loadData();

		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				displayLoadData();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				displayPurchaseMenuOptions();
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				System.exit(1);
			}
		}
	}

	public void loadData() {
		File file = new File("vendingmachine.csv");

		try {
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String [] lineArr = line.split("\\|");

				String slotLocation = lineArr[0];
				String name = lineArr[1];
				BigDecimal price = new BigDecimal(lineArr[2]);
				String category = lineArr[3];

				Product product = new Product(slotLocation, name, price, category, 5);
				products.add(product);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Problem finding file.");
		}
	}
	public void displayLoadData() {
		 for (Product product : this.products) {
			 System.out.println(product.getSlotLocation() + " " + product.getName() + " " +
					 product.getPrice() + " " + product.getCategory() + " Quantity: " + product.getQuantity());
		 }
	}
	public void displayPurchaseMenuOptions() {
		boolean stay = true;

		while (stay) {
			String choice = (String) menu.getChoiceFromOptions(EDIT_DATA_OPTIONS);
			if (choice.equals("Feed Money")) {
				feedMoney();
			} else if (choice.equals("Select Product")) {
				selectProduct();
			} else if (choice.equals("Finish Transaction")) {
				finishTransaction();
				stay = false;
			} else if (choice.equals("Back")) {
				stay = false;
			}
			System.out.println("Current balance: $" + balance);
		}
	}
	public void feedMoney () {
		BigDecimal bigDecimalOne = new BigDecimal("1.00");
		BigDecimal bigDecimalTwo = new BigDecimal("2.00");
		BigDecimal bigDecimalFive = new BigDecimal("5.00");
		BigDecimal bigDecimalTen = new BigDecimal("10.00");

		Scanner feedMoneyScanner = new Scanner(System.in);
		System.out.println("Please enter dollars in 1, 2, 5, or 10");

			int dollarAmount = Integer.parseInt(feedMoneyScanner.nextLine());

			if (dollarAmount == 1) {
				balance = balance.add(bigDecimalOne);
				logWriter.writeToFile(currentDateTime.format(targetFormat) + "FEED MONEY: \\$1.00" + balance);
			} else if (dollarAmount == 2) {
				balance = balance.add(bigDecimalTwo);
				logWriter.writeToFile(currentDateTime.format(targetFormat) + "FEED MONEY: \\$2.00" + balance);

			} else if (dollarAmount == 5) {
				balance = balance.add(bigDecimalFive);
				logWriter.writeToFile(currentDateTime.format(targetFormat) + "FEED MONEY: \\$5.00" + balance);

			} else if (dollarAmount == 10) {
				balance = balance.add(bigDecimalTen);
				logWriter.writeToFile(currentDateTime.format(targetFormat) + "FEED MONEY: \\$10.00" + balance);

			} else {
				System.out.println("Not a valid dollar amount");
	}

	}

	public void selectProduct() {
		displayLoadData();
		System.out.println("");
		System.out.println("Please enter an item code to select an item.");
		Scanner inputSelection = new Scanner(System.in);
		String slotLocationInput = inputSelection.nextLine();
		boolean found = false;
		boolean enoughBalance = false;
		boolean enoughQuantity = false;

		for (int i = 0; i < this.products.size(); i++) {
			Product product = products.get(i);
			String slotLocation = product.getSlotLocation();
		BigDecimal productPriceDecimal = new BigDecimal(String.valueOf(product.getPrice()));


			if (slotLocation.equals(slotLocationInput)) {
				found = true;
				if (balance.compareTo(productPriceDecimal) >= 0) {
					enoughBalance = true;
					if (product.getQuantity() > 0) {
						enoughQuantity = true;
						product.setQuantity(product.getQuantity() - 1);
						balance = balance.subtract(product.getPrice());
						if (product.getCategory().equals("Drink")) {
							System.out.println(product.getName() + " " + product.getPrice() + " " + balance + " Glug Glug, Yum!");
						} else if (product.getCategory().equals("Candy")) {
							System.out.println(product.getName() + " " + product.getPrice() + " " + balance + " Munch Munch, Yum!");
						} else if (product.getCategory().equals("Chips")) {
							System.out.println(product.getName() + " " + product.getPrice() + " " + balance + " Crunch Crunch, Yum!");
						} else if (product.getCategory().equals("Gum")) {
							System.out.println(product.getName() + " " + product.getPrice() + " " + balance + " Chew Chew, Yum!");
						}
					} else {
						System.out.println("Item is sold out.");
					}
				} else {
					System.out.println("Balance too low.");
				}
			}

		}if(found) {
			if (enoughBalance) {
				if (enoughQuantity) {
					System.out.println("Dispensing Item");
				}
			}
		} else {
			System.out.println("Invalid code.");
		}
	}
	public void finishTransaction() {
		int quarterAmount;
		int dimeAmount;
		int nickelAmount;

		if (balance.compareTo(new BigDecimal(0))> 0) {


			BigDecimal balanceInCents = new BigDecimal(String.valueOf(balance.multiply(new BigDecimal("100"))));

			int balanceInCentsInt = balanceInCents.intValue();
			quarterAmount = balanceInCentsInt / 25;
			int numberOne = balanceInCentsInt % 25;
			dimeAmount = numberOne / 10;
			int numberTwo = numberOne % 10;
			nickelAmount = numberTwo / 5;
			balance = balance.subtract(balance);
			System.out.println("Change is " + quarterAmount + " in quarters " + dimeAmount + " in dimes " + nickelAmount
			+ " in nickels.");

		}else {
			System.out.println("No balance available.");
		}



	}




	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
