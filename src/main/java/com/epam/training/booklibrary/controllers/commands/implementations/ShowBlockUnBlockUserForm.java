package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.datamodels.DataManager;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.datamodels.entity.UserExt;
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
public class ShowBlockUnBlockUserForm implements ICommand {
    private Logger logger = LogManager.getLogger(ShowBlockUnBlockUserForm.class.getName());
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
        String userID = request.getParameter("userID");

        //validation of parameters of inquiry
        boolean errorCheckFound = false;
        StringBuilder errorString = new StringBuilder();

        if (!RequestParamValidator.checkSymbolsNumbers(userID)) {
            errorString.append(LocaleMessageManager.getMessageValue("errorRequestParameter", locale));
            errorCheckFound = true;
        }

        //if there are mistakes that show them
        if (errorCheckFound) {
            session.setAttribute("autoShowModalForm", "#formInfo");
            session.setAttribute("messageFormInfo", errorString.toString());

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);

            return null;
        }

        try {
            //processing of inquiry and preparation of data for the user
            UserExt user = DataManager.getUser(Integer.valueOf(userID));

            if (user == null) {
                session.setAttribute("autoShowModalForm", "#formInfo");
                session.setAttribute("messageFormInfo", LocaleMessageManager.getMessageValue("errorUserNotFound", locale));

                redirectPage = redirectPage + MAIN_PAGE;
                response.sendRedirect(redirectPage);
                return null;
            }

            session.setAttribute("currentTab", "workUsers");

            session.setAttribute("userSelected", user);
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
