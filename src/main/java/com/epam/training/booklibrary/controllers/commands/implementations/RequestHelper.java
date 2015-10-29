package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.abstracts.AbstractRequestHelper;

/**
 * Created by URA on 20.09.2015.
 */
public class RequestHelper extends AbstractRequestHelper {
    private static RequestHelper instance;

    private RequestHelper() {
        super();

        //заполнение списка командами
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
        commands.replace("deleteBook", new DeleteBook());

        commands.replace("showCreateOrderForm", new ShowCreateOrderForm());
        commands.replace("createUserOrder", new CreateUserOrder());
        commands.replace("showCloseOrderForm", new ShowCloseOrderForm());
        commands.replace("showSetWorkOrderForm", new ShowSetWorkOrderForm());

        commands.replace("showBlockUnBlockUserForm", new ShowBlockUnBlockUserForm());
        commands.replace("blockUnBlockUser", new BlockUnBlockUser());
    }

    //создание единственного объекта по шаблону Singleton
    public static synchronized RequestHelper getInstance() {
        if (instance == null) {
            instance = new RequestHelper();
        }

        return instance;
    }
}
