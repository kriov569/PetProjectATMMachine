import java.util.HashMap;
import java.util.Map;

public class AtmOperationImpl implements AtmOperationInterf {
    ATM atm = new ATM();
    Map<Double, String> ministmt = new HashMap<>();
    @Override
    public void viewBalance() {
        System.out.println("Доступный баланс: " + atm.getBalance());
    }

    @Override
    public void withdrawAmount(double withdrawAmount) {
        if (withdrawAmount % 500 == 0) {
            if (withdrawAmount <= atm.getBalance()) {
                ministmt.put(withdrawAmount, " Снятая сумма");
                System.out.println("Заберите деньги: " + withdrawAmount);
                atm.setBalance(atm.getBalance() - withdrawAmount);
                viewBalance();
            } else {
                System.out.println("Недостаточный баланс ");
            }
        } else {
            System.out.println("Введите сумму, кратную 500.0");
        }
    }

    @Override
    public void depositAmount(double depositAmount) {
        ministmt.put(depositAmount, " Сумма депозита");
        System.out.println(depositAmount + " Депонировано успешно ");
        atm.setBalance(atm.getBalance() + depositAmount);
        viewBalance();

    }

    @Override
    public void viewMiniStatement() {
        for (Map.Entry<Double, String> m : ministmt.entrySet()) {
            System.out.println(m.getKey() + "" + m.getValue());
        }

    }
}
