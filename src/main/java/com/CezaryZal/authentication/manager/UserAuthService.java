package com.CezaryZal.authentication.manager;

import com.CezaryZal.authentication.entity.UserAuthentication;
import com.CezaryZal.authentication.repository.UserAuthRepository;
import com.CezaryZal.exceptions.not.found.UserNotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Service
public class UserAuthService {

    private UserAuthRepository userAuthR;

    @Autowired
    public UserAuthService(UserAuthRepository userAuthR) {
        this.userAuthR = userAuthR;
    }

    public UserAuthentication findByLoginName(String loginName) {
        return userAuthR.findByLoginName(loginName)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }
}
