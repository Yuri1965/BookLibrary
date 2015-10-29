package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.controllers.utils.DataManager;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
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
 * Created by URA on 28.10.2015.
 */
public class SaveBookCommand implements ICommand {
    private Logger logger = LogManager.getLogger(SaveBookCommand.class.getName());

    private static final String MAIN_PAGE = "/main";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        //извлечение параметров из сессии
        Locale locale = (Locale) session.getAttribute("currentLocale");
        int currentYear = (int) session.getAttribute("currentYear");

        String redirectPage = request.getContextPath();

        // очищаем предыдущие атрибуты запроса если они были
        session.removeAttribute("currentError");
        session.removeAttribute("autoShowModalForm");

        //извлечение из запроса параметров
        String actionModeBookForm = request.getParameter("actionModeBookForm");
        String bookGenreID = request.getParameter("bookGenreID");
        String bookID = request.getParameter("bookID");
        String bookName = request.getParameter("bookName");
        String bookAuthorID = request.getParameter("bookAuthorID");
        String bookPublisherID = request.getParameter("bookPublisherID");
        String bookPublishYear = request.getParameter("bookPublishYear");
        String bookISBN = request.getParameter("bookISBN");
        String bookNumberCopies = request.getParameter("bookNumberCopies");
        String bookShortDescription = request.getParameter("bookShortDescription");

        // проверка на обязательность заполнения полей
        if (actionModeBookForm == null || actionModeBookForm.isEmpty() || bookGenreID == null || bookGenreID.isEmpty()
                || bookID == null || bookID.isEmpty() || bookName == null || bookName.isEmpty()
                || bookAuthorID == null || bookAuthorID.isEmpty() || bookPublisherID == null || bookPublisherID.isEmpty()
                || bookPublishYear == null || bookPublishYear.isEmpty() || bookPublishYear == null || bookPublishYear.isEmpty()
                || bookISBN == null || bookISBN.isEmpty() || bookNumberCopies == null || bookNumberCopies.isEmpty()
                || bookShortDescription == null || bookShortDescription.isEmpty()
                ) {
            session.setAttribute("currentError", LocaleMessageManager.getMessageValue("errorInputObligationField", locale));
            session.setAttribute("autoShowModalForm", "#formBook");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
            return null;
        }

        //проверка по параметрам
        boolean errorCheckFound = false;
        StringBuilder errorString = new StringBuilder();

        if (!RequestParamValidator.checkSymbolsNumbers(bookGenreID) || !RequestParamValidator.checkSymbolsNumbers(bookID)
                || !RequestParamValidator.checkSymbolsNumbers(bookAuthorID) || !RequestParamValidator.checkSymbolsNumbers(bookPublisherID)
                || (!actionModeBookForm.equalsIgnoreCase("addBook") && !actionModeBookForm.equalsIgnoreCase("editBook"))
                ) {
            errorString.append(LocaleMessageManager.getMessageValue("errorRequestParameter", locale));
            errorCheckFound = true;
        }

        if (!RequestParamValidator.checkLengthBookName(bookName)) {
            errorString.append(String.format(LocaleMessageManager.getMessageValue("errorBookNameLength", locale),
                    ApplicationConfigManager.getConfigValue("bookNameMaxLength", RequestParamValidator.MAX_LENGTH_BOOK_NAME)));
            errorCheckFound = true;
        }

        if (!RequestParamValidator.checkLengthBookISBN(bookISBN)) {
            errorString.append(String.format(LocaleMessageManager.getMessageValue("errorBookISBNLength", locale),
                    ApplicationConfigManager.getConfigValue("bookISBNMaxLength", RequestParamValidator.MAX_LENGTH_BOOK_ISBN)));
            errorCheckFound = true;
        }

        if (!RequestParamValidator.checkLengthBookDescription(bookShortDescription)) {
            errorString.append(String.format(LocaleMessageManager.getMessageValue("errorBookDescriptionLength", locale),
                    ApplicationConfigManager.getConfigValue("bookDescriptionMaxLength", RequestParamValidator.MAX_LENGTH_BOOK_DESCRIPTION)));
            errorCheckFound = true;
        }

        if (!RequestParamValidator.checkValueBookPublishYear(Integer.valueOf(bookPublishYear), currentYear)) {
            errorString.append(String.format(LocaleMessageManager.getMessageValue("errorPublishYearValue", locale),
                    ApplicationConfigManager.getConfigValue("publishYearMinValue", RequestParamValidator.MIN_VALUE_BOOK_PUBLISH_YEAR)));
            errorCheckFound = true;
        }

        if (!RequestParamValidator.checkValueBookNumberCopies(Integer.valueOf(bookNumberCopies),
            Integer.valueOf(ApplicationConfigManager.getConfigValue("bookNumberCopiesMaxValue", RequestParamValidator.MAX_VALUE_BOOK_NUMBER_COPIES)))) {
            errorString.append(String.format(LocaleMessageManager.getMessageValue("errorBookNumberCopies", locale),
                    ApplicationConfigManager.getConfigValue("bookNumberCopiesMinValue", RequestParamValidator.MIN_VALUE_BOOK_NUMBER_COPIES),
                    ApplicationConfigManager.getConfigValue("bookNumberCopiesMaxValue", RequestParamValidator.MAX_VALUE_BOOK_NUMBER_COPIES)));
            errorCheckFound = true;
        }

        // если есть ошибки то показываем их
        if (errorCheckFound) {
            session.setAttribute("currentError", errorString);
            session.setAttribute("autoShowModalForm", "#formBook");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
            return null;
        }

        try {
            if (actionModeBookForm.equalsIgnoreCase("addBook")) {
                DataManager.createBook(Integer.valueOf(bookAuthorID), Integer.valueOf(bookGenreID),
                        Integer.valueOf(bookPublisherID), Integer.valueOf(bookPublishYear),
                        bookISBN, bookName, bookShortDescription, Integer.valueOf(bookNumberCopies), null);
            } else {
                DataManager.updateBook(Integer.valueOf(bookID), Integer.valueOf(bookAuthorID),
                        Integer.valueOf(bookGenreID), Integer.valueOf(bookPublisherID), Integer.valueOf(bookPublishYear),
                        bookISBN, bookName, bookShortDescription, Integer.valueOf(bookNumberCopies), null);
            }

            //удаляем аттрибуты из сессии
            session.removeAttribute("bookGenreName");
            session.removeAttribute("listAuthors");
            session.removeAttribute("listPublishers");
            session.removeAttribute("bookSelected");
            session.removeAttribute("actionModeBookForm");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        } catch (NamingException ex) {
            logger.error(ex.getMessage());
        } catch (MainExceptions.MainErrorException ex) {
            logger.error(LocaleMessageManager.getMessageValue(ex.getMessage(), Locale.US));

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue(ex.getMessage(), locale));
            session.setAttribute("autoShowModalForm", "#formBook");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
        } catch (Exception ex){
            logger.error(ex.getMessage());
        } finally {
        }

        return null;
    }
}
