package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.dao.implementations.DAOUser;
import com.epam.training.booklibrary.datamodels.DataManager;
import com.epam.training.booklibrary.controllers.utils.GeneralUtils;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
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
 * Class for processing of the closeUserOrder team
 */
public class CloseUserOrderCommand implements ICommand {
    private Logger logger = LogManager.getLogger(CloseUserOrderCommand.class.getName());

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
        String dateEndOrder = request.getParameter("dateEndOrder");

        //validation of parameters of inquiry
        boolean errorCheckFound = false;
        StringBuilder errorString = new StringBuilder();

        if (dateEndOrder == null || dateEndOrder.isEmpty()) {
            errorString.append(LocaleMessageManager.getMessageValue("errorIsEmptyDateEndOrder", locale));
            errorCheckFound = true;
        }

        //if there are mistakes that show them
        if (errorCheckFound) {
            session.setAttribute("currentError", errorString);
            session.setAttribute("autoShowModalForm", "#formCloseOrder");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
            return null;
        }

        try {
            //converting and check of date and time
            Date dateEndToDate = GeneralUtils.getDateFormatterByLocale(locale, false).parse(dateEndOrder);

            UserOrder userOrder = (UserOrder) session.getAttribute("orderSelected");
            int orderID = ((UserOrder) session.getAttribute("orderSelected")).getId();

            Date dateCheckOrder;
            if (userOrder.getBeginOrderDateTime() != null) {
                dateCheckOrder = userOrder.getBeginOrderDateTime();
            } else {
                dateCheckOrder = userOrder.getPreOrderDateTime();
            }

            if (dateEndToDate.before(dateCheckOrder)) {
                throw MainExceptions.getMainErrorException("errorBeforeDateEnd");
            }

            //we close the order for the book
            userOrder = DataManager.closeOrder(orderID, dateEndToDate);

            //we log action of the user
            String fromIP = "Client IP: " + request.getRemoteAddr();
            String userName = ((DAOUser) session.getAttribute("sessionUser")).getUserName();
            logger.info(fromIP + "\nThe user of " + userName + " closed the order for the book " +
                    userOrder.getBookName() + " " + userOrder.getOrderEndDateByLocale(Locale.US));

            session.removeAttribute("orderSelected");
            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
        } catch (ParseException ex) {
            logger.error(ex.getMessage());

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue("errorParseDate", locale));
            session.setAttribute("autoShowModalForm", "#formCloseOrder");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
        } catch (MainExceptions.MainErrorException ex) {
            logger.error(LocaleMessageManager.getMessageValue(ex.getMessage(), Locale.US));

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue(ex.getMessage(), locale));
            session.setAttribute("autoShowModalForm", "#formCloseOrder");

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
