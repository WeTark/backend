package com.wetark.main.helper;

import com.wetark.main.helper.rocketChat.RocketChatHelper;
import com.wetark.main.model.user.User;
import com.wetark.main.model.user.UserRepository;
import com.wetark.main.model.user.balance.Balance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {
    private UserRepository userRepository;
    private RocketChatHelper rocketChatHelper = new RocketChatHelper("https://chat.wetark.in");
    @Autowired
    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void run(ApplicationArguments args) {
        List<User> userList = userRepository.findAll();
        userList.forEach(user -> {
            if(user.getBalance() == null){
                user.setBalance(new Balance());
            }
//            if(user.getRocketChatToken() == null){
//                user.setRocketChatToken(
//                        rocketChatHelper.CreateUser(new RCRegisterUser(
//                                user.getUsername(),
//                                user.getEmail(),
//                                user.getEmail(),
//                                user.getFirstName()+" "+user.getLastName()))
//                );
//            }
            userRepository.save(user);
        });
    }
}
