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

    public void init(FilterConfig fConfig) throws ServletException {
        encodingPage = fConfig.getInitParameter("encodingPage");
        contentTypePage = fConfig.getInitParameter("contentTypePage");
        defaultLocale = fConfig.getInitParameter("defaultLocale");
    }

    public void destroy() {
        encodingPage = null;
        contentTypePage = null;
        defaultLocale = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpSession session = ((HttpServletRequest) req).getSession();

        // установка кодировки страниц
        if (req.getCharacterEncoding() != encodingPage) {
            req.setCharacterEncoding(encodingPage);
        }

        if (resp.getCharacterEncoding() != encodingPage) {
            resp.setCharacterEncoding(encodingPage);
            resp.setContentType(contentTypePage);
        }

        // установка локали
        Locale currentLocale = (Locale) session.getAttribute("currentLocale");

        if (currentLocale == null) {
            currentLocale = new Locale(defaultLocale);
            session.setAttribute("currentLocale", currentLocale);
        }

        // если еще не логинились, то пользователь = null. Заполним его для сессии с правами и именем GUEST (Гость)
        if (session.getAttribute("sessionUser") == null) {
            IDAOUser daoUser = new DAOUser("Guest", "Guest");
            session.setAttribute("sessionUser", daoUser);
        }

        chain.doFilter(req, resp);
    }
}
