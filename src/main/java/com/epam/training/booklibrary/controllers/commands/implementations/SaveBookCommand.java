package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.dao.implementations.DAOUser;
import com.epam.training.booklibrary.datamodels.DataManager;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.datamodels.entity.Book;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.utils.ApplicationConfigManager;
import com.epam.training.booklibrary.utils.LocaleMessageManager;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * Class for processing of the saveBook team
 */
public class SaveBookCommand implements ICommand {
    private Logger logger = LogManager.getLogger(SaveBookCommand.class.getName());

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

        //we clear the previous attributes of inquiry if they were
        session.removeAttribute("currentError");
        session.removeAttribute("autoShowModalForm");

        //extraction from inquiry of parameters
        int currentYear = (int) session.getAttribute("currentYear");
        String redirectPage = request.getContextPath();

        String actionModeBookForm = "";
        String bookGenreID = "";
        String bookID = "";
        String bookName = "";
        String bookAuthorID = "";
        String bookPublisherID = "";
        String bookPublishYear = "";
        String bookISBN = "";
        String bookNumberCopies = "";
        String bookShortDescription = "";
        byte[] bookImage = null;
        String coverImageIsEmpty = "";

        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (isMultipart) {
                ServletFileUpload uploadServlet = new ServletFileUpload();

                FileItemIterator itemIterator = uploadServlet.getItemIterator(request);

                while (itemIterator.hasNext()) {
                    FileItemStream item = itemIterator.next();

                    if (item.getFieldName().equalsIgnoreCase("actionModeBookForm")) {
                        actionModeBookForm = Streams.asString(item.openStream(), "UTF-8");
                    } else if (item.getFieldName().equalsIgnoreCase("bookGenreID")) {
                        bookGenreID = Streams.asString(item.openStream(), "UTF-8");
                    } else if (item.getFieldName().equalsIgnoreCase("bookID")) {
                        bookID = Streams.asString(item.openStream(), "UTF-8");
                    } else if (item.getFieldName().equalsIgnoreCase("bookName")) {
                        bookName = Streams.asString(item.openStream(), "UTF-8");
                    } else if (item.getFieldName().equalsIgnoreCase("bookAuthorID")) {
                        bookAuthorID = Streams.asString(item.openStream(), "UTF-8");
                    } else if (item.getFieldName().equalsIgnoreCase("bookPublisherID")) {
                        bookPublisherID = Streams.asString(item.openStream(), "UTF-8");
                    } else if (item.getFieldName().equalsIgnoreCase("bookPublishYear")) {
                        bookPublishYear = Streams.asString(item.openStream(), "UTF-8");
                    } else if (item.getFieldName().equalsIgnoreCase("bookISBN")) {
                        bookISBN = Streams.asString(item.openStream(), "UTF-8");
                    } else if (item.getFieldName().equalsIgnoreCase("bookNumberCopies")) {
                        bookNumberCopies = Streams.asString(item.openStream(), "UTF-8");
                    } else if (item.getFieldName().equalsIgnoreCase("bookShortDescription")) {
                        bookShortDescription = Streams.asString(item.openStream(), "UTF-8");
                    } else if (item.getFieldName().equalsIgnoreCase("bookImage")) {
                        bookImage = IOUtils.toByteArray(item.openStream());
                    } else if (item.getFieldName().equalsIgnoreCase("coverImageIsEmpty")) {
                        coverImageIsEmpty = Streams.asString(item.openStream(), "UTF-8");
                    }
                }
            } else {
                actionModeBookForm = request.getParameter("actionModeBookForm");
                bookGenreID = request.getParameter("bookGenreID");
                bookID = request.getParameter("bookID");
                bookName = request.getParameter("bookName");
                bookAuthorID = request.getParameter("bookAuthorID");
                bookPublisherID = request.getParameter("bookPublisherID");
                bookPublishYear = request.getParameter("bookPublishYear");
                bookISBN = request.getParameter("bookISBN");
                bookNumberCopies = request.getParameter("bookNumberCopies");
                bookShortDescription = request.getParameter("bookShortDescription");
                coverImageIsEmpty = request.getParameter("coverImageIsEmpty");
            }

            //check on obligation of filling of fields
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

            //validation of parameters of inquiry
            boolean errorCheckFound = false;
            StringBuilder errorString = new StringBuilder();

            if (!RequestParamValidator.checkSymbolsNumbers(bookGenreID) || !RequestParamValidator.checkSymbolsNumbers(bookID)
                    || !RequestParamValidator.checkSymbolsNumbers(bookAuthorID) || !RequestParamValidator.checkSymbolsNumbers(bookPublisherID)
                    || (!actionModeBookForm.equalsIgnoreCase("addBook") && !actionModeBookForm.equalsIgnoreCase("editBook"))
                    || (!coverImageIsEmpty.equalsIgnoreCase("true") && !coverImageIsEmpty.equalsIgnoreCase("false"))
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

            //if there are mistakes that show them
            if (errorCheckFound) {
                session.setAttribute("currentError", errorString);
                session.setAttribute("autoShowModalForm", "#formBook");

                redirectPage = redirectPage + MAIN_PAGE;
                response.sendRedirect(redirectPage);
                return null;
            }

            //we keep these books
            //we log action of the user
            String fromIP = "Client IP: " + request.getRemoteAddr();
            String userName = ((DAOUser) session.getAttribute("sessionUser")).getUserName();

            Book book;
            if (actionModeBookForm.equalsIgnoreCase("addBook")) {
                book = DataManager.createBook(Integer.valueOf(bookAuthorID), Integer.valueOf(bookGenreID),
                       Integer.valueOf(bookPublisherID), Integer.valueOf(bookPublishYear),
                       bookISBN, bookName, bookShortDescription, Integer.valueOf(bookNumberCopies), bookImage);
                logger.info(fromIP + "\nThe user of " + userName + " added the new book " + book.getName());
            } else {
                book = (Book) session.getAttribute("bookSelected");

                boolean isEmptyCoverImage = Boolean.getBoolean(coverImageIsEmpty);
                if (!isEmptyCoverImage && bookImage.length == 0) {
                    bookImage = book.getCoverImage();
                }

                DataManager.updateBook(Integer.valueOf(bookID), Integer.valueOf(bookAuthorID),
                       Integer.valueOf(bookGenreID), Integer.valueOf(bookPublisherID), Integer.valueOf(bookPublishYear),
                       bookISBN, bookName, bookShortDescription, Integer.valueOf(bookNumberCopies), bookImage);
                logger.info(fromIP + "\nThe user of " + userName + " changed these books " + book.getName());
            }

            //we delete attributes from session
            session.removeAttribute("bookGenreName");
            session.removeAttribute("listAuthors");
            session.removeAttribute("listPublishers");
            session.removeAttribute("bookSelected");
            session.removeAttribute("actionModeBookForm");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
        } catch (MainExceptions.MainErrorException ex) {
            logger.error(LocaleMessageManager.getMessageValue(ex.getMessage(), Locale.US));

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue(ex.getMessage(), locale));
            session.setAttribute("autoShowModalForm", "#formBook");

            redirectPage = redirectPage + MAIN_PAGE;
            response.sendRedirect(redirectPage);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ServletException(ex);
        } finally {
        }

        return null;
    }
}
