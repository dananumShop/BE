package org.dananum.dananum_shop.user.service;

import lombok.RequiredArgsConstructor;
import org.dananum.dananum_shop.user.repository.UserRepository;
import org.dananum.dananum_shop.user.web.dto.custom.CustomUserDetailDto;
import org.dananum.dananum_shop.user.web.dto.custom.TokenUserDto;
import org.dananum.dananum_shop.user.web.entity.user.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = (UserEntity) userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("일치하는 유저 정보가 존재하지 않습니다. email = " + email));

        TokenUserDto userDto = TokenUserDto.toDto(user);

        return new CustomUserDetailDto(userDto);
    }
}
