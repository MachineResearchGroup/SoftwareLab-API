package com.swl.populator.user;

import com.swl.models.user.User;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class PopulatorUser {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;


    public User create() {

        return User.builder()
                .name(FakerUtil.getInstance().faker.name().fullName())
                .email(getEmail())
                .username(getUsername())
                .password(getPassword(FakerUtil.getInstance().faker.internet().password()))
                .phones(new ArrayList<>(Arrays.asList(FakerUtil.getInstance().faker.phoneNumber().cellPhone(),
                        FakerUtil.getInstance().faker.phoneNumber().cellPhone())))
                .build();
    }


    private String getEmail() {
        String email = FakerUtil.getInstance().fakeValuesService.bothify("????##@gmail.com");
        while (userRepository.findByEmail(email).isPresent()) {
            email = FakerUtil.getInstance().fakeValuesService.bothify("????##@gmail.com");
        }
        return email;
    }


    private String getUsername() {
        String username = FakerUtil.getInstance().faker.name().username();
        while (userRepository.findByUsername(username).isPresent()) {
            username = FakerUtil.getInstance().faker.name().username();
        }
        return username;
    }


    private String getPassword(String password) {
        return encoder.encode(password);
    }


    public User save() {
        return userRepository.save(this.create());
    }

}
