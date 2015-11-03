package com.epam.training.booklibrary.entity;

import com.epam.training.booklibrary.enums.Roles;

import java.io.Serializable;

/**
 * The class Role of the User in system
 */
public class UserRole implements Serializable {
    private static final long serialVersionUID = 03L;

    // Identifier of a role of the user
    private int id;
    // user ID
    private int userID;
    // Role of the user - object of the class Role
    private Role role;

    /**
     * Constructor of a class by default
     */
    public UserRole() {
    }

    /**
     * Constructor of a class
     * @param id Identifier of a role of the user
     * @param userID user ID
     * @param roleID Role identifier
     */
    public UserRole(int id, int userID, int roleID) {
        this.id = id;
        this.userID = userID;
        this.role = new Role(roleID, Roles.getRoleByIndex(roleID));
    }

    // getters and setters
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

