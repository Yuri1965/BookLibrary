package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.dao.implementations.DAOUser;
import com.epam.training.booklibrary.dao.interfaces.IDAOUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by URA on 04.10.2015.
 */
public class LogoutCommand implements ICommand {
    private Logger logger = LogManager.getLogger(LogoutCommand.class.getName());
    private static final String INDEX_PAGE = "/index";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String redirectPage = request.getContextPath();

        // очищаем предыдущие атрибуты запроса если они были
        session.removeAttribute("currentError");
        session.removeAttribute("autoShowModalForm");
//        request.removeAttribute("currentError");
//        request.removeAttribute("autoShowModalForm");

        if (session != null) {
            IDAOUser daoUser = new DAOUser("Guest", "Guest");
            session.setAttribute("sessionUser", daoUser);
        }

        redirectPage = redirectPage + INDEX_PAGE;
        response.sendRedirect(redirectPage);
        return null;
    }
}
