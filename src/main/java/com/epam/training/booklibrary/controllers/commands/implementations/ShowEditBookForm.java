package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.datamodels.DataManager;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.entity.Author;
import com.epam.training.booklibrary.entity.Book;
import com.epam.training.booklibrary.entity.Publisher;
import com.epam.training.booklibrary.utils.LocaleMessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class for processing of the showEditBookForm team
 */
public class ShowEditBookForm implements ICommand {
    private Logger logger = LogManager.getLogger(ShowEditBookForm.class.getName());

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

        //validation of parameters of inquiry
        boolean errorCheckFound = false;
        StringBuilder errorString = new StringBuilder();

        if (!RequestParamValidator.checkSymbolsNumbers(bookID)) {
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
            Book book = DataManager.getBook(Integer.valueOf(bookID));

            if (book == null) {
                session.setAttribute("autoShowModalForm", "#formInfo");
                session.setAttribute("messageFormInfo", LocaleMessageManager.getMessageValue("errorBookNotFound", locale));

                redirectPage = redirectPage + MAIN_PAGE;
                response.sendRedirect(redirectPage);
                return null;
            }

            String bookGenreName = "";
            if (locale.equals("en_us")) {
                bookGenreName = book.getBookGenre().getNameEN();
            } else {
                bookGenreName = book.getBookGenre().getNameRU();
            }
            session.setAttribute("bookGenreName", bookGenreName);

            Date currentDate = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            int currentYear = calendar.get(Calendar.YEAR);;

            session.setAttribute("currentTab", "books");
            session.setAttribute("currentYear", currentYear);

            List<Author> listAuthors = DataManager.getAuthors();
            session.setAttribute("listAuthors", listAuthors);

            List<Publisher> listPublishers = DataManager.getPublishers();
            session.setAttribute("listPublishers", listPublishers);

            session.setAttribute("bookSelected", book);
            session.setAttribute("actionModeBookForm", "editBook");
            session.setAttribute("autoShowModalForm", "#formBook");

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
