package db_objs;

/*
    Объект пользователя, который используется для хранения используемой информации (т.е. идентификатора, имени пользователя, пароля и текущего баланса).
 */

import java.math.BigDecimal;
import java.math.RoundingMode;

public class User {
    private final int id;
    private final String username, password;
    private BigDecimal currentBalance;

    public User(int id, String username, String password, BigDecimal currentBalance) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.currentBalance = currentBalance;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal newBalance) {
        // сохранить новое значение до 2-го десятичного знака
        currentBalance = newBalance.setScale(2, RoundingMode.FLOOR);
    }
}