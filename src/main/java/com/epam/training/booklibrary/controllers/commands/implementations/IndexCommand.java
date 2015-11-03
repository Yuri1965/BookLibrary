package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.datamodels.DataManager;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.dao.implementations.DAOSearchBookCriteria;
import com.epam.training.booklibrary.entity.Book;
import com.epam.training.booklibrary.entity.BookGenre;
import com.epam.training.booklibrary.entity.BookSection;
import com.epam.training.booklibrary.utils.ApplicationConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Class for processing of the index team
 */
public class IndexCommand implements ICommand {
    private Logger logger = LogManager.getLogger(IndexCommand.class.getName());
    private static final String INDEX_PAGE = "/WEB-INF/pages/index.jsp";

    // for calculation of number of records and pages
    private static final int COUNT_RECORD_PAGE = 0;
    private static final int COUNT_PAGE = 0;

    // number of records on the page
    private static final int RECORD_PAGE = Integer.valueOf(ApplicationConfigManager.getConfigValue("recordsBookPage", "6"));
    // активная страница по умолчанию
    private static final String DEFAULT_PAGE = "1";

    // type of search
    private static final String TYPE_SEARCH_BY_NAME = "1";
    private static final String TYPE_SEARCH_BY_AUTHOR = "2";

    // section of literature by default (all sections)
    private static final String DEFAULT_BOOK_SECTION = "0";
    // genre of books by default (all genres)
    private static final String DEFAULT_BOOK_GENRE = "0";

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

        try {
            //extraction from inquiry of parameters
            String paramErrorClear = request.getParameter("errorClear");

            String paramTypeSearchBook = request.getParameter("typeSearchBook");
            String paramSearchTextBook = request.getParameter("searchTextBook");
            String paramBookSectionID = request.getParameter("bookSectionID");
            String paramBookGenreID = request.getParameter("bookGenreID");
            String paramBookPageNumber = request.getParameter("pageBookNumber");

            //extraction of attributes from session
            String attrTypeSearchBook = (String) session.getAttribute("typeSearchBook");
            String attrSearchTextBook = (String) session.getAttribute("searchTextBook");
            String attrBookSectionID = (String) session.getAttribute("bookSectionID");
            String attrBookGenreID = (String) session.getAttribute("bookGenreID");
            String attrBookPageNumber = (String) session.getAttribute("pageBookNumber");

            if (paramErrorClear !=null && paramErrorClear.equalsIgnoreCase("true")) {
                session.removeAttribute("currentError");
                session.removeAttribute("autoShowModalForm");
            }

            //Number of the page
            if (paramBookPageNumber != null && !paramBookPageNumber.isEmpty()) {
                if (!RequestParamValidator.checkSymbolsNumbers(paramBookPageNumber)) {
                    paramBookPageNumber = DEFAULT_PAGE;
                }

                session.setAttribute("pageBookNumber", paramBookPageNumber);
            } else {
                if (attrBookPageNumber == null) {
                    session.setAttribute("pageBookNumber", DEFAULT_PAGE);
                }
            }
            paramBookPageNumber = (String) session.getAttribute("pageBookNumber");

            //Type of search
            if (paramTypeSearchBook != null && !paramTypeSearchBook.isEmpty()) {
                if (!paramTypeSearchBook.equals(TYPE_SEARCH_BY_NAME) && !paramTypeSearchBook.equals(TYPE_SEARCH_BY_AUTHOR)) {
                    session.setAttribute("typeSearchBook", TYPE_SEARCH_BY_NAME);
                } else {
                    session.setAttribute("typeSearchBook", paramTypeSearchBook);
                }
            } else {
                if (attrTypeSearchBook == null) {
                    session.setAttribute("typeSearchBook", TYPE_SEARCH_BY_NAME);
                }
            }
            paramTypeSearchBook = (String) session.getAttribute("typeSearchBook");

            //Text of search
            if (paramSearchTextBook != null) {
                session.setAttribute("searchTextBook", paramSearchTextBook);
            } else {
                if (attrSearchTextBook == null) {
                    session.setAttribute("searchTextBook", "");
                }
            }
            paramSearchTextBook = (String) session.getAttribute("searchTextBook");

            //Section of literature
            if (paramBookGenreID != null && !paramBookGenreID.isEmpty()) {
                if (!RequestParamValidator.checkSymbolsNumbers(paramBookGenreID)) {
                    paramBookGenreID = DEFAULT_BOOK_GENRE;
                }

                session.setAttribute("bookGenreID", paramBookGenreID);
            } else {
                if (attrBookGenreID == null) {
                    session.setAttribute("bookGenreID", DEFAULT_BOOK_GENRE);
                }
            }
            paramBookGenreID = (String) session.getAttribute("bookGenreID");

            //Book genre
            if (paramBookSectionID != null && !paramBookSectionID.isEmpty()) {
                if (!RequestParamValidator.checkSymbolsNumbers(paramBookSectionID)) {
                    paramBookSectionID = DEFAULT_BOOK_SECTION;
                }

                session.setAttribute("bookSectionID", paramBookSectionID);
            } else {
                if (attrBookSectionID == null) {
                    session.setAttribute("bookSectionID", DEFAULT_BOOK_SECTION);
                }
            }
            paramBookSectionID = (String) session.getAttribute("bookSectionID");

            //we receive object with criteria of search depending on the transferred inquiry parameters
            DAOSearchBookCriteria bookSearchCriteriaByPager =
                    new DAOSearchBookCriteria(Integer.valueOf(paramTypeSearchBook), paramSearchTextBook,
                            Integer.valueOf(paramBookSectionID), Integer.valueOf(paramBookGenreID),
                            COUNT_RECORD_PAGE, COUNT_PAGE);

            // we receive number of the found books
            int countBooks = DataManager.getCountBooks(bookSearchCriteriaByPager);
            session.setAttribute("countBooks", countBooks);

            // we receive number of pages with the found books
            int countBookPages = COUNT_PAGE;
            if (countBooks > 0) {
                bookSearchCriteriaByPager.setRecordCountPage(RECORD_PAGE);
                countBookPages = DataManager.getCountPagesBooks(bookSearchCriteriaByPager);
            }

            if (countBookPages > 0 && paramBookPageNumber.equals(Integer.toString(COUNT_PAGE))) {
                paramBookPageNumber = DEFAULT_PAGE;
                session.setAttribute("pageBookNumber", paramBookPageNumber);
            }

            session.setAttribute("countBookPages", countBookPages);
            if (Integer.valueOf(paramBookPageNumber) > countBookPages) {
                paramBookPageNumber = Integer.toString(countBookPages);
                session.setAttribute("pageBookNumber", paramBookPageNumber);
            }

            // we receive the list of books for the specified page
            DAOSearchBookCriteria bookSearchCriteriaByListBook =
                    new DAOSearchBookCriteria(Integer.valueOf(paramTypeSearchBook), paramSearchTextBook,
                            Integer.valueOf(paramBookSectionID), Integer.valueOf(paramBookGenreID),
                            RECORD_PAGE, Integer.valueOf(paramBookPageNumber));
            List<Book> listBookPage = DataManager.getListBooks(bookSearchCriteriaByListBook);
            session.setAttribute("listBookPage", listBookPage);

            // obtaining list of sections of literature
            List<BookSection> listBookSection = DataManager.getBookSections();
            session.setAttribute("listBookSection", listBookSection);

            // obtaining list of genres of books
            List<BookGenre> listBookGenre = DataManager.getBookGenres();
            session.setAttribute("listBookGenre", listBookGenre);

            session.setAttribute("currentPage", "index");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ServletException(ex);
        } finally {
        }

        return INDEX_PAGE;
    }
}
