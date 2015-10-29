package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.controllers.utils.DataManager;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
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

/**
 * Created by URA on 26.10.2015.
 */
public class BlockUnBlockUser implements ICommand {
    private Logger logger = LogManager.getLogger(BlockUnBlockUser.class.getName());

    private static final String MAIN_PAGE = "/main";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        //извлечение локали из сессии
        Locale locale = (Locale) session.getAttribute("currentLocale");

        String redirectPage = request.getContextPath();

        // очищаем предыдущие атрибуты запроса если они были
        session.removeAttribute("currentError");
        session.removeAttribute("autoShowModalForm");

        //извлечение из запроса параметров
        String userID = request.getParameter("userID");
        String actionMode = request.getParameter("actionMode");
        String blockedDescription = request.getParameter("blockedDescription");

        //проверка параметров запроса
        boolean errorCheckFound = false;
        StringBuilder errorString = new StringBuilder();

        if (!RequestParamValidator.checkSymbolsNumbers(userID)) {
            errorString.append(LocaleMessageManager.getMessageValue("errorRequestParameter", locale));
            errorCheckFound = true;
        }

        if (blockedDescription == null || blockedDescription.isEmpty()) {
            errorString.append(LocaleMessageManager.getMessageValue("errorIsEmptyBlockedDescription", locale));
            errorCheckFound = true;
        }

        if (actionMode == null || actionMode.isEmpty()
                || (!actionMode.equalsIgnoreCase("setBlock") && !actionMode.equalsIgnoreCase("setUnBlock"))) {
            errorString.append(LocaleMessageManager.getMessageValue("errorRequestParameter", locale));
            errorCheckFound = true;
        }

        // если есть ошибки то показываем их
        if (errorCheckFound) {
            session.setAttribute("currentError", errorString);
            session.setAttribute("autoShowModalForm", "#formBlockUnBlockUser");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
            return null;
        }

        try {
            if (actionMode.equalsIgnoreCase("setBlock")) {
                DataManager.blockUser(Integer.valueOf(userID), blockedDescription);
            } else {
                DataManager.unBlockUser(Integer.valueOf(userID));
            }

            session.removeAttribute("userSelected");
            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        } catch (NamingException ex) {
            logger.error(ex.getMessage());
        } catch (MainExceptions.MainErrorException ex) {
            logger.error(LocaleMessageManager.getMessageValue(ex.getMessage(), Locale.US));

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue(ex.getMessage(), locale));
            session.setAttribute("autoShowModalForm", "#formBlockUnBlockUser");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
        } catch (Exception ex){
            logger.error(ex.getMessage());
        } finally {
        }

        return null;
    }
}
