package guis;

import db_objs.User;

import javax.swing.*;

/*
    Создание абстрактного класса помогает нам настроить план, которому будут следовать наши графические интерфейсы, например.
    в каждом из графических интерфейсов они будут одинакового размера и должны будут вызывать свои собственные addGuiComponents().
    который будет уникальным для каждого подкласса.
 */

public abstract class BaseFrame extends JFrame {
    // хранить информацию о пользователе
    protected User user;


    public BaseFrame(String title) {
        initialize(title);
    }
    public BaseFrame(String title, User user) {
        // инициализировать пользователя
        this.user = user;

        initialize(title);
    }

    private void initialize(String title) {
        // создать экземпляр свойств jframe и добавить заголовок на панель.
        setTitle(title);

        // установить размер (в пикселях)
        setSize(420, 600);

        // завершить программу, когда графический интерфейс закрыт
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // установите для макета значение null, чтобы иметь абсолютный макет, который позволяет нам вручную указывать размер и положение каждого компонента графического интерфейса.
        setLayout(null);

        // запретить изменение размера графического интерфейса
        setResizable(false);

        // запустить графический интерфейс в центре экрана
        setLocationRelativeTo(null);

        // класс в подклассе 'addGuiComponent()
        addGuiComponents();
    }

    // этот метод должен быть определен подклассами, когда этот класс наследуется
    protected abstract void addGuiComponents();
}