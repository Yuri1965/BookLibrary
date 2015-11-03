package com.epam.training.booklibrary.entity;

import com.epam.training.booklibrary.enums.Roles;

import java.io.Serializable;

/**
 * The class Role in system
 */
public class Role implements Serializable {
    private static final long serialVersionUID = 01L;

    // Role identifier
    private int id;
    // Role name from transfer of Roles
    private Roles role;

    /**
     * Constructor of a class by default
     */
    public Role() {
    }

    /**
     * Constructor of a class
     * @param id Role identifier
     * @param role Role name
     */
    public Role(int id, Roles role) {
        this.id = id;
        this.role = role;
    }

    // getters and setters
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
