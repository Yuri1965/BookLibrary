package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by URA on 20.09.2015.
 */
public class EmptyCommand implements ICommand {
    private static final String INDEX_PAGE = "/index";

    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        /*в случае неизвестной команды возврат на главную страницу*/

        HttpSession session = request.getSession(false);

        // очищаем предыдущие атрибуты запроса если они были
        session.removeAttribute("currentError");
        session.removeAttribute("autoShowModalForm");
//        request.removeAttribute("currentError");
//        request.removeAttribute("autoShowModalForm");

        return INDEX_PAGE;
    }
}
