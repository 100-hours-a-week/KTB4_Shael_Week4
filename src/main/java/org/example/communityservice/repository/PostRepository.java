package org.example.communityservice.repository;

import org.example.communityservice.dummyObject.Post;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PostRepository {
    private final List<Post> postList;

    public PostRepository(ObjectMapper objectMapper) {
        try (InputStream inputStream = new ClassPathResource("dummy/post.json").getInputStream()) {
            this.postList = new ArrayList<>(
                    objectMapper.readValue(inputStream, new TypeReference<List<Post>>() {})
            );
        } catch (Exception e) {
            throw new IllegalStateException("post.json 읽기 실패", e);
        }
    }

    public List<Post> showAllPost(){
        return postList;
    }

    public void save(Post post){
        postList.add(post);
    }

    public Optional<Post> findByUuid(UUID postUuid){
        Post post = null;
        for(Post findPost : postList){
            if(findPost.getPostUuid().equals(postUuid)){
                post = findPost;
                break;
            }
        }
        return Optional.ofNullable(post);
    }

    public void delete(Post post){
        postList.remove(post);
    }
}
