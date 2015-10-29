package com.epam.training.booklibrary.enums;

/**
 * Created by URA on 15.09.2015.
 * Класс перечисление ролей для использования в системе
 */
public enum Roles {
    GUEST_LIBRARY(0, "GUEST"),          // Гость
    EMPLOYEE_LIBRARY(1, "EMPLOYEE"),    // Работник библиотеки
    READER_LIBRARY(2, "READER");        // Читатель библиотеки

    private int index;
    /**
     * Метод возвращает целочисленное значение индекса роли
     * @return int индекс роли
     */
    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    private String name;

    Roles (int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static Roles getRoleByName(String name) {
        Roles result = null;
        for (Roles role : Roles.values()) {
            if (role.getName().equals(name)) {
                result = role;
            }
        }
        return result;
    }

    /**
     * Метод возвращает имя роли в текстовом виде
     * @param roleIndex параметр содержит целочисленное значение роли, по которому надо вернуть имя роли
     * @return имя роли в текстовом виде
     */
    public static Roles getRoleByIndex(int roleIndex) {
        Roles result = null;
        for (Roles role : Roles.values()) {
            if (role.getIndex() == roleIndex) {
                result = role;
            }
        }
        return result;
    }

}
