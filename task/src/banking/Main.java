package banking;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    static Map<String, Card> cards = new HashMap();
    static Scanner scanner = new Scanner(System.in);

    static void instructions() {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
    }
    static void instruction1() {
        System.out.println("1. Balance");
        System.out.println("2. Add income");
        System.out.println("3. Do transfer");
        System.out.println("4. Close account");
        System.out.println("5. Log out");
        System.out.println("0. Exit");
    }

    static void error1() {
        System.out.println("Wrong card number or PIN!");
    }
    static void errorWrongRecevierCard() {
        System.out.println("Probably you made a mistake in the card number. Please try again!");
    }
    static void errorCardNotExist() {
        System.out.println("Such a card does not exist.");
    }
    static void notEnoughMoney() {
        System.out.println("Not enough money!");
    }

    static void start(String filename) {
        WorkWithDB db =new WorkWithDB(filename);
        db.createDB();
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                instructions();
                switch (scanner.nextInt()) {
                    case 1:
                        while (true) {
                            Card card = new Card();
                            if (db.chekCardInDB(card.getCardNumber(),card.getPin())) {
                                continue;
                            } else {
                                db.insertToDB(card.getCardNumber(),card.getPin());
                                System.out.println("Your card has been created");
                                System.out.println("Your card number:");
                                System.out.println(card.getCardNumber());
                                System.out.println("Your card PIN:");
                                System.out.println(card.getPin());
                                break;
                            }
                        }
                        break;
                    case 2:
                        System.out.println("Enter your card number:");
                        String cardNumber = scanner.next();
                        System.out.println("Enter your PIN:");
                        String pin = scanner.next();
                        if (db.chekCardInDB(cardNumber,pin)) {
                            logged(cardNumber, db);
                        } else {
                           error1();
                        }
                        break;
                    case 0:
                        System.out.println("Bye!");
                        System.exit(0);

                }
            }
        }
    }
    private static void logged(String cardNumber, WorkWithDB db) {
        System.out.println("You have successfully logged in!");
        try (Scanner scanner = new Scanner(System.in)) {
            instruction1();
            int choice = -1;
            while (choice != 5) {
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.println("Balance: " +  db.getBalanceDB(cardNumber));
                        break;
                    case 2:
                        System.out.println("Enter income:");
                        int add = scanner.nextInt();
                        db.addIncomeDB(cardNumber, add);
                        System.out.println("Income was added!");
                        break;
                    case 3:
                        System.out.println("Transfer");
                        System.out.println("Enter card number:");
                        String cardResivier = scanner.next();
                        boolean luhn = !validateCard(cardResivier);
                        boolean notExist = db.chekCardNotExistInDB(cardResivier);
                        if (luhn) {
                            errorWrongRecevierCard();
                        } else if (notExist) {
                            errorCardNotExist();
                        } else {
                            int transferMoney = scanner.nextInt();
                            if (db.getBalanceDB(cardNumber) <= transferMoney) {
                                notEnoughMoney();
                            } else {
                                System.out.println("Balance on my card: " + db.getBalanceDB(cardNumber));
                                System.out.println("Balance on recivier card: " + db.getBalanceDB(cardResivier));
                                System.out.println(db.doTransferDB(cardNumber, cardResivier, transferMoney));
                                System.out.println("Balance on my card: " + db.getBalanceDB(cardNumber));
                                System.out.println("Balance on recivier card: " + db.getBalanceDB(cardResivier));
                            }
                        }
                        break;
                    case 4:
                        db.deleteCardFromDB(cardNumber);
                        break;
                    case 5:
                        System.out.println("You have successfully logged out!");
                        break;
                    case 0:
                        System.out.println("Bye!");
                        System.exit(0);
                }
            }
        }
    }

    private static boolean validateCard(String cardNumber) {
        //Luhn algorithm
        int sum = 0;
        String[] fifteenNumbersStringArray = cardNumber.split("");
        int[] fifteenNumbers = new int[16];
        int checkSum = Integer.valueOf(fifteenNumbersStringArray[15]);
        for (int i = 0; i < fifteenNumbersStringArray.length - 1; i++) {
            fifteenNumbers[i] = Integer.valueOf(fifteenNumbersStringArray[i]);
        }
        for (int i = 0; i < fifteenNumbers.length - 1; i++) {
            int number = 0;
            // thus Array starts with 0 we should invert conditions.
            if (i % 2 != 0) {
                number = fifteenNumbers[i];
            } else {
                number = fifteenNumbers[i] * 2;
                if (number > 9) {
                    number -= 9;
                }
            }
            sum += number;

        }
        return checkSum == ((10 - sum % 10) % 10);
    }

    public static void main(String[] args) {
        String fileName = "";
        for (int i = 0; i < args.length -1; i++) {
            if (args[i].equalsIgnoreCase("-fileName")) {
                fileName = args[i + 1];
            }
        }
        start(fileName);
    }
}