package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.controllers.utils.DataManager;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.dao.implementations.DAOSearchBookCriteria;
import com.epam.training.booklibrary.entity.Book;
import com.epam.training.booklibrary.entity.BookGenre;
import com.epam.training.booklibrary.entity.BookSection;
import com.epam.training.booklibrary.utils.ApplicationConfigManager;
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

/**
 * Created by URA on 15.10.2015.
 */
public class IndexCommand implements ICommand {
    private Logger logger = LogManager.getLogger(IndexCommand.class.getName());
    private static final String INDEX_PAGE = "/pages/index.jsp";

    // для подсчета кол-ва записей и страниц
    private static final int COUNT_RECORD_PAGE = 0;
    private static final int COUNT_PAGE = 0;

    // кол-во записей на странице
    private static final int RECORD_PAGE = Integer.valueOf(ApplicationConfigManager.getConfigValue("recordsBookPage", "6"));
    // активная страница по умолчанию
    private static final String DEFAULT_PAGE = "1";

    // вид поиска
    private static final String TYPE_SEARCH_BY_NAME = "1";
    private static final String TYPE_SEARCH_BY_AUTHOR = "2";

    // раздел литературы по умолчанию (все разделы)
    private static final String DEFAULT_BOOK_SECTION = "0";
    // жанр книг по умолчанию (все жанры)
    private static final String DEFAULT_BOOK_GENRE = "0";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        try {
            //извлечение из запроса параметров
            String paramErrorClear = request.getParameter("errorClear");

            String paramTypeSearchBook = request.getParameter("typeSearchBook");
            String paramSearchTextBook = request.getParameter("searchTextBook");
            String paramBookSectionID = request.getParameter("bookSectionID");
            String paramBookGenreID = request.getParameter("bookGenreID");
            String paramBookPageNumber = request.getParameter("pageBookNumber");

            //извлечение атрибутов из сессии
            String attrTypeSearchBook = (String) session.getAttribute("typeSearchBook");
            String attrSearchTextBook = (String) session.getAttribute("searchTextBook");
            String attrBookSectionID = (String) session.getAttribute("bookSectionID");
            String attrBookGenreID = (String) session.getAttribute("bookGenreID");
            String attrBookPageNumber = (String) session.getAttribute("pageBookNumber");

            if (paramErrorClear !=null && paramErrorClear.equalsIgnoreCase("true")) {
                session.removeAttribute("currentError");
                session.removeAttribute("autoShowModalForm");
            }

            //Номер страницы
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

            //Вид поиска
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

            //Текст поиска
            if (paramSearchTextBook != null) {
                session.setAttribute("searchTextBook", paramSearchTextBook);
            } else {
                if (attrSearchTextBook == null) {
                    session.setAttribute("searchTextBook", "");
                }
            }
            paramSearchTextBook = (String) session.getAttribute("searchTextBook");

            //Раздел литературы
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

            //Жанр книги
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

            DAOSearchBookCriteria bookSearchCriteriaByPager =
                    new DAOSearchBookCriteria(Integer.valueOf(paramTypeSearchBook), paramSearchTextBook,
                            Integer.valueOf(paramBookSectionID), Integer.valueOf(paramBookGenreID),
                            COUNT_RECORD_PAGE, COUNT_PAGE);

            // получаем кол-во найденных книг
            int countBooks = DataManager.getCountBooks(bookSearchCriteriaByPager);
            session.setAttribute("countBooks", countBooks);

            // получаем кол-во страниц с найденными книгами
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

            // получаем список книг для указанной страницы
            DAOSearchBookCriteria bookSearchCriteriaByListBook =
                    new DAOSearchBookCriteria(Integer.valueOf(paramTypeSearchBook), paramSearchTextBook,
                            Integer.valueOf(paramBookSectionID), Integer.valueOf(paramBookGenreID),
                            RECORD_PAGE, Integer.valueOf(paramBookPageNumber));
            List<Book> listBookPage = DataManager.getListBooks(bookSearchCriteriaByListBook);
            session.setAttribute("listBookPage", listBookPage);

            // получение списка разделов литературы
            List<BookSection> listBookSection = DataManager.getBookSections();
            session.setAttribute("listBookSection", listBookSection);

            // получение списка жанров книг
            List<BookGenre> listBookGenre = DataManager.getBookGenres();
            session.setAttribute("listBookGenre", listBookGenre);

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        } catch (NamingException ex) {
            logger.error(ex.getMessage());
        } finally {
        }

//        String redirectPage = INDEX_PAGE;
//        response.sendRedirect(redirectPage);

//        return null;
        return INDEX_PAGE;
    }
}
