package com.epam.training.booklibrary.datamodels.enums;

/**
 * Class transfer of roles for use in system
 */
public enum Roles {
    GUEST_LIBRARY(0, "GUEST"),          // Guest
    EMPLOYEE_LIBRARY(1, "EMPLOYEE"),    // Employee of library
    READER_LIBRARY(2, "READER");        // Reader of library

    private int index;
    private String name;

    /**
     * The method returns integer value of an index of a role
     * @return int role index
     */
    public int getIndex() {
        return index;
    }

    /**
     * The method returns value of a name of a role
     * @return String role name
     */
    public String getName() {
        return name;
    }

    Roles (int index, String name) {
        this.index = index;
        this.name = name;
    }

    /**
     * The method returns объетк a role on her name
     * @return Roles role
     */
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
     * The method returns a name of a role in text form
     * @param roleIndex parameter contains integer value of a role on which it is necessary to return a role name
     * @return name of a role in text form
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
