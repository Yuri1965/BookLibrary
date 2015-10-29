package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.controllers.utils.DataManager;
import com.epam.training.booklibrary.controllers.utils.GeneralUtils;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.dao.implementations.DAOUser;
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
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by URA on 28.10.2015.
 */
public class CreateUserOrder implements ICommand {
    private Logger logger = LogManager.getLogger(CreateUserOrder.class.getName());

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
        String bookID = request.getParameter("bookID");
        String typeOrderID = request.getParameter("typeOrderID");
        String datePrev = request.getParameter("datePrev");
        int userID = ((DAOUser) session.getAttribute("sessionUser")).getCurrentUser().getId();

        //проверка параметров запроса
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

        // если есть ошибки то показываем их
        if (errorCheckFound) {
            session.setAttribute("currentError", errorString);
            session.setAttribute("autoShowModalForm", "#formCreateOrder");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
            return null;
        }

        try {
            // конвертация и проверка даты и времени
            Date currentDateTime = new Date();
            Date datePrevToDate = GeneralUtils.getDateFormatterByLocale(locale, false).parse(datePrev);

            if (datePrevToDate.before(currentDateTime)) {
                throw MainExceptions.getMainErrorException("errorBeforeDate");
            }

            // сохраняем заказ
            DataManager.createOrder(Integer.valueOf(bookID), Integer.valueOf(userID), Integer.valueOf(typeOrderID), datePrevToDate);

            session.removeAttribute("bookSelected");
            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        } catch (NamingException ex) {
            logger.error(ex.getMessage());
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
        } finally {
        }

        return null;
    }
}

