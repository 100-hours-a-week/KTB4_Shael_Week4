package org.example.communityservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.communityservice.common.dto.CommonResponseDto;
import org.example.communityservice.dto.user.UserResponseDto;
import org.example.communityservice.dto.user.UserRequestDto;
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
    public ResponseEntity<CommonResponseDto<UserResponseDto>> login(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResponseDto = userService.login(userRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("login_success", userResponseDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<Void>> createUser(@RequestBody UserRequestDto userRequestDto){
        userService.createUser(userRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseDto<>("register_success", null));
    }

    @GetMapping("/user/{userUuid}/info")
    public ResponseEntity<CommonResponseDto<UserResponseDto>> showInfo(@PathVariable UUID userUuid){
        UserResponseDto userResponseDto = userService.showInfo(userUuid);

        return ResponseEntity.ok(new CommonResponseDto<>("fetch_success", userResponseDto));
    }

    @PatchMapping("/user/{userUuid}/info")
    public ResponseEntity<CommonResponseDto<UserResponseDto>> updateInfo(@PathVariable UUID userUuid, @RequestBody UserRequestDto userRequestDto){
        UserResponseDto userResponseDto = userService.updateInfo(userUuid, userRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("update_success", userResponseDto));
    }

    @PatchMapping("/user/{userUuid}/password")
    public ResponseEntity<CommonResponseDto<Void>> updatePassword(@PathVariable UUID userUuid, @RequestBody String password){
        userService.updatePassword(userUuid, password);

        return ResponseEntity.ok(new CommonResponseDto<>("update_success", null));
    }

    @DeleteMapping("/{userUuid}/withdrawal")
    public ResponseEntity<CommonResponseDto<Void>> withdrawal(@PathVariable UUID userUuid){
        userService.withdrawal(userUuid);

        return ResponseEntity.ok(new CommonResponseDto<>("delete_success", null));
    }
}
