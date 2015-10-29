package com.epam.training.booklibrary.controllers.commands.interfaces;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by URA on 20.09.2015.
 */
public interface ICommand {
        public String execute(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException;
}
