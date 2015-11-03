package com.epam.training.booklibrary.datamodels.enums;

import junit.framework.TestCase;

public class RolesTest extends TestCase {

    public void testGetIndex() throws Exception {

    }

    public void testGetName() throws Exception {

    }

    public void testGetRoleByName() throws Exception {

    }

    public void testGetRoleByIndex() throws Exception {
        assertEquals(Roles.getRoleByIndex(0), Roles.GUEST_LIBRARY);
    }
}