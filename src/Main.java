import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        AtmOperationInterf op = new AtmOperationImpl();
        int atmnumber = 12345;
        int atmpin=123;
        Scanner in = new Scanner(System.in);
        System.out.println("Добро пожаловать в банкомат ");
        System.out.print("Введите номер банкомата: ");
        int atmNumber = in.nextInt();
        System.out.print("Введите ПИН-код: ");
        int pin = in.nextInt();
        if ((atmnumber == atmNumber) && (atmpin == pin)) {
            while (true) {
                System.out.println("1.Просмотр доступного баланса\n2.Сумма снятия\n3.Сумма вклада\n4.Просмотр истории операций\n5.Выйти");
                System.out.print("Введите номер: ");
                int ch = in.nextInt();
                if (ch == 1) {
                    op.viewBalance();

                } else if (ch == 2) {
                    System.out.print("Введите сумму для вывода: ");
                    double withdrawAmount = in.nextDouble();
                    op.withdrawAmount(withdrawAmount);

                } else if (ch == 3) {
                    System.out.print("Введите сумму для внесения депозита: ");
                    double depositAmount = in.nextDouble(); //5000
                    op.depositAmount(depositAmount);

                } else if (ch == 4) {
                    op.viewMiniStatement();

                } else if (ch == 5) {
                    System.out.println("Заберите свою банковскую карту\nБлагодарим вас за использование банкомата ");
                    System.exit(0);
                } else {
                    System.out.println("Пожалуйста, введите правильный номер");
                }
            }
        } else {
            System.out.println("Неправильный номер или PIN-код банкомата.");
            System.exit(0);
        }
    }
}
