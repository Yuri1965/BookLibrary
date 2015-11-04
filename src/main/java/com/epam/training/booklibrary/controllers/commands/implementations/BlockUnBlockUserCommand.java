package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.dao.implementations.DAOUser;
import com.epam.training.booklibrary.datamodels.DataManager;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.datamodels.entity.UserExt;
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
 * Class for processing of the showBlockUnBlockUserForm team
 */
public class BlockUnBlockUserCommand implements ICommand {
    private Logger logger = LogManager.getLogger(BlockUnBlockUserCommand.class.getName());

    private static final String MAIN_PAGE = "/main";

    /**
     * Method handler of inquiry of the client
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return returns a resource for formation of the answer to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        //extraction of localization from session
        Locale locale = (Locale) session.getAttribute("currentLocale");

        String redirectPage = request.getContextPath();

        //we clear the previous attributes of inquiry if they were
        session.removeAttribute("currentError");
        session.removeAttribute("autoShowModalForm");

        //extraction from inquiry of parameters
        String actionMode = request.getParameter("actionMode");
        String blockedDescription = request.getParameter("blockedDescription");

        //validation of parameters of inquiry
        boolean errorCheckFound = false;
        StringBuilder errorString = new StringBuilder();

        if (blockedDescription == null || blockedDescription.isEmpty()) {
            errorString.append(LocaleMessageManager.getMessageValue("errorIsEmptyBlockedDescription", locale));
            errorCheckFound = true;
        }

        if (actionMode == null || actionMode.isEmpty()
                || (!actionMode.equalsIgnoreCase("setBlock") && !actionMode.equalsIgnoreCase("setUnBlock"))) {
            errorString.append(LocaleMessageManager.getMessageValue("errorRequestParameter", locale));
            errorCheckFound = true;
        }

        //if there are mistakes that show them
        if (errorCheckFound) {
            session.setAttribute("currentError", errorString);
            session.setAttribute("autoShowModalForm", "#formBlockUnBlockUser");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
            return null;
        }

        try {
            //processing of inquiry and preparation of data for the user
            UserExt userExt;
            String fromIP = "Client IP: " + request.getRemoteAddr();
            String userName = ((DAOUser) session.getAttribute("sessionUser")).getUserName();
            int userID = ((UserExt) session.getAttribute("userSelected")).getId();

            if (actionMode.equalsIgnoreCase("setBlock")) {
                userExt = DataManager.blockUser(userID, blockedDescription);
                //we log action of the user
                logger.info(fromIP + "\nThe user of " + userName + " blocked the user of " + userExt.getName_user());
            } else {
                userExt = DataManager.unBlockUser(userID);
                //we log action of the user
                logger.info(fromIP + "\nThe user of " + userName + " unblocked the user of " + userExt.getName_user());
            }

            session.removeAttribute("userSelected");
            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
        } catch (MainExceptions.MainErrorException ex) {
            logger.error(LocaleMessageManager.getMessageValue(ex.getMessage(), Locale.US));

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue(ex.getMessage(), locale));
            session.setAttribute("autoShowModalForm", "#formBlockUnBlockUser");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
        } catch (Exception ex){
            logger.error(ex.getMessage());
            throw new ServletException(ex);
        } finally {
        }

        return null;
    }
}
