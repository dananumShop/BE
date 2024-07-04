package org.dananum.dananum_shop.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.util.UserValidation;
import org.dananum.dananum_shop.user.web.dto.getUser.GetUserVerificationDto;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserValidation userValidation;

    public GetUserVerificationDto getUserVerification(User user) {
        UserEntity loggedInUser = userValidation.validateExistUser(user.getUsername());

        return GetUserVerificationDto.from(loggedInUser);
    }

    public void updateAccountCancellation(User user) {
        UserEntity loggedInUser = userValidation.validateExistUser(user.getUsername());

        loggedInUser.updateAccountStatus(loggedInUser.getAccountStatus());
        userRepository.save(loggedInUser);
    }
}
