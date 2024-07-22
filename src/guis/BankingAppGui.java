package guis;

import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
    Выполняет банковские функции, такие как внесение депозита, снятие средств, просмотр прошлых транзакций и перевод.
    Это выходит из BaseFrame, что означает, что нам нужно будет определить наш собственный addGuiComponent.
 */
public class BankingAppGui extends BaseFrame implements ActionListener {
    private JTextField currentBalanceField;
    public JTextField getCurrentBalanceField() {return currentBalanceField;}

    public BankingAppGui(User user) {
        super("Banking App", user);
    }
    @Override
    protected void addGuiComponents() {
        // создать приветственное сообщение
        String welcomeMessage = "<html>" +
                "<body style = 'text-align:center'>" +
                "<b>Hello " + user.getUsername() + "</b><br>" +
                "What would you like to do today?</body></html>";
        JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
        welcomeMessageLabel.setBounds(0, 20, getWidth() - 10, 40);
        welcomeMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
        welcomeMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeMessageLabel);

        // создать метку текущего баланса
        JLabel currentBalanceLabel = new JLabel("Current Balance");
        currentBalanceLabel.setBounds(0, 80, getWidth() - 10, 30);
        currentBalanceLabel.setFont(new Font("Dialog", Font.BOLD, 22));
        currentBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(currentBalanceLabel);

        // создать поле текущего баланса
        currentBalanceField = new JTextField("$" + user.getCurrentBalance());
        currentBalanceField.setBounds(15, 120, getWidth() - 50, 40);
        currentBalanceField.setFont(new Font("Dialog", Font.BOLD, 20));
        currentBalanceField.setHorizontalAlignment(SwingConstants.RIGHT);
        currentBalanceField.setEditable(false);
        add(currentBalanceField);

        // кнопка депозита
        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(15, 180, getWidth() - 50, 50);
        depositButton.setFont(new Font("Dialog", Font.BOLD, 22));
        depositButton.addActionListener(this);
        add(depositButton);

        // кнопка вывода средств
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(15, 250, getWidth() - 50, 50);
        withdrawButton.setFont(new Font("Dialog", Font.BOLD, 22));
        withdrawButton.addActionListener(this);
        add(withdrawButton);

        // кнопка прошлых транзакции
        JButton pastTransactionButton = new JButton("Past Transaction");
        pastTransactionButton.setBounds(15, 320, getWidth() - 50, 50);
        pastTransactionButton.setFont(new Font("Dialog", Font.BOLD, 22));
        pastTransactionButton.addActionListener(this);
        add(pastTransactionButton);

        // кнопка перевода
        JButton transferButton = new JButton("Transfer");
        transferButton.setBounds(15, 390, getWidth() - 50, 50);
        transferButton.setFont(new Font("Dialog", Font.BOLD, 22));
        transferButton.addActionListener(this);
        add(transferButton);

        // кнопка выхода
        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(15, 500, getWidth() - 50, 50);
        logoutButton.setFont(new Font("Dialog", Font.BOLD, 22));
        logoutButton.addActionListener(this);
        add(logoutButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        // пользователь нажал выход
        if (buttonPressed.equalsIgnoreCase("Logout")) {
            // вернуть пользователю графический интерфейс входа в систему
            new LoginGui().setVisible(true);

            // убрать графический интерфейс
            this.dispose();

            return;
        }

        // другие функции
        BankingAppDialog bankingAppDialog = new BankingAppDialog(this, user);

        // установления заголовка диалога для действия
        bankingAppDialog.setTitle(buttonPressed);

        // если нажата кнопка – это депозит, вывод средств или перевод
        if (buttonPressed.equalsIgnoreCase("Deposit") || buttonPressed.equalsIgnoreCase("Withdraw")
                || buttonPressed.equalsIgnoreCase("Transfer")) {
            // добавить в диалоговое окно компоненты текущего баланса и суммы графического интерфейса
            bankingAppDialog.addCurrentBalanceAndAmount();

            // добавить кнопку действия
            bankingAppDialog.addActionButton(buttonPressed);

            // для передаточного действия потребуется больше компонентов
            if (buttonPressed.equalsIgnoreCase("Transfer")) {
                bankingAppDialog.addUserField();
            }

        } else if (buttonPressed.equalsIgnoreCase("Past Transaction")){
            bankingAppDialog.addPastTransactionComponents();
        }

        // сделать диалог приложения видимым
        bankingAppDialog.setVisible(true);
    }
}
