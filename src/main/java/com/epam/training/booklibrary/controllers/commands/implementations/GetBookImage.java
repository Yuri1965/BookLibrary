package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.controllers.utils.DataManager;
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
 * Created by URA on 20.10.2015.
 */
public class GetBookImage implements ICommand {
    private Logger logger = LogManager.getLogger(GetBookImage.class.getName());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        response.setContentType("image/jpeg");
        OutputStream outStream = response.getOutputStream();

        try {
            int bookIndex = Integer.valueOf(request.getParameter("bookIndex"));
            List<Book> lstBookPage = (List<Book>) session.getAttribute("listBookPage");

            Book book = new Book();

            if (lstBookPage != null && lstBookPage.size() > 0) {
                book = lstBookPage.get(bookIndex);
            }

            response.setContentLength(book.getCoverImage().length);
            outStream.write(book.getCoverImage());

        } catch (Exception ex){
            logger.error(ex.getMessage());
        } finally {
            outStream.close();
        }

        return null;
    }
}
