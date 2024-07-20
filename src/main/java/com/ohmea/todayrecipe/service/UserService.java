package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.dto.user.JoinDTO;
import com.ohmea.todayrecipe.dto.user.UpdateUserDTO;
import com.ohmea.todayrecipe.dto.user.UserResponseDTO;
import com.ohmea.todayrecipe.entity.CookingSkillEnum;
import com.ohmea.todayrecipe.entity.GenderEnum;
import com.ohmea.todayrecipe.entity.UserEntity;
import com.ohmea.todayrecipe.exception.EntityDuplicatedException;
import com.ohmea.todayrecipe.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {

        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public ResponseDTO<String> joinProcess(JoinDTO joinDTO) {

        String username = joinDTO.getUsername();
        String password = joinDTO.getPassword();
        GenderEnum gender = joinDTO.getGender();
        Integer age = joinDTO.getAge();
        Integer cookingBudget = joinDTO.getCookingBudget();
        String filter = joinDTO.getFilter();
        CookingSkillEnum cookingSkill = joinDTO.getCookingSkill();

        Boolean isExist = userRepository.existsByUsername(username);

        if (isExist) {
            throw new EntityDuplicatedException("중복된 아이디가 존재합니다.");
        }

        UserEntity user = UserEntity.builder()
                .username(username)
                .password(bCryptPasswordEncoder.encode(password))
                .gender(gender)
                .cookingSkill(cookingSkill)
                .cookingBudget(cookingBudget)
                .filter(filter)
                .age(age)
                .role("ROLE_ADMIN")
                .build();

        userRepository.save(user);

        return new ResponseDTO<>(HttpStatus.CREATED.value(), "회원가입 성공", null);
    }

    public UserResponseDTO getUserInfo(String username) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        return UserResponseDTO.builder()
                .username(user.getUsername())
                .gender(user.getGender())
                .age(user.getAge())
                .cookingBudget(user.getCookingBudget())
                .cookingSkill(user.getCookingSkill())
                .filter(user.getFilter())
                .build();
    }

    @Transactional
    public UserResponseDTO updateUser(String username, UpdateUserDTO updateUserDTO) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 이름을 가진 사용자를 찾을 수 없습니다: " + username));

        user.updateUser(updateUserDTO.getCookingSkill(), updateUserDTO.getCookingBudget(), updateUserDTO.getFilter());

        userRepository.save(user);

        return UserResponseDTO.builder()
                .username(user.getUsername())
                .gender(user.getGender())
                .age(user.getAge())
                .cookingBudget(user.getCookingBudget())
                .cookingSkill(user.getCookingSkill())
                .filter(user.getFilter())
                .build();
    }
}
