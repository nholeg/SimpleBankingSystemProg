package banking;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.logging.SocketHandler;

public class WorkWithDB {
    static String fileName = "";
    static String url = "";
    static SQLiteDataSource dataSource = new SQLiteDataSource();


    public WorkWithDB(String fileName) {
        this.fileName = fileName;
        url = "jdbc:sqlite:" + fileName;
        dataSource.setUrl(url);
    }

    static void createDB(){
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "number TEXT NOT NULL," +
                        "pin TEXT NOT NULL," +
                        "balance INTEGER DEFAULT 0)");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static int getBalanceDB(String number) {
        String getBalance = "SELECT balance FROM card WHERE number = ?";
        int balance = 0;
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (PreparedStatement statement = con.prepareStatement(getBalance)) {
                // Statement execution
                statement.setString(1, number);
                ResultSet balanceResult = statement.executeQuery();
                balance = balanceResult.getInt("balance");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    static void addIncomeDB(String cardNumber, int sum) {
        String addBalance = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            con.setAutoCommit(false);
            try (PreparedStatement statement = con.prepareStatement(addBalance)) {
                // Statement execution
                statement.setInt(1, sum);
                statement.setString(2, cardNumber);
                statement.executeUpdate();
                con.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                con.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static String doTransferDB(String clientCard, String resivierCard, int sum) {
        String addBalance = "UPDATE card SET balance = balance + ? WHERE number = ?";
        String widrawMoney = "UPDATE card SET balance = balance - ? WHERE number = ?";
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            con.setAutoCommit(false);
            try (PreparedStatement statement = con.prepareStatement(addBalance);
                 PreparedStatement statement1 = con.prepareStatement(widrawMoney)) {
                // Statement execution
                statement.setInt(1, sum);
                statement.setString(2, resivierCard);
                statement.executeUpdate();
                // Statement1 execution
                statement1.setInt(1, sum);
                statement1.setString(2, clientCard);
                statement1.executeUpdate();

                con.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                con.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    return "Success!";
    }

    static void deleteCardFromDB(String cartToDelete) {
        String inputStatement = "DELETE FROM card WHERE number = ?";
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (PreparedStatement statement = con.prepareStatement(inputStatement)) {
                // Statement execution
                statement.setString(1, cartToDelete);
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void insertToDB(String number, String pin) {
        String inputStatement = "INSERT INTO card (number, pin)VALUES (?, ?)";
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (PreparedStatement statement = con.prepareStatement(inputStatement)) {
                // Statement execution
                statement.setString(1, number);
                statement.setString(2, pin);
                statement.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static boolean chekCardInDB(String inNumber, String inPin) {
        boolean flag = false;
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (Statement statement = con.createStatement()) {
                // Statement execution
                try (ResultSet cardResult = statement.executeQuery("SELECT number, pin FROM card")){
                    while (cardResult.next()) {
                        if (inNumber.equalsIgnoreCase(cardResult.getString("number"))
                                && inPin.equalsIgnoreCase(cardResult.getString("pin"))){
                                flag = true;

                        }
                    }

                } catch (SQLException e){
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException c) {
            c.printStackTrace();
        }
        return flag;
    }

    static boolean chekCardNotExistInDB(String inNumber) {
        boolean flag = false;
        String getNumber = "SELECT number FROM card WHERE number = ?";
        try (Connection con = dataSource.getConnection()) {
            // Statement creation
            try (PreparedStatement statement = con.prepareStatement(getNumber)) {
                statement.setString(1, inNumber);
                ResultSet cardResult = statement.executeQuery();
                        if (cardResult.next()){
                            flag = false;
                        } else {
                            flag = true;
                        }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException c) {
            c.printStackTrace();
        }
        return flag;
    }



}
