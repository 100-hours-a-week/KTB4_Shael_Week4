package org.example.communityservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.communityservice.common.Exception.BadRequestException;
import org.example.communityservice.common.Exception.UnauthorizedException;
import org.example.communityservice.common.dto.ErrorInfoDto;
import org.example.communityservice.common.dto.ErrorResponseDto;
import org.example.communityservice.dto.user.*;
import org.example.communityservice.dummyObject.User;
import org.example.communityservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto login(@Valid UserLoginRequestDto userLoginRequestDto){
        User user = userRepository.findByEmail(userLoginRequestDto.getEmail()).orElseThrow(() -> new UnauthorizedException("not_exist"));

        if(!user.getPassword().equals(userLoginRequestDto.getPassword())){
            throw new UnauthorizedException("login_failed");
        }
        return new UserResponseDto(user.getUserUuid());
    }

    public void createUser(@Valid UserCreateRequestDto userCreateRequestDto){
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<>();

        if(userRepository.duplicateEmail(userCreateRequestDto.getEmail())) {
            errorInfoDtoList.add(new ErrorInfoDto("email", "duplicate_email"));
        }
        if(userRepository.duplicateNickname(userCreateRequestDto.getNickname())){
            errorInfoDtoList.add(new ErrorInfoDto("nickname", "duplicate_nickname"));
        }
        if(!errorInfoDtoList.isEmpty()){
            throw new BadRequestException("invalid_request", new ErrorResponseDto(errorInfoDtoList));
        }

        User user = new User(userCreateRequestDto);
        user.setUserUuid(UUID.randomUUID());
        userRepository.save(user);
    }

    public UserResponseDto showInfo(UUID userUuid){
        return new UserResponseDto(userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist")));
    }

    public UserResponseDto updateInfo(UUID userUuid, @Valid UserInfoUpdateRequestDto userInfoUpdateRequestDto){
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<>();

        User existUser = userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));
        if(userInfoUpdateRequestDto.getEmail()==null && userInfoUpdateRequestDto.getNickname()==null && userInfoUpdateRequestDto.getProfileImage()==null) {
            throw new BadRequestException("invalid_request");
        }
        if(userInfoUpdateRequestDto.getEmail()!=null && !existUser.getEmail().equals(userInfoUpdateRequestDto.getEmail()) && userRepository.duplicateEmail(userInfoUpdateRequestDto.getEmail())) {
            errorInfoDtoList.add(new ErrorInfoDto("email", "duplicate_email"));
        }
        if(userInfoUpdateRequestDto.getNickname()!=null && !existUser.getNickname().equals(userInfoUpdateRequestDto.getNickname()) && userRepository.duplicateNickname(userInfoUpdateRequestDto.getNickname())){
            errorInfoDtoList.add(new ErrorInfoDto("nickname", "duplicate_nickname"));
        }
        if(!errorInfoDtoList.isEmpty()){
            throw new BadRequestException("invalid_request", new ErrorResponseDto(errorInfoDtoList));
        }

        if(userInfoUpdateRequestDto.getEmail()!=null){
            existUser.setEmail(userInfoUpdateRequestDto.getEmail());
        }
        if(userInfoUpdateRequestDto.getNickname()!=null){
            existUser.setNickname(userInfoUpdateRequestDto.getNickname());
        }
        if(userInfoUpdateRequestDto.getProfileImage()!=null){
            existUser.setProfileImage(userInfoUpdateRequestDto.getProfileImage());
        }
        return new UserResponseDto(existUser);
    }

    public void updatePassword(UUID userUuid, @Valid UserPasswordUpdateRequestDto userPasswordUpdateRequestDto){
        User user = userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));
        user.setPassword(userPasswordUpdateRequestDto.getPassword());
    }

    public void withdrawal(UUID userUuid){
        User user = userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));
        userRepository.delete(user);
    }
}
