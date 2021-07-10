package banking;

import java.util.Random;


public class Card {
    private String MII = "4";
    private String BIN = MII + "00000";
    private String accountId;
    private int checkSum;
    private String cardNumber;
    private double balance = 0;
    private String pin;

    public Card() {
        accountId = cardNumber();
        checkSum = checkSum(accountId, BIN);
        pin = pinNumber();
        cardNumber = BIN + accountId + checkSum;
    }

    private String pinNumber() {
        Random random = new Random();
        String pin = "";
        while (pin.length() < 4) {
            pin = pin + random.nextInt(9);
        }

        return pin;
    }
    private int checkSum (String str, String BIN) {
        int sum = 0;
        String fifteenNumbersString = "" + BIN + str;
        String[] fifteenNumbersStringArray = fifteenNumbersString.split("");
        int[] fifteenNumbers = new int[15];
        for (int i = 0; i < fifteenNumbersStringArray.length; i++) {
            fifteenNumbers[i] = Integer.valueOf(fifteenNumbersStringArray[i]);
        }
        for (int i = 0; i < fifteenNumbers.length; i++) {
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

        return ((10 - sum % 10) % 10) ;
    }

    private String cardNumber(){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int count = 1;
        int digit = 0;
        while (count < 10) {
            digit = random.nextInt(9);
            stringBuilder.append(digit);
            count++;
        }
        return stringBuilder.toString();
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String PIN) {
        this.pin = PIN;
    }
}

