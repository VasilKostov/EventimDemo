package services;

import interfaces.IUserService;
import repositories.UserRepository;
import singletons.UsersRoles;

public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final String[] DEFAULT_ROLES = {"Client", "Admin"};

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean createUser(String name, String password) {
        return createUser(name, password, UsersRoles.Client);
    }

    @Override
    public boolean createUser(String name, String password, UsersRoles role) {
        if(name.isBlank() || password.isBlank() || role == null) {
            return false;
        }

        int userId = userRepository.insertUser(name, password);
        if(userId < 0){
            return false;
        }

        int roleId = userRepository.getRoleId(role);
        if(roleId < 0){
            return false;
        }

        return userRepository.assignRoleToUser(userId, roleId);
    }

    @Override
    public void importRoles() {
        for (String role : DEFAULT_ROLES) {
            userRepository.importRole(role);
        }
    }

    @Override
    public boolean validateUser(String name, String password) {
        if(name.isBlank() || password.isBlank()) {
            return false;
        }

        return userRepository.validateUser(name, password);
    }

    @Override
    public int getUserIdByUsername(String username) {
        if(username.isBlank()) {
            return -1;
        }
        return userRepository.getUserId(username);
    }

    @Override
    public UsersRoles getUserRole(String username) {
        if(username.isBlank()) {
            return null;
        }
        return userRepository.getUserRole(username);
    }
}
