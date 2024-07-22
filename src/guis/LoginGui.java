package guis;

/*
    Этот графический интерфейс позволит пользователю войти в систему или запустить графический интерфейс регистрации.
    Это происходит из BaseFrame, поэтому нам нужно будет определить наш собственный addGuiComponent().
 */

import db_objs.MyJDBC;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginGui extends BaseFrame {
    public LoginGui() {
        super("Banking App Login");
    }


    @Override
    protected void addGuiComponents() {
        // создать ярлык банковского приложения
        JLabel bankingAppLabel = new JLabel("Banking Application");

        // установить местоположение и размер компонента графического интерфейса
        bankingAppLabel.setBounds(0, 20, super.getWidth(), 40);

        // изменить стиль шрифта
        bankingAppLabel.setFont(new Font("Dialog", Font.BOLD, 32));

        // центрировать текст в Jlabel
        bankingAppLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // добавить в графический интерфейс
        add(bankingAppLabel);

        // ярлык с именем пользователя
        JLabel usernameLabel = new JLabel("Username:");

        // getWidth() возвращает нам ширину нашего кадра, которая составляет около 420.
        usernameLabel.setBounds(20, 120, getWidth() - 30, 24);

        usernameLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(usernameLabel);

        // создать поле имени пользователя
        JTextField usernameField = new JTextField();
        usernameField.setBounds(20, 160, getWidth() - 50, 40);
        usernameField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(usernameField);

        // создать метку пароля
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 280, getWidth() - 50, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        // создать поле пароля
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 320, getWidth() - 50, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        // создать кнопку входа
        JButton loginButton = new JButton("Login");
        loginButton.setBounds(20, 460, getWidth() - 50, 40);
        loginButton.setFont(new Font("Dialog", Font.BOLD, 20));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // получить имя пользователя
                String username = usernameField.getText();

                // получить пароль
                String password = String.valueOf(passwordField.getPassword());

                // подтвердить вход
                User user = MyJDBC.validateLogin(username, password);

                // если пользователь имеет значение null, это означает, что он недействителен, в противном случае это действительная учетная запись.
                if (user != null) {
                    // означает действительный логин

                    // избавиться от этого графического интерфейса
                    LoginGui.this.dispose();

                    // запустить графический интерфейс приложения банка
                    BankingAppGui bankingAppGui = new BankingAppGui(user);
                    bankingAppGui.setVisible(true);

                    // показать диалог успеха
                    JOptionPane.showMessageDialog(bankingAppGui, "Login Successfully!");
                }else {
                    // Неверный логин
                    JOptionPane.showMessageDialog(LoginGui.this, "Login failed...");
                }
            }
        });
        add(loginButton);

        // создать регистрационную метку
        JLabel registerLabel = new JLabel("<html><a href=\"#\">Don't have an account? Register Here</a></html>");
        registerLabel.setBounds(0, 510, getWidth() - 10, 30);
        registerLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        registerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // добавляет прослушиватель событий, поэтому при нажатии мыши он запускает графический интерфейс регистрации
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // убрать графический интерфейс
                LoginGui.this.dispose();

                // запустить графический интерфейс регистрации
                new RegisterGui().setVisible(true);
            }
        });

        add(registerLabel);
    }
}