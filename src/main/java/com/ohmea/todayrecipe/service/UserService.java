package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.user.RegisterDTO;
import com.ohmea.todayrecipe.entity.UserEntity;
import com.ohmea.todayrecipe.exception.EntityDuplicatedException;
import com.ohmea.todayrecipe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void register(RegisterDTO registerDTO) {
        String username = registerDTO.getUsername();
        // 비밀번호 암호화
        String encryptedPassword = bCryptPasswordEncoder.encode(registerDTO.getPassword());

        // username이 존재하는지 확인
        Boolean isExist = userRepository.existsByUsername(username);

        // username이 존재한다면
        if (isExist) {
            throw new EntityDuplicatedException("동일한 아이디가 존재합니다.");
        }

        UserEntity userEntity = new UserEntity(username, encryptedPassword, "ROLE_ADMIN");

        userRepository.save(userEntity);
    }

}
