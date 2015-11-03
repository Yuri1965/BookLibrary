package com.epam.training.booklibrary.controllers.commands.interfaces;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The interface contains a method of processing of inquiries and
 * team of the client for realization in classes successors
 */
public interface ICommand {
    /**
     * Method handler of inquiry of the client
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return returns a resource for formation of the answer to the client
     * @throws ServletException
     * @throws IOException
     */
    public String execute(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException;
}
