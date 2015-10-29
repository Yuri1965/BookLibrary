package com.epam.training.booklibrary.controllers.commands.abstracts;

import com.epam.training.booklibrary.controllers.commands.implementations.EmptyCommand;
import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * Created by URA on 20.09.2015.
 */
public abstract class AbstractRequestHelper {
    protected HashMap<String, ICommand> commands = new HashMap<String, ICommand>();

    public AbstractRequestHelper() {
        //заполнение таблицы командами
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
        commands.put("showSetWorkOrderForm", null);

        commands.put("showBlockUnBlockUserForm", null);
        commands.put("blockUnBlockUser", null);
    }

    public HashMap<String, ICommand> getCommands() {
        return commands;
    }

    public ICommand getCommand(HttpServletRequest request) {
        //извлечение команды из запроса
        String action = request.getServletPath().substring(1);

        //получение объекта, соответствующего команде
        ICommand command = commands.get(action);

        //если команды не существует в текущем объекте
        if (command == null) {
            command = new EmptyCommand();
        }

        return command;
    }

    public void setCommandByCommandName(String commandName, ICommand command) {
        commands.replace(commandName, command);
    }
}
