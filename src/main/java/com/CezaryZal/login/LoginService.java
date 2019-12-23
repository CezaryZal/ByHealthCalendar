package com.CezaryZal.login;

import com.CezaryZal.api.user.User;
import com.CezaryZal.api.user.UserService;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private UserService userService;

    public LoginService(UserService userService) {
        this.userService = userService;
    }

    public UserBasic getUserBasicFromUser(String loginName){
        return convertToUserBasic(getUserByLoginName(loginName));
    }


    private User getUserByLoginName(String loginName){
        return userService.getUserByLoginName(loginName);
    }

    private UserBasic convertToUserBasic(User userFromDb){
        return new UserBasic(userFromDb.getId(), userFromDb.getNick(),userFromDb.getLoginName());
    }
}
