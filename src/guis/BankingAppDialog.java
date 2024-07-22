package guis;

/*
    Отображает настраиваемое диалоговое окно для нашего BankingAppGui.
 */

import db_objs.MyJDBC;
import db_objs.Transaction;
import db_objs.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

public class BankingAppDialog extends JDialog implements ActionListener {
    private User user;
    private BankingAppGui bankingAppGui;
    private JLabel balanceLabel, enterAmountLabel, enterUserLabel;
    private JTextField enterAmountField, enterUserField;
    private JButton actionButton;
    private JPanel pastTransactionPanel;
    private ArrayList<Transaction> pastTransactions;

    public BankingAppDialog(BankingAppGui bankingAppGui, User user) {
        // установить размер
        setSize(400, 400);

        // добавить фокус в диалог (невозможно взаимодействовать ни с чем, пока диалог не будет закрыт)
        setModal(true);

        setLocationRelativeTo(bankingAppGui);

        // когда пользователь закрывает диалог, он освобождает используемые ресурсы
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // предотвращает изменение размера диалога
        setResizable((false));

        // позволяет нам вручную указать размер и положение каждого компонента
        setLayout(null);

        // понадобится ссылка на графический интерфейс, чтобы обновить текущий баланс
        this.bankingAppGui = bankingAppGui;

        // понадобится доступ к информации о пользователе, чтобы обновлять базу данных или автоматически получать сведения о пользователе.
        this.user = user;
    }

    public void addCurrentBalanceAndAmount() {
        // метка баланса
        balanceLabel = new JLabel("Balance: $" + user.getCurrentBalance());
        balanceLabel.setBounds(0, 10, getWidth() - 20, 20);
        balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(balanceLabel);

        // введите метку суммы
        enterAmountLabel = new JLabel("Enter Amount:");
        enterAmountLabel.setBounds(0, 50, getWidth() - 20, 20);
        enterAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountLabel);

        // введите поле суммы
        enterAmountField = new JTextField();
        enterAmountField.setBounds(15, 80, getWidth() - 50, 40);
        enterAmountField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterAmountField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterAmountField);
    }

    public void addActionButton(String actionButtonType) {
        actionButton = new JButton(actionButtonType);
        actionButton.setBounds(15, 300, getWidth() - 50, 40);
        actionButton.setFont(new Font("Dialog", Font.BOLD, 20));
        actionButton.addActionListener(this);
        add(actionButton);
    }

    public void addUserField() {
        // введите ярлык пользователя
        enterUserLabel = new JLabel("Enter User:");
        enterUserLabel.setBounds(0, 160, getWidth() - 20, 20);
        enterUserLabel.setFont(new Font("Dialog", Font.BOLD, 16));
        enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserLabel);

        // введите пользовательское поле
        enterUserField = new JTextField();
        enterUserField.setBounds(15, 190, getWidth() - 50, 40);
        enterUserField.setFont(new Font("Dialog", Font.BOLD, 20));
        enterUserField.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterUserField);
    }

    public void addPastTransactionComponents() {
        // контейнер, в котором мы будем хранить каждую транзакцию
        pastTransactionPanel = new JPanel();

        // сделать макет 1х1
        pastTransactionPanel.setLayout(new BoxLayout(pastTransactionPanel, BoxLayout.Y_AXIS));

        // добавить возможность прокрутки в контейнер
        JScrollPane scrollPane = new JScrollPane(pastTransactionPanel);

        // отображает вертикальную прокрутку только тогда, когда это необходимо
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(0, 20, getWidth() -15, getHeight() - 15);

        // выполнить вызов базы данных, чтобы получить все прошлые транзакции и сохранить их в списке массивов
        pastTransactions = MyJDBC.getPastTransaction(user);

        // перебрать список и добавить в графический интерфейс
        for (int i = 0; i < pastTransactions.size(); i++) {
            // сохранить текущую транзакцию
            Transaction pastTransaction = pastTransactions.get(i);

            // создать контейнер для истории отдельной транзакции
            JPanel pastTransactionContainer =new JPanel();
            pastTransactionContainer.setLayout(new BorderLayout());

            // создать метку типа транзакции
            JLabel transactionTypeLabel =  new JLabel(pastTransaction.getTransactionType());
            transactionTypeLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // создать метку суммы транзакции
            JLabel transactionAmountLabel = new JLabel(String.valueOf(pastTransaction.getTransactionAmount()));
            transactionAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // создать метку даты транзакции
            JLabel transactionDateLabel = new JLabel(String.valueOf(pastTransaction.getTransactionDate()));
            transactionDateLabel.setFont(new Font("Dialog", Font.BOLD, 20));

            // добавить в контейнер
            pastTransactionContainer.add(transactionTypeLabel, BorderLayout.WEST); // place this on the west side
            pastTransactionContainer.add(transactionAmountLabel, BorderLayout.EAST); // place this on the east side
            pastTransactionContainer.add(transactionDateLabel, BorderLayout.SOUTH); // place this on the south side

            // дать временный фон каждому контейнеру
            pastTransactionContainer.setBackground(Color.WHITE);

            // задать границу блока для каждого контейнера транзакции
            pastTransactionContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // добавить компонент транзакции на панель транзакций
            pastTransactionPanel.add(pastTransactionContainer);
        }

        // добавить в диалог
        add(scrollPane);
    }

    private void handleTransaction(String transactionType, float amountVal) {
        Transaction transaction;

        if (transactionType.equalsIgnoreCase("Deposit")) {
            // тип депозитной операции
            // добавить к текущему балансу
            user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amountVal)));

            // создать транзакцию
            // оставляем дату нулевой, для использования NOW() в sql, который получит текущую дату
            transaction = new Transaction(user.getId(), transactionType, new BigDecimal(amountVal), null);
        } else {
            // тип транзакции вывода
            // вычесть из текущего баланса
            user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amountVal)));

            // показывает отрицательный знак для суммы vol при выводе
            transaction = new Transaction(user.getId(), transactionType, new BigDecimal(amountVal), null);
        }

        // обновление базы данных
        if (MyJDBC.addTransactionToDatabase(transaction) && MyJDBC.updateCurrentBalance(user)) {
            // показать диалог источников
            JOptionPane.showMessageDialog(this, transactionType + " Successfully!");

            // сбросить поля
            resetFieldsAndUpdateCurrentBalance();
        } else {
            // показать диалог сбоя
            JOptionPane.showMessageDialog(this, transactionType + " Failed...");
        }
    }

    private void resetFieldsAndUpdateCurrentBalance() {
        // Очистить поля
        enterAmountField.setText("");

        // появляется только при нажатии кнопки «Перевести»
        if (enterUserField != null) {
            enterUserField.setText("");
        }

        // обновить текущий баланс в диалоговом окне
        balanceLabel.setText("Balance: $" + user.getCurrentBalance());

        // обновить текущий баланс в главном интерфейсе
        bankingAppGui.getCurrentBalanceField().setText("$" + user.getCurrentBalance());
    }

    private void handleTransfer(User user, String transferredUser, float amount) {
        // попытка выполнить перевод
        if (MyJDBC.transfer(user, transferredUser, amount)) {
            // показать диалог успеха
            JOptionPane.showMessageDialog(this, "Transfer Success!");
            resetFieldsAndUpdateCurrentBalance();
        } else {
            // показать диалог сбоя
            JOptionPane.showMessageDialog(this, "Transfer Failed...");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String buttonPressed = e.getActionCommand();

        // получить сумму val
        float amountVal = Float.parseFloat(enterAmountField.getText());

        // прессованный депозит
        if (buttonPressed.equalsIgnoreCase("Deposit")) {
            // обработка депозитной транзакции
            handleTransaction(buttonPressed, amountVal);
        } else {
            // нажал вывод или перевод

            // подтвердите ввод, убедившись, что сумма вывода или перевода меньше текущего баланса
            // если результат равен -1, это означает, что введенная сумма больше, 0 означает, что они равны, а 1 означает, что
            // введенная сумма меньше
            int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amountVal));
            if (result < 0) {
                // отобразить диалоговое окно ошибки
                JOptionPane.showMessageDialog(this, "Error: input value is more than current balance");
                return;
            }

            // проверьте, была ли нажата кнопка вывода или перевода
            if (buttonPressed.equalsIgnoreCase("Withdraw")) {
                handleTransaction(buttonPressed, amountVal);
            } else {
                // перевод
                String transferredUser = enterUserField.getText();

                // обрабатывать перевод
                handleTransfer(user, transferredUser, amountVal);
            }
        }
    }
}