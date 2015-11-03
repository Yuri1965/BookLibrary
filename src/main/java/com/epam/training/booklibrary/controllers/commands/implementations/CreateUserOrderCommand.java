package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.datamodels.DataManager;
import com.epam.training.booklibrary.controllers.utils.GeneralUtils;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.dao.implementations.DAOUser;
import com.epam.training.booklibrary.datamodels.entity.UserOrder;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.utils.LocaleMessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Class for processing of the createUserOrder team
 */
public class CreateUserOrderCommand implements ICommand {
    private Logger logger = LogManager.getLogger(CreateUserOrderCommand.class.getName());

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
        String bookID = request.getParameter("bookID");
        String typeOrderID = request.getParameter("typeOrderID");
        String datePrev = request.getParameter("datePrev");
        int userID = ((DAOUser) session.getAttribute("sessionUser")).getCurrentUser().getId();

        //validation of parameters of inquiry
        boolean errorCheckFound = false;
        StringBuilder errorString = new StringBuilder();

        if (!RequestParamValidator.checkSymbolsNumbers(bookID) || !RequestParamValidator.checkSymbolsNumbers(typeOrderID)) {
            errorString.append(LocaleMessageManager.getMessageValue("errorRequestParameter", locale));
            errorCheckFound = true;
        }

        if (datePrev == null || datePrev.isEmpty()) {
            errorString.append(LocaleMessageManager.getMessageValue("errorIsEmptyDatePrev", locale));
            errorCheckFound = true;
        }

        //if there are mistakes that show them
        if (errorCheckFound) {
            session.setAttribute("currentError", errorString);
            session.setAttribute("autoShowModalForm", "#formCreateOrder");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
            return null;
        }

        try {
            //converting and check of date and time
            Date currentDateTime = new Date();
            Date datePrevToDate = GeneralUtils.getDateFormatterByLocale(locale, false).parse(datePrev);

            if (datePrevToDate.before(currentDateTime)) {
                throw MainExceptions.getMainErrorException("errorBeforeDate");
            }

            //we keep the order for the book
            UserOrder userOrder = DataManager.createOrder(Integer.valueOf(bookID), Integer.valueOf(userID), Integer.valueOf(typeOrderID), datePrevToDate);

            //we log action of the user
            String fromIP = "Client IP: " + request.getRemoteAddr();
            String userName = ((DAOUser) session.getAttribute("sessionUser")).getUserName();
            logger.info(fromIP + "\nThe user of " + userName + " created the order for the book " +
                    userOrder.getBookName() + " on " + userOrder.getOrderPreDateByLocale(Locale.US));

            session.removeAttribute("bookSelected");
            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue("errorParseDate", locale));
            session.setAttribute("autoShowModalForm", "#formCreateOrder");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
        } catch (MainExceptions.MainErrorException ex) {
            logger.error(LocaleMessageManager.getMessageValue(ex.getMessage(), Locale.US));

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue(ex.getMessage(), locale));
            session.setAttribute("autoShowModalForm", "#formCreateOrder");

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

