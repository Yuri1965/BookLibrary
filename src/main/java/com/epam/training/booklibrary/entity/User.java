package com.epam.training.booklibrary.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by URA on 13.09.2015.
 * Класс пользователь системы
 */
public class User implements Serializable {
    private static final long serialVersionUID = 02L;

    // Идентификатор пользователя
    private int id;
    // UUID пользователя
    private String uuid_user;
    // Имя (Login) пользователя
    private String name_user;
    // Пароль пользователя
    private String pass_user;
    // Email пользователя
    private String email_user;
    // Дата и время регистрации пользователя
    private Date reg_Date;
    // Имя пользователя
    private String firstName_user;
    // Фамилия пользователя
    private String lastName_user;

    private boolean isBlocked;
    private String blockedDescription;

    /**
     * Конструктор класса по умолчанию
     */
    public User() {
    }

    /**
     * Конструктор класса
     * @param id Идентификатор пользователя
     * @param uuid_user UUID пользователя
     * @param name_user Имя (Login) пользователя
     * @param pass_user Пароль пользователя
     * @param email_user Email пользователя
     * @param reg_Date Дата и время регистрации пользователя
     * @param firstName_user Имя пользователя
     * @param lastName_user Фамилия пользователя
     * @param isBlocked Признак блокировки пользователя
     * @param blockedDescription Описание причины блокировки пользователя
     */
    public User(int id, String uuid_user, String name_user, String pass_user, String email_user,
                Date reg_Date, String firstName_user, String lastName_user, boolean isBlocked,
                String blockedDescription) {
        this.id = id;
        this.uuid_user = uuid_user;
        this.name_user = name_user;
        this.pass_user = pass_user;
        this.email_user = email_user;
        this.reg_Date = reg_Date;
        this.firstName_user = firstName_user;
        this.lastName_user = lastName_user;
        this.isBlocked = isBlocked;
        this.blockedDescription = blockedDescription;
    }

    // геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid_user() {
        return uuid_user;
    }

    public void setUuid_user(String uuid_user) {
        this.uuid_user = uuid_user;
    }

    public String getName_user() {
        return name_user;
    }

    public void setName_user(String name_user) {
        this.name_user = name_user;
    }

    public String getPass_user() {
        return pass_user;
    }

    public void setPass_user(String pass_user) {
        this.pass_user = pass_user;
    }

    public String getEmail_user() {
        return email_user;
    }

    public void setEmail_user(String email_user) {
        this.email_user = email_user;
    }

    public Date getReg_Date() {
        return reg_Date;
    }

    public void setReg_Date(Date reg_Date) {
        this.reg_Date = reg_Date;
    }

    public String getFirstName_user() {
        return firstName_user;
    }

    public void setFirstName_user(String firstName_user) {
        this.firstName_user = firstName_user;
    }

    public String getLastName_user() {
        return lastName_user;
    }

    public void setLastName_user(String lastName_user) {
        this.lastName_user = lastName_user;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public String getBlockedDescription() {
        return blockedDescription;
    }

    public void setBlockedDescription(String blockedDescription) {
        this.blockedDescription = blockedDescription;
    }

}
