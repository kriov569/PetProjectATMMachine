package guis;

import db_objs.MyJDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegisterGui extends BaseFrame {
    public RegisterGui() {
        super("Banking App Register");
    }
    @Override
    protected void addGuiComponents() {
        // создать ярлык банковского приложения
        JLabel bankingAppLabel = new JLabel("Banking Application");

        // установления местоположения и размер компонента графического интерфейса
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
        passwordLabel.setBounds(20, 220, getWidth() - 50, 24);
        passwordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(passwordLabel);

        // создать поле пароля
        JPasswordField passwordField = new JPasswordField();
        passwordField.setBounds(20, 260, getWidth() - 50, 40);
        passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(passwordField);

        // введите еще раз метку пароля
        JLabel rePasswordLabel = new JLabel("Re-type Password:");
        rePasswordLabel.setBounds(20, 320, getWidth() - 50, 40);
        rePasswordLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        add(rePasswordLabel);

        // создать поле для повторного ввода пароля
        JPasswordField rePasswordField = new JPasswordField();
        rePasswordField.setBounds(20, 360, getWidth() - 50, 40);
        rePasswordField.setFont(new Font("Dialog", Font.PLAIN, 28));
        add(rePasswordField);

        // создать кнопку регистрации
        JButton registerButton = new JButton("Register");
        registerButton.setBounds(20, 460, getWidth() - 50, 40);
        registerButton.setFont(new Font("Dialog", Font.BOLD, 20));
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // получить имя пользователя
                String username = usernameField.getText();

                // получить пароль
                String password = String.valueOf(passwordField.getPassword());

                // получить повторный пароль
                String rePassword = String.valueOf(rePasswordField.getPassword());

                // нужно проверить ввод пользователя
                if (validateUserInput(username, password, rePassword)) {
                    // попытка зарегистрировать пользователя в базе данных
                    if (MyJDBC.register(username, password)) {
                        // регистрация удалась
                        // убрать графический интерфейс
                        RegisterGui.this.dispose();

                        // запустить графический интерфейс входа в систему
                        LoginGui loginGui = new LoginGui();
                        loginGui.setVisible(true);

                        // создать диалоговое окно успеха
                        JOptionPane.showMessageDialog(loginGui, "Registered Account Successfully!");
                    }else {
                        // регистрация не удалась
                        JOptionPane.showMessageDialog(RegisterGui.this, "Error: Username already taken");
                    }
                } else {
                    // неверный ввод пользователя
                    JOptionPane.showMessageDialog(RegisterGui.this,
                            "Error: Username must be a least 6 characters\n" +
                                    "and/or Password must match");
                }
            }
        });
        add(registerButton);

        // создать ярлык для входа
        JLabel loginLabel = new JLabel("<html><a href=\"#\">Have an account? Sign-in here</a></html>");
        loginLabel.setBounds(0, 510, getWidth() - 10, 30);
        loginLabel.setFont(new Font("Dialog", Font.PLAIN, 20));
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // убрать графический интерфейс
                RegisterGui.this.dispose();

                // запустить графический интерфейс входа в систему
                new LoginGui().setVisible(true);
            }
        });
        add(loginLabel);
    }

    private boolean validateUserInput(String username, String password, String rePassword) {
        // все поля должны иметь значение
        if (username.length() == 0 || password.length() == 0 || rePassword.length() == 0) return false;

        // имя пользователя должно быть длиной не менее 6 символов
        if (username.length() < 6) return false;

        // пароль и повторный пароль должны быть одинаковыми
        if (!password.equals(rePassword)) return false;

        // проходит проверку
        return true;
    }
}