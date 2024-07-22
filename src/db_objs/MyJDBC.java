package db_objs;

/*
    Класс JDBC используется для взаимодействия с базой данных MySQL для выполнения таких действий, как выполнение и обновление базы данных.
 */

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;

public class MyJDBC {
    // конфигурация базы данных
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bankapp";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "password";

    // если допустимо, вернуть объект с информацией о пользователе
    public static User validateLogin(String username, String password) {
        try {
            // установить соединение с базой данных с помощью конфигурации
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // создать sql-запрос
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND password = ?"
            );

            // заменить ?
            // индекс параметра, относящийся к итерации ? так 1 первый ? а 2 это второй ?
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // выполнить запрос и посмотреть на набор результатов
            ResultSet resultSet = preparedStatement.executeQuery();

            // next() возвращает true или false
            // true — возвращаемые данные запроса и набор результатов теперь указывают на первую строку
            // false — запрос не вернул данные, а набор результатов равен нулю
            if (resultSet.next()) {
                // успех
                // получить идентификатор
                int userId = resultSet.getInt("id");

                // получить текущий баланс
                BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");

                // вернуть пользовательский объект
                return new User(userId, username, password, currentBalance);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // недействительный пользователь
        return null;
    }

    // регистрирует нового пользователя в базе данных
    // true – регистрациия удалась
    // false — регистрация не удалась
    public static boolean register(String username, String password) {
        try {
            // сначала нам нужно будет проверить, занято ли уже имя пользователя
            if (checkUser(username)) {
                Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO users(username, password, current_balance) " +
                                "VALUES(?, ?, ?)"
                );

                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);
                preparedStatement.setBigDecimal(3, new BigDecimal(0));

                preparedStatement.executeUpdate();
                return true;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    // проверяем, существует ли имя пользователя в базе данных
    // true — пользователь существует
    // false — пользователь не существует
    private static boolean checkUser(String username) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            // это означает, что запрос не вернул данных, что означает, что имя пользователя доступно.
            if (resultSet.next()) {
                return false;
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    // true — обновление базы данных прошло успешно
    // false - обновление базы данных не прошла
    public static boolean addTransactionToDatabase(Transaction transaction) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement insertTransaction = connection.prepareStatement(
                    "INSERT transactions(user_id, transaction_type, transaction_amount, transaction_date) " +
                            "VALUES(?, ?, ?, NOW())"
            );
            // NOW() вставит текущую дату

            insertTransaction.setInt(1, transaction.getUserId());
            insertTransaction.setString(2, transaction.getTransactionType());
            insertTransaction.setBigDecimal(3, transaction.getTransactionAmount());


            // обновление базы данных
            insertTransaction.executeUpdate();

            return true;


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // true – обновление баланса выполнено успешно
    // false — обновления баланса не вышло
    public static boolean updateCurrentBalance(User user) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement updateBalance = connection.prepareStatement(
                    "UPDATE users SET current_balance = ? WHERE id = ?"
            );

            updateBalance.setBigDecimal(1, user.getCurrentBalance());
            updateBalance.setInt(2, user.getId());

            updateBalance.executeUpdate();
            return true;


        } catch(SQLException e){
            e.printStackTrace();
        }

        return false;
    }

    // true - передача прошла успешно
    // false — передача не удалась
    public static boolean transfer(User user, String transferredUsername, float transferAmount) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement queryUser = connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ?"
            );

            queryUser.setString(1, transferredUsername);
            ResultSet resultSet = queryUser.executeQuery();

            while (resultSet.next()) {
                // выполнить перевод
                User transferredUser = new User(
                        resultSet.getInt("id"),
                        transferredUsername,
                        resultSet.getString("password"),
                        resultSet.getBigDecimal("current_balance")
                );

                // создать транзакцию
                Transaction transferTransaction = new Transaction(
                        user.getId(),
                        "Transfer",
                        new BigDecimal(-transferAmount),
                        null
                );

                // эта транзакция будет принадлежать переданному пользователю
                Transaction receivedTransaction = new Transaction(
                        transferredUser.getId(),
                        "Transfer",
                        new BigDecimal(transferAmount),
                        null
                );

                // обновить пользователя перевода
                transferredUser.setCurrentBalance(transferredUser.getCurrentBalance().add(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(transferredUser);

                // обновить текущий баланс пользователя
                user.setCurrentBalance(user.getCurrentBalance().subtract(BigDecimal.valueOf(transferAmount)));
                updateCurrentBalance(user);

                // добавить эти транзакции в базу данных
                addTransactionToDatabase(transferTransaction);
                addTransactionToDatabase(receivedTransaction);

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // получить все транзакции (используется для прошлой транзакции)
    public static ArrayList<Transaction> getPastTransaction(User user) {
        ArrayList<Transaction> pastTransactions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            PreparedStatement selectAllTransaction = connection.prepareStatement(
                    "SELECT * FROM transactions WHERE user_id = ?"
            );
            selectAllTransaction.setInt(1, user.getId());

            ResultSet resultSet = selectAllTransaction.executeQuery();

            // перебирать результаты (если есть)
            while (resultSet.next()) {
                // создать объект транзакции
                Transaction transaction = new Transaction(
                        user.getId(),
                        resultSet.getString("transaction_type"),
                        resultSet.getBigDecimal("transaction_amount"),
                        resultSet.getDate("transaction_date")
                );

                // сохранить в список массивов
                pastTransactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pastTransactions;
    }
}