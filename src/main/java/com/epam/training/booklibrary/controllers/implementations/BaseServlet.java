package com.epam.training.booklibrary.controllers.implementations;

import com.epam.training.booklibrary.controllers.abstracts.AbstractBaseServlet;
import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.controllers.commands.implementations.RequestHelper;
import org.apache.logging.log4j.LogManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by URA on 20.09.2015.
 */
//@WebServlet(name = "Main")
public class BaseServlet extends AbstractBaseServlet implements Servlet {

    @Override
    public void init() throws ServletException {
        super.init();
        this.setRequestHelper(RequestHelper.getInstance());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String page = null;

        try {
            //определение команды, пришедшей из JSP
            ICommand command = this.getRequestHelper().getCommand(request);

            /*вызов реализованного метода execute() интерфейса Command и передача параметров
            классу-обработчику конкретной команды*/
            // метод возвращает страницу ответа
            page = command.execute(request, response);

        } catch (ServletException e) {
            LogManager.getLogger(BaseServlet.class.getName()).error(e.getMessage());
//            //генерация сообщения об ошибке
//            request.setAttribute("errorMessage",
//                    MessageManager.getInstance().getProperty(MessageManager.SERVLET_EXCEPTION_ERROR_MESSAGE));
//            //вызов JSP-страницы c cообщением об ошибке
//            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        } catch (IOException e) {
            LogManager.getLogger(BaseServlet.class.getName()).error(e.getMessage());
//            request.setAttribute("errorMessage",
//                    MessageManager.getInstance().getProperty(MessageManager.IO_EXCEPTION_ERROR_MESSAGE));
//            page = ConfigurationManager.getInstance().getProperty(ConfigurationManager.ERROR_PAGE_PATH);
        }

        if (page != null) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
            dispatcher.forward(request, response);
        }
    }
}
