package com.epam.training.booklibrary.controllers.filters;

import com.epam.training.booklibrary.dao.implementations.DAOUser;
import com.epam.training.booklibrary.dao.interfaces.IDAOUser;
import com.epam.training.booklibrary.enums.Roles;
import com.epam.training.booklibrary.utils.LocaleMessageManager;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by URA on 04.10.2015.
 */
@WebFilter(filterName = "AccessPagesFilter",
        urlPatterns = { "/pages/main.jsp" },
        dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE },
        initParams = { @WebInitParam(name = "INDEX_PAGE", value = "/index") })
public class AccessPagesFilter implements Filter {

    private String indexPage;

    public void init(FilterConfig fConfig) throws ServletException {
        indexPage = fConfig.getInitParameter("INDEX_PAGE");
    }

    public void destroy() {
        indexPage = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpSession session = ((HttpServletRequest) req).getSession(false);

        //извлечение локали из сессии
        Locale locale = (Locale) session.getAttribute("currentLocale");

        // проверка прав(ролей) на доступ к основной странице
        DAOUser daoUser = (DAOUser) session.getAttribute("sessionUser");

        if ((daoUser == null) || (!daoUser.isAuthorized()) || (daoUser.getCurrentUser().isBlocked()) ||
            ((daoUser.getUserRoleByName(Roles.EMPLOYEE_LIBRARY) == null) &&
            (daoUser.getUserRoleByName(Roles.READER_LIBRARY) == null))) {

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue("errorAccessToPagesError", locale));
            session.setAttribute("autoShowModalForm", "#formLogin");

            ((HttpServletResponse) resp).sendRedirect(((HttpServletRequest) req).getContextPath() + indexPage);
        }

        chain.doFilter(req, resp);
    }
}
