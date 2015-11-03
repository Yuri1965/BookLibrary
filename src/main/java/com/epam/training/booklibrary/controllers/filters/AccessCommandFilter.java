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
 * The filter for check of the rights for performance of teams by the user depending on his role
 */
@WebFilter(filterName = "AccessCommandFilter",
        urlPatterns = { "/deleteBook", "/showEditBookForm", "/showAddBookForm", "/saveBook",
                        "/showDeleteBookForm", "/showCloseOrderForm", "/closeUserOrder",
                        "/showSetWorkOrderForm", "/setWorkUserOrder", "/showBlockUnBlockUserForm",
                        "/blockUnBlockUser" },
        dispatcherTypes = { DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE },
        initParams = { @WebInitParam(name = "INDEX_PAGE", value = "/index") })
public class AccessCommandFilter implements Filter {
    private Logger logger = LogManager.getLogger(AccessCommandFilter.class.getName());
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
     * Method of check of the rights for performance of teams by the user depending on his role.
     * If the user not the employee of library, access to performance of teams is forbidden.
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

        //check of the rights (roles) for access to performance of teams
        DAOUser daoUser = (DAOUser) session.getAttribute("sessionUser");

        if ((daoUser == null) || (!daoUser.isAuthorized()) || (daoUser.getCurrentUser().isBlocked()) ||
                daoUser.getUserRoleByName(Roles.EMPLOYEE_LIBRARY) == null) {

            String fromIP = "Client IP: " + ((HttpServletRequest) req).getRemoteAddr();
            String userName = ((DAOUser) session.getAttribute("sessionUser")).getUserName();
            logger.info(fromIP + "\nThe user of " + userName + " has no rights for command " +
                    ((HttpServletRequest) req).getServletPath() + " execution");

            daoUser = new DAOUser("Guest", "Guest");
            session.setAttribute("sessionUser", daoUser);

            session.setAttribute("currentError", LocaleMessageManager.getMessageValue("errorAccessToPagesError", locale));
            session.setAttribute("autoShowModalForm", "#formLogin");

            ((HttpServletResponse) resp).sendRedirect(((HttpServletRequest) req).getContextPath() + indexPage);
            return;
        }

        chain.doFilter(req, resp);
    }
}
