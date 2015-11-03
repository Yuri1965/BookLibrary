package com.epam.training.booklibrary.controllers.implementations;

import com.epam.training.booklibrary.controllers.abstracts.AbstractBaseServlet;
import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.controllers.commands.implementations.ActionCommandFabric;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The main class a servlet (controler) for processing of inquiries of the client
 */

public class BaseServlet extends AbstractBaseServlet {
    /**
     * Method of initialization of object of a class
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        super.init();
        this.setRequestHelper(ActionCommandFabric.getInstance());
    }

    /**
     * Method of processing of inquiries of POST
     * @param request (type of HttpServletRequest)
     * @param response (type of HttpServletResponse)
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Method of processing of inquiries of GET
     * @param request (type of HttpServletRequest)
     * @param response (type of HttpServletResponse)
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * The method takes team from inquiry of the client and transfers performance to the specific handler of team
     * @param request (type of HttpServletRequest)
     * @param response (type of HttpServletResponse)
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String page = null;

        //definition of the team which came from the client
        ICommand command = this.getRequestHelper().getCommand(request);

        /*call of the realized execute method
        the Command interface and transfer of parameters to a class handler of concrete team*/
        // the method returns the page of the answer
        page = command.execute(request, response);

        if (page != null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
            dispatcher.forward(request, response);
        }
    }
}
