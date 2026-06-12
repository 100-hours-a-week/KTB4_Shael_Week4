package org.example.communityservice.dto.user;

import lombok.Getter;
import org.example.communityservice.dummyObject.User;

import java.util.UUID;

@Getter
public class UserResponseDto {

    private UUID userUuid;
    private String email;
    private String password;
    private String nickname;
    private String profileImage;

    public UserResponseDto(User user){
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }

    //비밀번호 제외한 응답을 위해 생성
    public UserResponseDto(String email, String nickname, String profileImage){
        this.email = email;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public UserResponseDto(UUID userUuid){
        this.userUuid = userUuid;
    }
}
