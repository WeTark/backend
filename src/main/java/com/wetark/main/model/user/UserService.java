package com.wetark.main.model.user;

import com.wetark.main.helper.twoFactor.TwoFactorHelper;
import com.wetark.main.helper.twoFactor.payload.TwoFactorSendOtpResponse;
import com.wetark.main.payload.response.user.UserLoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    TwoFactorHelper twoFactorHelper;

    @Autowired
    UserRepository userRepository;

    public UserLoginResponse signInByPhoneNo(String phoneNo) {
        Optional<User> optionalUser = userRepository.findByPhoneNo(phoneNo);
        return optionalUser.map(user -> {
            if(user.getPassword() == null){
                return sendOtp(phoneNo);
            }
            return UserLoginResponse.builder().isUserExist(true).build();
        }).orElseGet(()->{
            User user = new User(phoneNo);
            userRepository.save(user);
            return sendOtp(phoneNo);
        });
    }

    private UserLoginResponse sendOtp(String phoneNo){
        UserLoginResponse userLoginResponse = UserLoginResponse.builder()
                .isUserExist(false)
                .build();
        TwoFactorSendOtpResponse twoFactorSendOtpResponse = twoFactorHelper.sendOTP(phoneNo);
        userLoginResponse.setSessionId(twoFactorSendOtpResponse.getDetails());
        userLoginResponse.setMessage(twoFactorSendOtpResponse.getStatus());
        return userLoginResponse;
    }

    private void verifyOtp(){

    }
}
