package com.epam.training.booklibrary.controllers.filters;

import com.epam.training.booklibrary.dao.implementations.DAOUser;
import com.epam.training.booklibrary.enums.Roles;
import com.epam.training.booklibrary.utils.LocaleMessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

/**
 * The filter for check of access to pages according to the rights of the user
 */
@WebFilter(filterName = "AccessPagesFilter",
        urlPatterns = { "/WEB-INF/pages/main.jsp", "/saveUserProfile", "/showCreateOrderForm", "/createUserOrder" },
        dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE },
        initParams = { @WebInitParam(name = "INDEX_PAGE", value = "/index") })
public class AccessPagesFilter implements Filter {
    private Logger logger = LogManager.getLogger(AccessPagesFilter.class.getName());
    private String indexPage;

    /**
     * Method of initialization of a class
     * @param fConfig configuration parameters for initialization of parameters of the filter (type of FilterConfig)
     * @throws ServletException
     */
    public void init(FilterConfig fConfig) throws ServletException {
        indexPage = fConfig.getInitParameter("INDEX_PAGE");
    }

    /**
     * Method of destruction of a class
     */
    public void destroy() {
        indexPage = null;
    }

    /**
     * The method contains check of access to pages according to the rights of the user.
     * If the user isn't authorized, access to the main page of the appendix is forbidden
     * @param req ServletRequest request
     * @param resp ServletResponse response
     * @param chain FilterChain the list of filters for processing of inquiries
     * @throws ServletException
     * @throws IOException
     */
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpSession session = ((HttpServletRequest) req).getSession(false);

        //extraction of localization from session
        Locale locale = (Locale) session.getAttribute("currentLocale");

        //check of the rights (roles) for access to the main page
        DAOUser daoUser = (DAOUser) session.getAttribute("sessionUser");

        if ((daoUser == null) || (!daoUser.isAuthorized()) || (daoUser.getCurrentUser().isBlocked()) ||
            ((daoUser.getUserRoleByName(Roles.EMPLOYEE_LIBRARY) == null) &&
            (daoUser.getUserRoleByName(Roles.READER_LIBRARY) == null))) {

            String fromIP = "Client IP: " + ((HttpServletRequest) req).getRemoteAddr();
            String userName = ((DAOUser) session.getAttribute("sessionUser")).getUserName();
            logger.info(fromIP + "\nThe user of " + userName + " has no rights for access to the main page");

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue("errorAccessToPagesError", locale));
            session.setAttribute("autoShowModalForm", "#formLogin");

            ((HttpServletResponse) resp).sendRedirect(((HttpServletRequest) req).getContextPath() + indexPage);
            return;
        }

        chain.doFilter(req, resp);
    }
}
