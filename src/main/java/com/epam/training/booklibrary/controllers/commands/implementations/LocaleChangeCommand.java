package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * Class for processing of the localeChange team
 */
public class LocaleChangeCommand implements ICommand {
    private static final String DEFAULT_LOCALE = "ru_ru";
    private static final String EN_US_LOCALE = "en_us";

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

        //extraction from inquiry of parameters of language of the interface (Locale)
        String locale = request.getParameter("locale");
        String redirectPage = request.getContextPath() + "/" + request.getParameter("currentPage");

        //we clear the previous attributes of inquiry if they were
        session.removeAttribute("currentError");
        session.removeAttribute("autoShowModalForm");

        //verification of parameters
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
