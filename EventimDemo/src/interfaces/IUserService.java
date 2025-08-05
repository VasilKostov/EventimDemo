package interfaces;

import Singletons.UsersRoles;

public interface IUserService {
    boolean createUser(String name, String password);
    boolean createUser(String name, String password, UsersRoles role);
    void importRoles();
    boolean validateUser(String name, String password);
    int getUserIdByUsername(String username);
}
