package com.epam.training.booklibrary.controllers.abstracts;

import com.epam.training.booklibrary.controllers.commands.abstracts.AbstractRequestHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Abstract class for the main servlet of the application
 */
public abstract class AbstractBaseServlet extends HttpServlet {
    //Object which contains teams for work with the application
    private AbstractRequestHelper requestHelper;

    /**
     * Returns object which contains teams for work with the application
     * @return requestHelper (type of AbstractRequestHelper)
     */
    protected AbstractRequestHelper getRequestHelper() {
        return requestHelper;
    }

    /**
     * Transfers to a class object which contains the list of teams for work with the application
     * @param requestHelper (type of AbstractRequestHelper)
     *                      Object which contains teams for work with the application
     */
    protected void setRequestHelper(AbstractRequestHelper requestHelper) {
        this.requestHelper = requestHelper;
    }

    /**
     * Method for processing of inquiries of POST (override methods from HttpServlet)
     * @param request (type of HttpServletRequest)
     * @param response (type of HttpServletResponse)
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected abstract void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;

    /**
     * Method for processing of inquiries of GET (override methods from HttpServlet)
     * @param request (type of HttpServletRequest)
     * @param response (type of HttpServletResponse)
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected abstract void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException;
}
