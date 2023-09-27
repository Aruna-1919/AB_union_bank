import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class bankjdbc2 {
    private Connection connection;

    public bankjdbc2() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/labs", "root", "arunatarak");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    public void deposit(String accno, int amt, int total) {
        int temp = total + amt;
        String sql = "UPDATE union_bank SET amount=? WHERE acc_no=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, temp);
            preparedStatement.setString(2, accno);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Deposit successful. New balance: " + temp);
            } else {
                System.out.println("Deposit failed.");
            }
        } catch (Exception e) {
            System.out.println("Error: account iss not there");
        }
    }

    public void withdraw(String accno, int amt, int total) {
        int temp = total - amt;
        String sql = "UPDATE union_bank SET amount=? WHERE acc_no=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, temp);
            preparedStatement.setString(2, accno);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Withdrawal successful. New balance: " + temp);
            } else {
                System.out.println("Withdrawal failed.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public int getBalance(String accno) {
        int total = 0;
        String sql = "SELECT amount FROM union_bank WHERE acc_no=?";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, accno);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getInt("amount");
            }
        } catch (Exception e) {
            System.out.println("Error:at get balance method");
        }

        return total;
    }
    public void checkBalance(String accno) {
        String sql = "SELECT * FROM union_bank WHERE acc_no=?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, accno);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("Account holder name: " + resultSet.getString("name"));
                System.out.println("Account holder age: " + resultSet.getString("age"));
                System.out.println("Account number: " + resultSet.getString("acc_no"));
                System.out.println("Balance: " + resultSet.getInt("amount"));
                System.out.println("---------------------------------------------");
            } else {
                System.out.println("Account not found.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        bankjdbc2 bank = new bankjdbc2();

        while (true) {
            System.out.println("Enter choice: 1-deposit 2-withdraw 3-balance check 4-exit");
            int n = sc.nextInt();
            switch (n) {
                case 1:
                    System.out.println("Enter acc no.:");
                    String acc = sc.next();
                    System.out.println("Enter amount to deposit:");
                    int dep = sc.nextInt();
                    // Retrieve total balance from the database first
                    int totalBalance = bank.getBalance(acc);
                    bank.deposit(acc, dep, totalBalance);
                    break;
                case 2:
                    System.out.println("Enter acc no.:");
                    acc = sc.next();
                    System.out.println("Enter amount to withdraw:");
                    dep = sc.nextInt();
                    // Retrieve total balance from the database first
                    totalBalance = bank.getBalance(acc);
                    bank.withdraw(acc, dep, totalBalance);
                    break;
                case 3:
                    System.out.println("Enter acc no.:");
                    acc = sc.next();
                    bank.checkBalance(acc);
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    

    
}
