package com.epam.training.booklibrary.entity;

import com.epam.training.booklibrary.enums.Roles;

import java.io.Serializable;

/**
 * Created by URA on 13.09.2015.
 * Класс Роль в системе
 */
public class Role implements Serializable {
    private static final long serialVersionUID = 01L;

    // Идентификатор роли
    private int id;
    // Имя роли из перечисления Roles
    private Roles role;

    /**
     * Конструктор класса по умолчанию
     */
    public Role() {
    }

    /**
     * Конструктор класса
     * @param id Идентификатор роли
     * @param role Имя роли
     */
    public Role(int id, Roles role) {
        this.id = id;
        this.role = role;
    }

    // геттеры и сеттеры
    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRoleName(String roleName) {
        this.role = Roles.valueOf(roleName);
    }


}
