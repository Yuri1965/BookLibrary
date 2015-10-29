package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.dao.interfaces.IDAOUser;
import com.epam.training.booklibrary.entity.User;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.utils.ApplicationConfigManager;
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
 * Created by URA on 09.10.2015.
 */
public class RegistrationUserCommand implements ICommand {
    private Logger logger = LogManager.getLogger(RegistrationUserCommand.class.getName());

    private static final String INDEX_PAGE = "/index";

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
        String login = request.getParameter("loginUser");
        String password = request.getParameter("passwordUser");
        String email = request.getParameter("emailUser");
        String firstName = request.getParameter("firstNameUser");
        String lastName = request.getParameter("lastNameUser");

        // проверка на обязательность заполнения полей
        if (login == null || login.isEmpty()
            || password == null || password.isEmpty()
            || email == null || email.isEmpty()
            || firstName == null || firstName.isEmpty()) {

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue("errorInputObligationField", locale));
            session.setAttribute("autoShowModalForm", "#formRegistration");

            redirectPage = redirectPage + INDEX_PAGE;
            response.sendRedirect(redirectPage);
            return null;
        }

        boolean errorCheckFound = false;
        StringBuilder errorString = new StringBuilder();

        if (lastName == null) { lastName = ""; }

        // валидация по параметрам
        if (!RequestParamValidator.checkSymbolsLogin(login) || !RequestParamValidator.checkLengthLogin(login)) {
            errorString.append(String.format(LocaleMessageManager.getMessageValue("errorLoginLength", locale),
                    ApplicationConfigManager.getConfigValue("loginMinLength", RequestParamValidator.MIN_LENGTH_LOGIN),
                    ApplicationConfigManager.getConfigValue("loginMaxLength", RequestParamValidator.MAX_LENGTH_LOGIN)));
            errorString.append(LocaleMessageManager.getMessageValue("errorLoginSymbols", locale));
            errorCheckFound = true;
        }

        if (!RequestParamValidator.checkSymbolsPassword(password) || !RequestParamValidator.checkLengthPassword(password)) {
            errorString.append(String.format(LocaleMessageManager.getMessageValue("errorPasswordLength", locale),
                    ApplicationConfigManager.getConfigValue("passwordMinLength", RequestParamValidator.MIN_LENGTH_PASSWORD),
                    ApplicationConfigManager.getConfigValue("passwordMaxLength", RequestParamValidator.MAX_LENGTH_PASSWORD)));
            errorString.append(LocaleMessageManager.getMessageValue("errorPasswordSymbols", locale));
            errorCheckFound = true;
        }

        if (!RequestParamValidator.checkSymbolsEmail(email) || !RequestParamValidator.checkLengthEmail(email)) {
            errorString.append(String.format(LocaleMessageManager.getMessageValue("errorEmailLength", locale),
                    ApplicationConfigManager.getConfigValue("emailMaxLength", RequestParamValidator.MAX_LENGTH_EMAIL)));
            errorString.append(LocaleMessageManager.getMessageValue("errorEmailSymbols", locale));
            errorCheckFound = true;
        }

        if (!RequestParamValidator.checkSymbolsFirstName(firstName) || !RequestParamValidator.checkLengthFirstName(firstName)) {
            errorString.append(String.format(LocaleMessageManager.getMessageValue("errorFirstNameLength", locale),
                    ApplicationConfigManager.getConfigValue("firstNameMaxLength", RequestParamValidator.MAX_LENGTH_FIRST_NAME)));
            errorString.append(LocaleMessageManager.getMessageValue("errorFirstNameSymbols", locale));
            errorCheckFound = true;
        }

        if (lastName.length() > 0) {
            if (!RequestParamValidator.checkSymbolsLastName(lastName) || !RequestParamValidator.checkLengthLastName(lastName)) {
                errorString.append(String.format(LocaleMessageManager.getMessageValue("errorLastNameLength", locale),
                        ApplicationConfigManager.getConfigValue("lastNameMaxLength", RequestParamValidator.MAX_LENGTH_LAST_NAME)));
                errorString.append(LocaleMessageManager.getMessageValue("errorLastNameSymbols", locale));
                errorCheckFound = true;
            }
        }

        // если есть ошибки то показываем их
        if (errorCheckFound) {
            session.setAttribute("currentError", errorString);
            session.setAttribute("autoShowModalForm", "#formRegistration");

            redirectPage = redirectPage + INDEX_PAGE;
            response.sendRedirect(redirectPage);
            return null;
        }

        IDAOUser daoUser = (IDAOUser) session.getAttribute("sessionUser");

        try {
            User user = daoUser.createUser(login, password, email, firstName, lastName, "READER");

            StringBuilder registerMessage = new StringBuilder();
            registerMessage.append(String.format(LocaleMessageManager.getMessageValue("registrationOKMessage", locale),
                    user.getName_user()));

            session.setAttribute("autoShowModalForm", "#formInfo");
            session.setAttribute("messageFormInfo", registerMessage);

            redirectPage = redirectPage + INDEX_PAGE;
            response.sendRedirect(redirectPage);

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        } catch (NamingException ex) {
            logger.error(ex.getMessage());
        } catch (MainExceptions.MainErrorException ex) {
            logger.error(LocaleMessageManager.getMessageValue(ex.getMessage(), Locale.US));

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue(ex.getMessage(), locale));
            session.setAttribute("autoShowModalForm", "#formRegistration");

            redirectPage = redirectPage + INDEX_PAGE;
            response.sendRedirect(redirectPage);
        } finally {
        }

        return null;
    }
}
