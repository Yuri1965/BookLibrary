package com.epam.training.booklibrary.controllers.filters;

import com.epam.training.booklibrary.dao.implementations.DAOUser;
import com.epam.training.booklibrary.dao.interfaces.IDAOUser;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * The basic filter for work with inquiries of the client
 */
@WebFilter(filterName = "PagesBaseFilter", urlPatterns = { "/*" },
  dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE },
  initParams = {
  @WebInitParam(name = "encodingPage", value = "UTF-8", description = "encodingPage Parameter"),
  @WebInitParam(name = "contentTypePage", value = "text/html; charset = UTF-8", description = "contentTypePage Parameter"),
  @WebInitParam(name = "defaultLocale", value = "ru_ru", description = "defaultLocale Parameter")
  })
public class PagesBaseFilter implements Filter {
    private String encodingPage;
    private String contentTypePage;
    private String defaultLocale;

    /**
     * Method of initialization of a class
     * @param fConfig configuration parameters for initialization of parameters of the filter (type of FilterConfig)
     * @throws ServletException
     */
    public void init(FilterConfig fConfig) throws ServletException {
        encodingPage = fConfig.getInitParameter("encodingPage");
        contentTypePage = fConfig.getInitParameter("contentTypePage");
        defaultLocale = fConfig.getInitParameter("defaultLocale");
    }

    /**
     * Method of destruction of a class
     */
    public void destroy() {
        encodingPage = null;
        contentTypePage = null;
        defaultLocale = null;
    }

    /**
     * Method of processing of inquiry of the client filter
     * @param req ServletRequest request
     * @param resp ServletResponse response
     * @param chain FilterChain the list of filters for processing of inquiries
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpSession session = ((HttpServletRequest) req).getSession();

        // installation of the coding of pages
        if (req.getCharacterEncoding() != encodingPage) {
            req.setCharacterEncoding(encodingPage);
        }

        if (resp.getCharacterEncoding() != encodingPage) {
            resp.setCharacterEncoding(encodingPage);
            resp.setContentType(contentTypePage);
        }

        // localization installation
        Locale currentLocale = (Locale) session.getAttribute("currentLocale");

        if (currentLocale == null) {
            currentLocale = new Locale(defaultLocale);
            session.setAttribute("currentLocale", currentLocale);
        }

        // if didn't log in yet, the user = null. We will fill it for session
        // with the rights and the name GUEST (Guest)
        if (session.getAttribute("sessionUser") == null) {
            DAOUser daoUser = new DAOUser("Guest", "Guest");
            session.setAttribute("sessionUser", daoUser);
        }

        chain.doFilter(req, resp);
    }
}
