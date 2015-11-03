package com.epam.training.booklibrary.datamodels.entity;

import com.epam.training.booklibrary.controllers.utils.GeneralUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

/**
 * Class which contains expansion of the class User for work in the Work with Users mode
 */
public class UserExt extends User implements Serializable {
    private static final long serialVersionUID = 14L;

    // role of the user
    private UserRole userRole;
    // role of the user in text form
    private String roleName;

    /**
     * Constructor of a class by default
     */
    public UserExt() {
    }

    /**
     * Constructor of a class
     * @param id
     * @param uuid_user
     * @param name_user
     * @param pass_user
     * @param email_user
     * @param reg_Date
     * @param firstName_user
     * @param lastName_user
     * @param isBlocked
     * @param blockedDescription
     * @param userRole
     */
    public UserExt(int id, String uuid_user, String name_user, String pass_user, String email_user,
                   Date reg_Date, String firstName_user, String lastName_user, boolean isBlocked,
                   String blockedDescription, UserRole userRole) {
        super(id, uuid_user, name_user, pass_user, email_user, reg_Date, firstName_user, lastName_user,
              isBlocked, blockedDescription);

        this.userRole = userRole;
        this.roleName = userRole.getRole().getRole().getName();
    }

    // getters and setters
    public String getRoleName() {
        return roleName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
        this.roleName = userRole.getRole().getRole().getName();
    }

    // office methods for receiving date of registration of the user in the necessary localization
    public String getUserRegDateByLocale(Locale locale) {
        return GeneralUtils.getDateFormatterByLocale(locale, true).format(getReg_Date());
    }

    public String getUserRegDateByLocale(String locale) {
        return GeneralUtils.getDateFormatterByLocale(locale, true).format(getReg_Date());
    }
}
