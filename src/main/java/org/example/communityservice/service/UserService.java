package org.example.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.example.communityservice.common.CustomException;
import org.example.communityservice.common.dto.ErrorInfoDto;
import org.example.communityservice.common.dto.ErrorResponseDto;
import org.example.communityservice.dto.user.UserResponseDto;
import org.example.communityservice.dto.user.UserRequestDto;
import org.example.communityservice.dummyObject.User;
import org.example.communityservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserResponseDto login(UserRequestDto userRequestDto){
        if(userRequestDto.getEmail()==null || userRequestDto.getPassword()==null) {
            throw new CustomException(HttpStatus.BAD_REQUEST,"invalid_request");
        }
        User user = userRepository.findByEmail(userRequestDto.getEmail()).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));
        if(!user.getPassword().equals(userRequestDto.getPassword())){
            throw new CustomException(HttpStatus.UNAUTHORIZED, "login_failed");
        }
        return new UserResponseDto(user.getUserUuid());
    }

    public void createUser(UserRequestDto userRequestDto){
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<>();
        if(userRequestDto.getEmail()==null || userRequestDto.getPassword()==null || userRequestDto.getNickname()==null || userRequestDto.getProfileImage()==null) {
            throw new CustomException(HttpStatus.BAD_REQUEST,"invalid_request", new ErrorResponseDto(List.of(new ErrorInfoDto(null, "invalid_request"))));
        }
        if(userRepository.duplicateEmail(userRequestDto.getEmail())) {
            errorInfoDtoList.add(new ErrorInfoDto("email", "duplicate_email"));
        }
        if(userRepository.duplicateNickname(userRequestDto.getNickname())){
            errorInfoDtoList.add(new ErrorInfoDto("nickname", "duplicate_nickname"));
        }
        if(!errorInfoDtoList.isEmpty()){
            throw new CustomException(HttpStatus.BAD_REQUEST, "invalid_request", new ErrorResponseDto(errorInfoDtoList));
        }

        User user = new User(userRequestDto);
        user.setUserUuid(UUID.randomUUID());
        userRepository.save(user);
    }

    public UserResponseDto showInfo(UUID userUuid){
        return new UserResponseDto(userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist")));
    }

    public UserResponseDto updateInfo(UUID userUuid, UserRequestDto userRequestDto){
        List<ErrorInfoDto> errorInfoDtoList = new ArrayList<>();
        if(userRequestDto.getEmail()==null && userRequestDto.getNickname()==null && userRequestDto.getProfileImage()==null) {
            throw new CustomException(HttpStatus.BAD_REQUEST,"invalid_request", new ErrorResponseDto(List.of(new ErrorInfoDto(null, "invalid_request"))));
        }
        if(userRepository.duplicateEmail(userRequestDto.getEmail())) {
            errorInfoDtoList.add(new ErrorInfoDto("email", "duplicate_email"));
        }
        if(userRepository.duplicateNickname(userRequestDto.getNickname())){
            errorInfoDtoList.add(new ErrorInfoDto("nickname", "duplicate_nickname"));
        }
        if(!errorInfoDtoList.isEmpty()){
            throw new CustomException(HttpStatus.BAD_REQUEST, "invalid_request", new ErrorResponseDto(errorInfoDtoList));
        }
        User existUser = userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));

        if(userRequestDto.getEmail()!=null){
            existUser.setEmail(userRequestDto.getEmail());
        }
        if(userRequestDto.getNickname()!=null){
            existUser.setNickname(userRequestDto.getNickname());
        }
        if(userRequestDto.getProfileImage()!=null){
            existUser.setProfileImage(userRequestDto.getProfileImage());
        }
        return new UserResponseDto(existUser.getEmail(), existUser.getNickname(), existUser.getProfileImage());
    }

    public void updatePassword(UUID userUuid, String password){
        if(password == null){
            throw new CustomException(HttpStatus.BAD_REQUEST,"invalid_request");
        }
        User user = userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));
        user.setPassword(password);
    }

    public void withdrawal(UUID userUuid){
        User user = userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));
        userRepository.delete(user);
    }
}
