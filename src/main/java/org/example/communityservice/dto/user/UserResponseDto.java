package org.example.communityservice.dto.user;

import lombok.Getter;
import org.example.communityservice.dummyObject.User;

import java.util.UUID;

@Getter
public class UserResponseDto {

    private UUID userUuid;
    private String email;
    private String nickname;
    private String profileImage;

    public UserResponseDto(User user){
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }

    public UserResponseDto(UUID userUuid){
        this.userUuid = userUuid;
    }
}
