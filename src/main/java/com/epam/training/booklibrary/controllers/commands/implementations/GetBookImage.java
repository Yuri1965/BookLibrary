package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.entity.Book;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Class for processing of the getBookImage team
 */
public class GetBookImage implements ICommand {
    private Logger logger = LogManager.getLogger(GetBookImage.class.getName());

    private static final int UNKNOWN_BOOK_INDEX = -1;

    /**
     * Method handler of inquiry of the client
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return returns a resource for formation of the answer to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        response.setContentType("image/jpeg");
        OutputStream outStream = response.getOutputStream();

        try {
            //processing of inquiry and preparation of data for the user
            int bookIndex = Integer.valueOf(request.getParameter("bookIndex"));

            Book book = new Book();

            if (bookIndex == UNKNOWN_BOOK_INDEX) {
                book = (Book) session.getAttribute("bookSelected");
            } else {
                List<Book> lstBookPage = (List<Book>) session.getAttribute("listBookPage");

                if (lstBookPage != null && lstBookPage.size() > 0) {
                    book = lstBookPage.get(bookIndex);
                }
            }

            response.setContentLength(book.getCoverImage().length);
            outStream.write(book.getCoverImage());

        } catch (Exception ex){
            logger.error(ex.getMessage());
            throw new ServletException(ex);
        } finally {
            outStream.close();
        }

        return null;
    }
}
