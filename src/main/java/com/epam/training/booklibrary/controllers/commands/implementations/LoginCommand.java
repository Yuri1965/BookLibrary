package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.dao.implementations.DAOUser;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.utils.LocaleMessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * Class which contains realization of the "login" team for an entrance of users to system
 */
public class LoginCommand implements ICommand {
    private Logger logger = LogManager.getLogger(LoginCommand.class.getName());
    private static final String INDEX_PAGE = "/index";
    private static final String MAIN_PAGE = "/main";

    /**
     * Realization of the "execute" method for processing of the "login" team from the ICommand interface
     * @param request (type of HttpServletRequest)
     * @param response (type of HttpServletResponse)
     * @return redirect page or resource of the application
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String redirectPage = request.getContextPath();
        String fromIP = "Client IP: " + request.getRemoteAddr();

        //extraction of a locale from session
        Locale locale = (Locale) session.getAttribute("currentLocale");

        //we clear the previous attributes of inquiry if they were
        session.removeAttribute("currentError");
        session.removeAttribute("autoShowModalForm");

        //extraction from inquiry of parameters
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (login != null && !login.isEmpty() && password != null && !password.isEmpty()) {
            DAOUser daoUser = new DAOUser(login, password);

            //attempt of authorization of the user
            try {
                daoUser.getAuthorization();
                session.setAttribute("sessionUser", daoUser);

                if (daoUser.isAuthorized()) {

                    logger.info(fromIP + "\nThe user of " + daoUser.getCurrentUser().getName_user() + " became authorized in system");

                    redirectPage = redirectPage + MAIN_PAGE;
                    response.sendRedirect(redirectPage);

                    return null;
                }
            } catch (MainExceptions.MainErrorException ex) {
                logger.error(fromIP + "\n" + LocaleMessageManager.getMessageValue(ex.getMessage(), Locale.US));

                session.setAttribute("currentError", LocaleMessageManager.getMessageValue(ex.getMessage(), locale));
                session.setAttribute("autoShowModalForm", "#formLogin");

                daoUser = new DAOUser("Guest", "Guest");
                session.setAttribute("sessionUser", daoUser);

                redirectPage = redirectPage + INDEX_PAGE;
                response.sendRedirect(redirectPage);
            } catch (Exception ex){
                logger.error(ex.getMessage());
                throw new ServletException(ex);
            } finally {
            }
        }

        return null;
    }
}
