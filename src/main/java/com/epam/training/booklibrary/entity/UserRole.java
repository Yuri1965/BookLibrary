package com.epam.training.booklibrary.entity;

import com.epam.training.booklibrary.enums.Roles;

import java.io.Serializable;

/**
 * Класс Роль пользователя в системе
 * Created by URA on 13.09.2015.
 */
public class UserRole implements Serializable {
    private static final long serialVersionUID = 03L;

    // Идентификатор роли пользователя
    private int id;
    // Идентификатор пользователя
    private int userID;
    // Роль пользователя - объект класса Role
    private Role role;

    /**
     * Конструктор класса по умолчанию
     */
    public UserRole() {
    }

    /**
     * Конструктор класса
     * @param id Идентификатор роли пользователя
     * @param userID Идентификатор пользователя
     * @param roleID Идентификатор роли
     */
    public UserRole(int id, int userID, int roleID) {
        this.id = id;
        this.userID = userID;
        this.role = new Role(roleID, Roles.getRoleByIndex(roleID));
    }

    // геттеры и сеттеры
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setRoleID(int roleID) {
        this.role = new Role(roleID, Roles.getRoleByIndex(roleID));
    }
}

