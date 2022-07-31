package ru.nhp.user.enums;

public enum EmailType {
    USER_REGISTRATION("Подтвердите ваш email, TRUE"),
    USER_ORDER_CREATED("Заказ успешно сформирован"),
    MANAGER_ORDER_CREATED("Поступил новый заказ");

    private final String name;

    EmailType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
