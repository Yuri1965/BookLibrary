package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.dao.implementations.DAOUser;
import com.epam.training.booklibrary.dao.interfaces.IDAOUser;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.utils.LocaleMessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

public class LoginCommand implements ICommand {
    private Logger logger = LogManager.getLogger(LoginCommand.class.getName());
    private static final String INDEX_PAGE = "/index";
    private static final String MAIN_PAGE = "/main";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String redirectPage = request.getContextPath();

        //извлечение локали из сессии
        Locale locale = (Locale) session.getAttribute("currentLocale");

        // очищаем предыдущие атрибуты запроса если они были
        session.removeAttribute("currentError");
        session.removeAttribute("autoShowModalForm");

        //извлечение из запроса параметров
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (login != null && !login.isEmpty() && password != null && !password.isEmpty()) {
            IDAOUser daoUser = new DAOUser(login, password);

            try {
                daoUser.getAuthorization();
                session.setAttribute("sessionUser", daoUser);

                if (daoUser.isAuthorized()) {
                    redirectPage = redirectPage + MAIN_PAGE;
                    response.sendRedirect(redirectPage);

                    return null;
                }

            } catch (SQLException ex) {
                logger.error(ex.getMessage());
            } catch (NamingException ex) {
                logger.error(ex.getMessage());
            } catch (MainExceptions.MainErrorException ex) {
                logger.error(LocaleMessageManager.getMessageValue(ex.getMessage(), Locale.US));

                session.setAttribute("currentError", LocaleMessageManager.getMessageValue(ex.getMessage(), locale));
                session.setAttribute("autoShowModalForm", "#formLogin");

                daoUser = new DAOUser("Guest", "Guest");
                session.setAttribute("sessionUser", daoUser);

                redirectPage = redirectPage + INDEX_PAGE;
                response.sendRedirect(redirectPage);
            } finally {
            }
        }

        return null;
    }
}
