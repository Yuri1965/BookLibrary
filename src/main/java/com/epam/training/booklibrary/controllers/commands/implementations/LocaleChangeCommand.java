package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by URA on 20.09.2015.
 */
public class LocaleChangeCommand implements ICommand {
    private static final String DEFAULT_LOCALE = "ru_ru";
    private static final String EN_US_LOCALE = "en_us";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        //извлечение из запроса параметров языка интерфейса (Locale)
        String locale = request.getParameter("locale");
        String redirectPage = request.getContextPath() + "/" + request.getParameter("currentPage");

        // очищаем предыдущие атрибуты запроса если они были
        session.removeAttribute("currentError");
        session.removeAttribute("autoShowModalForm");

        //проверка наличия параметров
        if (locale == null ||
           (!locale.toLowerCase().equals(DEFAULT_LOCALE) && !locale.toLowerCase().equals(EN_US_LOCALE))) {
            locale = DEFAULT_LOCALE;
        }

        Locale currentLocale = new Locale(locale);
        session.setAttribute("currentLocale", currentLocale);

        response.sendRedirect(redirectPage);
        return null;
    }
}
