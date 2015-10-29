package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.controllers.utils.DataManager;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.entity.Author;
import com.epam.training.booklibrary.entity.Book;
import com.epam.training.booklibrary.entity.Publisher;
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
import java.util.List;
import java.util.Locale;

/**
 * Created by URA on 28.10.2015.
 */
public class ShowEditBookForm implements ICommand {
    private Logger logger = LogManager.getLogger(ShowEditBookForm.class.getName());

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

        boolean errorCheckFound = false;
        StringBuilder errorString = new StringBuilder();

        if (!RequestParamValidator.checkSymbolsNumbers(bookID)) {
            errorString.append(LocaleMessageManager.getMessageValue("errorRequestParameter", locale));
            errorCheckFound = true;
        }

        // если есть ошибки то показываем их
        if (errorCheckFound) {
            session.setAttribute("autoShowModalForm", "#formInfo");
            session.setAttribute("messageFormInfo", errorString.toString());

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
            return null;
        }

        try {
            Book book = DataManager.getBook(Integer.valueOf(bookID));

            if (book == null) {
                session.setAttribute("autoShowModalForm", "#formInfo");
                session.setAttribute("messageFormInfo", LocaleMessageManager.getMessageValue("errorBookNotFound", locale));

                redirectPage = redirectPage + MAIN_PAGE;
                response.sendRedirect(redirectPage);
                return null;
            }

            String bookGenreName = "";
            if (locale.equals("ru-ru")) {
                bookGenreName = book.getBookGenre().getNameRU();
            } else {
                bookGenreName = book.getBookGenre().getNameEN();
            }
            session.setAttribute("bookGenreName", bookGenreName);

            List<Author> listAuthors = DataManager.getAuthors();
            session.setAttribute("listAuthors", listAuthors);

            List<Publisher> listPublishers = DataManager.getPublishers();
            session.setAttribute("listPublishers", listPublishers);

            session.setAttribute("bookSelected", book);
            session.setAttribute("actionModeBookForm", "editBook");
            session.setAttribute("autoShowModalForm", "#formBook");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        } catch (NamingException ex) {
            logger.error(ex.getMessage());
        } catch (Exception ex){
            logger.error(ex.getMessage());
        } finally {
        }

        return null;
    }
}
