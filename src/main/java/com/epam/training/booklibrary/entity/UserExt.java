package com.epam.training.booklibrary.entity;

import com.epam.training.booklibrary.controllers.utils.GeneralUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by URA on 24.10.2015.
 */
public class UserExt extends User implements Serializable {
    private static final long serialVersionUID = 14L;

    private UserRole userRole;
    private String roleName;

    public UserExt() {
    }

    public UserExt(int id, String uuid_user, String name_user, String pass_user, String email_user,
                   Date reg_Date, String firstName_user, String lastName_user, boolean isBlocked,
                   String blockedDescription, UserRole userRole) {
        super(id, uuid_user, name_user, pass_user, email_user, reg_Date, firstName_user, lastName_user,
              isBlocked, blockedDescription);

        this.userRole = userRole;
        this.roleName = userRole.getRole().getRole().getName();
    }

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

    public String getUserRegDateByLocale(Locale locale) {
        return GeneralUtils.getDateFormatterByLocale(locale, true).format(getReg_Date());
    }

    public String getUserRegDateByLocale(String locale) {
        return GeneralUtils.getDateFormatterByLocale(locale, true).format(getReg_Date());
    }
}
