package org.example.communityservice.dummyObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.communityservice.dto.user.UserCreateRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class User {
    private UUID userUuid;
    private String email;
    private String password;
    private String nickname;
    private String profileImage;
    private List<UUID> likePost = new ArrayList<>();

    public User(UserCreateRequestDto UserCreateRequestDto){
        this.email = UserCreateRequestDto.getEmail();
        this.password = UserCreateRequestDto.getPassword();
        this.nickname = UserCreateRequestDto.getNickname();
        this.profileImage = UserCreateRequestDto.getProfileImage();
    }
}
