package com.epam.training.booklibrary.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Class User of System
 */
public class User implements Serializable {
    private static final long serialVersionUID = 02L;

    // user ID
    private int id;
    // User's UUID
    private String uuid_user;
    // Name (Login) of the user
    private String name_user;
    // User password
    private String pass_user;
    // E-mail of the user
    private String email_user;
    // Date and time of registration of the user
    private Date reg_Date;
    // User first name
    private String firstName_user;
    // Surname of the user
    private String lastName_user;

    // Sign of blocking of the user
    private boolean isBlocked;
    // Blocking reason
    private String blockedDescription;

    /**
     * Constructor of a class by default
     */
    public User() {
    }

    /**
     * Constructor of a class
     * @param id user ID
     * @param uuid_user User's UUID
     * @param name_user Name (Login) of the user
     * @param pass_user User password
     * @param email_user E-mail of the user
     * @param reg_Date Date and time of registration of the user
     * @param firstName_user User first name
     * @param lastName_user Surname of the user
     * @param isBlocked Sign of blocking of the user
     * @param blockedDescription Blocking reason
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

    // getters and setters
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
