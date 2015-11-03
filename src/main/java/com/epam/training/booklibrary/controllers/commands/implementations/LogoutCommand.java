package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.dao.implementations.DAOUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * The class contains realization of the "logout" team for an exit of users from system
 */
public class LogoutCommand implements ICommand {
    private Logger logger = LogManager.getLogger(LogoutCommand.class.getName());
    private static final String INDEX_PAGE = "/index";

    /**
     * Realization of the "execute" method for processing of the "logout" team from the ICommand interface
     * @param request (type of HttpServletRequest)
     * @param response (type of HttpServletResponse)
     * @return redirect page or resource (index) of the application
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String redirectPage = request.getContextPath();

        String fromIP = "Client IP: " + request.getRemoteAddr();
        String userName = ((DAOUser) session.getAttribute("sessionUser")).getCurrentUser().getName_user();
        if (session != null) {
            session.invalidate();
        }
        logger.info(fromIP + "\nThe user of " + userName + " quitted the system");

        redirectPage = redirectPage + INDEX_PAGE;
        response.sendRedirect(redirectPage);
        return null;
    }
}
