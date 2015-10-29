package com.epam.training.booklibrary.controllers.abstracts;

import com.epam.training.booklibrary.controllers.commands.abstracts.AbstractRequestHelper;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by URA on 20.09.2015.
 */
public abstract class AbstractBaseServlet extends HttpServlet implements Servlet {
    //объект, содержащий список возможных команд и их реализацию
    private AbstractRequestHelper requestHelper;

    protected AbstractRequestHelper getRequestHelper() {
        return requestHelper;
    }

    protected void setRequestHelper(AbstractRequestHelper requestHelper) {
        this.requestHelper = requestHelper;
    }

    protected abstract void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
