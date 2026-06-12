package org.example.communityservice.repository;

import org.example.communityservice.dummyObject.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {
    private final List<User> userList;

    public UserRepository(ObjectMapper objectMapper) {
        try (InputStream inputStream = new ClassPathResource("dummy/user.json").getInputStream()) {
            this.userList = new ArrayList<>(
                    objectMapper.readValue(inputStream, new TypeReference<List<User>>() {})
            );
        } catch (Exception e) {
            throw new IllegalStateException("post.json 읽기 실패", e);
        }
    }

    public boolean duplicateEmail(String email){
        boolean result = false;
        for (User findUser : userList) {
            if (findUser.getEmail().equals(email)) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean duplicateNickname(String nickname){
        boolean result = false;
        for (User findUser : userList) {
            if (findUser.getNickname().equals(nickname)) {
                result = true;
                break;
            }
        }
        return result;
    }

    //user가 존재하지 않을 수 있기에
    public Optional<User> findByEmail(String email){
        User user = null;
        for (User findUser : userList) {
            if (findUser.getEmail().equals(email)) {
                user = findUser;
                break;
            }
        }
        return Optional.ofNullable(user);
    }

    public void save(User user){
        userList.add(user);
    }

    public void delete(User user) {
        userList.remove(user);
    }

    //user가 존재하지 않을 수 있기에
    public Optional<User> findByUuid(UUID userUuid){
        User user = null;
        for(User findUser : userList){
            if(findUser.getUserUuid().equals(userUuid)){
                user = findUser;
                break;
            }
        }
        return Optional.ofNullable(user);
    }
    public void deleteLikePost(User user, UUID postUuid){
        user.getLikePost().remove(postUuid);
    }
}
