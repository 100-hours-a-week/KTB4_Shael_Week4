package org.example.communityservice.repository;

import org.example.communityservice.dummyObject.PostInfo;
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
public class PostInfoRepository {
    private final List<PostInfo> postInfoList;

    public PostInfoRepository(ObjectMapper objectMapper) {
        try (InputStream inputStream = new ClassPathResource("dummy/post_info.json").getInputStream()) {
            this.postInfoList = new ArrayList<>(
                    objectMapper.readValue(inputStream, new TypeReference<List<PostInfo>>() {})
            );
        } catch (Exception e) {
            throw new IllegalStateException("post.json 읽기 실패", e);
        }
    }

    public List<PostInfo> showAllPostInfo() {
        return postInfoList;
    }

    public void save(PostInfo postInfo){
        postInfoList.add(postInfo);
    }

    public Optional<PostInfo> findByUuid(UUID postUuid){
        PostInfo postInfo = null;
        for(PostInfo findPostInfo : postInfoList){
            if(findPostInfo.getPostUuid().equals(postUuid)){
                postInfo = findPostInfo;
                break;
            }
        }
        return Optional.ofNullable(postInfo);
    }
}
