package org.example.communityservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.communityservice.common.dto.CommonResponseDto;
import org.example.communityservice.dto.user.*;
import org.example.communityservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<UserResponseDto>> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        UserResponseDto userResponseDto = userService.login(userLoginRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("login_success", userResponseDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<Void>> createUser(@Valid @RequestBody UserCreateRequestDto UserCreateRequestDto){
        userService.createUser(UserCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseDto<>("register_success", null));
    }

    @GetMapping("/user/{userUuid}/info")
    public ResponseEntity<CommonResponseDto<UserResponseDto>> showInfo(@PathVariable UUID userUuid){
        UserResponseDto userResponseDto = userService.showInfo(userUuid);

        return ResponseEntity.ok(new CommonResponseDto<>("fetch_success", userResponseDto));
    }

    @PatchMapping("/user/{userUuid}/info")
    public ResponseEntity<CommonResponseDto<UserResponseDto>> updateInfo(@PathVariable UUID userUuid, @Valid @RequestBody UserInfoUpdateRequestDto userInfoUpdateRequestDto){
        UserResponseDto userResponseDto = userService.updateInfo(userUuid, userInfoUpdateRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("update_success", userResponseDto));
    }

    @PatchMapping("/user/{userUuid}/password")
    public ResponseEntity<CommonResponseDto<Void>> updatePassword(@PathVariable UUID userUuid, @Valid @RequestBody UserPasswordUpdateRequestDto userPasswordUpdateRequestDto){
        userService.updatePassword(userUuid, userPasswordUpdateRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("update_success", null));
    }

    @DeleteMapping("/{userUuid}/withdrawal")
    public ResponseEntity<CommonResponseDto<Void>> withdrawal(@PathVariable UUID userUuid){
        userService.withdrawal(userUuid);

        return ResponseEntity.ok(new CommonResponseDto<>("delete_success", null));
    }
}
