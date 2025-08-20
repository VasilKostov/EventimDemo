package controllers;

import interfaces.IUserService;
import singletons.UsersRoles;

public class UserController {

    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    public boolean createUser(String name, String password, UsersRoles role) {
        boolean success = userService.createUser(name, password, role);
        if (success) {
            System.out.println("User created successfully.");
        }
        else{
            System.out.println("User could not be created.");
        }

        return success;
    }

    public boolean createUser(String name, String password) {
        boolean success = userService.createUser(name, password);
        if (success) {
            System.out.println("User created successfully.");
        }
        else{
            System.out.println("User could not be created.");
        }

        return success;
    }

    public void importRoles() {
        userService.importRoles();
    }

    public boolean validateUser(String name, String password) {
        return userService.validateUser(name, password);
    }

    public int getUserId(String username) {
        return userService.getUserIdByUsername(username);
    }

    public UsersRoles getUserRole(String username) {
        return userService.getUserRole(username);
    }
}
