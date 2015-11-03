package com.epam.training.booklibrary.controllers.commands.abstracts;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Abstract class which comprises the list of teams for work with the application
 */
public abstract class AbstractActionCommandFabric {
    //the list of teams for work with the application
    protected HashMap<String, ICommand> commands = new HashMap<String, ICommand>();

    /**
     * Constructor of a class
     */
    public AbstractActionCommandFabric() {
        //filling of the list with teams
        commands.put("localeChange", null);

        commands.put("login", null);
        commands.put("logout", null);
        commands.put("registrationUser", null);
        commands.put("saveUserProfile", null);

        commands.put("main", null);
        commands.put("index", null);

        commands.put("getBookImage", null);
        commands.put("showEditBookForm", null);
        commands.put("showAddBookForm", null);
        commands.put("saveBook", null);
        commands.put("showDeleteBookForm", null);
        commands.put("deleteBook", null);

        commands.put("showCreateOrderForm", null);
        commands.put("createUserOrder", null);
        commands.put("showCloseOrderForm", null);
        commands.put("closeUserOrder", null);
        commands.put("showSetWorkOrderForm", null);
        commands.put("setWorkUserOrder", null);

        commands.put("showBlockUnBlockUserForm", null);
        commands.put("blockUnBlockUser", null);

        commands.put("showErrorPage", null);
    }

    /**
     * The method returns the list of teams for work with the application
     * @return The list of teams for work with the application (type of HashMap<String, ICommand>)
     */
    public HashMap<String, ICommand> getCommands() {
        return commands;
    }

    /**
     * The method returns team for processing by the application controller which was transferred with inquiry
     * @param request (type of HttpServletRequest)
     * @return team for processing (type of ICommand)
     */
    public ICommand getCommand(HttpServletRequest request) {
        //extraction of team from inquiry
        String action = request.getServletPath().substring(1);

        //receiving the object corresponding to team
        ICommand command = commands.get(action);

        return command;
    }

    /**
     * The method allows to establish realization of team for an element from the list of
     * teams of the appendix on her name in the list
     * @param commandName the key identifier of team in the list
     * @param command object - team (type of ICommand) which contains realization of its processing in the application
     */
    public void setCommandByCommandName(String commandName, ICommand command) {
        commands.replace(commandName, command);
    }
}
