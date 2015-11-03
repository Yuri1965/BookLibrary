package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.abstracts.AbstractActionCommandFabric;

/**
 * Class factory which contains the list of teams for processing by the application controller
 */
public class ActionCommandFabric extends AbstractActionCommandFabric {
    private static ActionCommandFabric instance;

    /**
     * private Constructor of class
     */
    private ActionCommandFabric() {
        super();

        //filling of the list with teams
        commands.replace("localeChange", new LocaleChangeCommand());

        commands.replace("login", new LoginCommand());
        commands.replace("logout", new LogoutCommand());
        commands.replace("registrationUser", new RegistrationUserCommand());
        commands.replace("saveUserProfile", new SaveUserProfileCommand());

        commands.replace("main", new MainCommand());
        commands.replace("index", new IndexCommand());

        commands.replace("getBookImage", new GetBookImage());
        commands.replace("showEditBookForm", new ShowEditBookForm());
        commands.replace("showAddBookForm", new ShowAddBookForm());
        commands.replace("saveBook", new SaveBookCommand());
        commands.replace("showDeleteBookForm", new ShowDeleteBookForm());
        commands.replace("deleteBook", new DeleteBookCommand());

        commands.replace("showCreateOrderForm", new ShowCreateOrderForm());
        commands.replace("createUserOrder", new CreateUserOrderCommand());
        commands.replace("showCloseOrderForm", new ShowCloseOrderForm());
        commands.replace("closeUserOrder", new CloseUserOrderCommand());
        commands.replace("showSetWorkOrderForm", new ShowSetWorkOrderForm());
        commands.replace("setWorkUserOrder", new SetWorkUserOrderCommand());

        commands.replace("showBlockUnBlockUserForm", new ShowBlockUnBlockUserForm());
        commands.replace("blockUnBlockUser", new BlockUnBlockUserCommand());

        commands.replace("showErrorPage", new ShowErrorPage());
    }

    /**
     * The method creates the single copy of the class ActionCommandFabric on the Singleton template and returns it
     * @return (type of ActionCommandFabric)
     */
    public static synchronized ActionCommandFabric getInstance() {
        if (instance == null) {
            instance = new ActionCommandFabric();
        }

        return instance;
    }
}
