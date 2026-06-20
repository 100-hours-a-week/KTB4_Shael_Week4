package org.example.communityservice.dummyObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.communityservice.dto.user.UserRequestDto;

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

    public User(UserRequestDto userRequestDto){
        this.email = userRequestDto.getEmail();
        this.password = userRequestDto.getPassword();
        this.nickname = userRequestDto.getNickname();
        this.profileImage = userRequestDto.getProfileImage();
    }
}
