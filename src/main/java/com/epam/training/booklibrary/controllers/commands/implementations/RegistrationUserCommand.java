package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.dao.implementations.DAOUser;
import com.epam.training.booklibrary.dao.interfaces.IDAOUser;
import com.epam.training.booklibrary.entity.User;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.utils.ApplicationConfigManager;
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
 * Class for processing of the registrationUser team
 */
public class RegistrationUserCommand implements ICommand {
    private Logger logger = LogManager.getLogger(RegistrationUserCommand.class.getName());

    private static final String INDEX_PAGE = "/index";

    /**
     * Method handler of inquiry of the client
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return returns a resource for formation of the answer to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        //extraction of localization from session
        Locale locale = (Locale) session.getAttribute("currentLocale");

        String redirectPage = request.getContextPath();

        //we clear the previous attributes of inquiry if they were
        session.removeAttribute("currentError");
        session.removeAttribute("autoShowModalForm");

        //extraction from inquiry of parameters
        String login = request.getParameter("loginUser");
        String password = request.getParameter("passwordUser");
        String email = request.getParameter("emailUser");
        String firstName = request.getParameter("firstNameUser");
        String lastName = request.getParameter("lastNameUser");

        //check on obligation of filling of fields
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

        //validation of parameters of inquiry
        boolean errorCheckFound = false;
        StringBuilder errorString = new StringBuilder();

        if (lastName == null) { lastName = ""; }

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

        //if there are mistakes that show them
        if (errorCheckFound) {
            session.setAttribute("currentError", errorString);
            session.setAttribute("autoShowModalForm", "#formRegistration");

            redirectPage = redirectPage + INDEX_PAGE;
            response.sendRedirect(redirectPage);
            return null;
        }

        IDAOUser daoUser = (IDAOUser) session.getAttribute("sessionUser");

        try {
            //processing of inquiry and preparation of data for the user
            User user = daoUser.createUser(login, password, email, firstName, lastName, "READER");

            //we log action of the user
            String fromIP = "Client IP: " + request.getRemoteAddr();
            String userName = user.getName_user();
            logger.info(fromIP + "\nThe user of " + userName + " was registered in system");

            StringBuilder registerMessage = new StringBuilder();
            registerMessage.append(String.format(LocaleMessageManager.getMessageValue("registrationOKMessage", locale),
                    user.getName_user()));

            session.setAttribute("autoShowModalForm", "#formInfo");
            session.setAttribute("messageFormInfo", registerMessage);

            redirectPage = redirectPage + INDEX_PAGE;
            response.sendRedirect(redirectPage);
        } catch (MainExceptions.MainErrorException ex) {
            logger.error(LocaleMessageManager.getMessageValue(ex.getMessage(), Locale.US));

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue(ex.getMessage(), locale));
            session.setAttribute("autoShowModalForm", "#formRegistration");

            redirectPage = redirectPage + INDEX_PAGE;
            response.sendRedirect(redirectPage);
        } catch (Exception ex){
            logger.error(ex.getMessage());
            throw new ServletException(ex);
        } finally {
        }

        return null;
    }
}
