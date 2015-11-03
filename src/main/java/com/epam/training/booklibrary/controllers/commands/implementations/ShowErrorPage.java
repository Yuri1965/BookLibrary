package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.utils.LocaleMessageManager;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * Class for processing of the showErrorPage team
 */
public class ShowErrorPage implements ICommand {
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
        String redirectPage = request.getContextPath() + "/" + session.getAttribute("currentPage");
        Locale locale = (Locale) session.getAttribute("currentLocale");

        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
        if (servletName == null) {
            servletName = "Unknown";
        }
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        if (requestUri == null) {
            requestUri = "Unknown";
        }

        // Set response content type
        response.setContentType("text/html; charset=UTF-8");

        PrintWriter outWriter = response.getWriter();
        outWriter.write("<!DOCTYPE html>" +
                "<head>" +
                "<title>" + LocaleMessageManager.getMessageValue("welcome_title", locale) + "</title>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bootstrap.min.css\"/>" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/bootstrap-theme.min.css\"/>" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/font-awesome.min.css\"/>" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/ustyle.css\"/>" +
                "<link rel=\"shortcut icon\" href=\"images/favicon.ico\" type=\"image/x-icon\">" +
                "</head>" +
                "<body>");

        outWriter.write("<br>");
        outWriter.write("<a href=\"" + redirectPage + "\">&nbsp;&nbsp;&nbsp;To return on the previous page</a>");
        outWriter.write("<br>");

        if (statusCode != 500){
            outWriter.write("<h3>&nbsp;&nbsp;&nbsp;Error Details</h3>");
            outWriter.write("<strong>&nbsp;&nbsp;&nbspStatus Code: </strong>" + statusCode + "<br>");
            if (statusCode == 404) {
                outWriter.write("<strong>&nbsp;&nbsp;&nbspRequested URI: </strong>" + requestUri + " is not found");
            } else {
                outWriter.write("<strong>&nbsp;&nbsp;&nbspRequested URI: </strong>" + requestUri);
            }
        } else {
            outWriter.write("<h3>&nbsp;&nbsp;&nbsp;Exception Details</h3>");
            outWriter.write("<ul>");

            outWriter.write("<strong>&nbsp;&nbsp;&nbspServlet Name: </strong>" + servletName + "<br>");
            outWriter.write("<strong>&nbsp;&nbsp;&nbspException Name: </strong>" + throwable.getClass().getName() + "<br>");
            outWriter.write("<strong>&nbsp;&nbsp;&nbspRequested URI: </strong>" + requestUri + "<br>");
            outWriter.write("<strong>&nbsp;&nbsp;&nbspException Message: </strong>" + throwable.getMessage()+"<br>");
            outWriter.write("<strong>&nbsp;&nbsp;&nbspStack trace:</strong><br>");

            for (StackTraceElement traceElement : throwable.getStackTrace()) {
                outWriter.write("<li>&nbsp;&nbsp;&nbsp;" + traceElement.toString() + "</li>");
            }

            outWriter.write("</ul>");
        }

        outWriter.write("</body></html>");

        return null;
    }
}
