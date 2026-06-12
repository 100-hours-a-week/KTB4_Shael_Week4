package org.example.communityservice.repository;

import org.example.communityservice.dummyObject.Comment;
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
public class CommentRepository {
    private final List<Comment> commentList;

    public CommentRepository(ObjectMapper objectMapper) {
        try (InputStream inputStream = new ClassPathResource("dummy/comment.json").getInputStream()) {
            this.commentList = new ArrayList<>(
                    objectMapper.readValue(inputStream, new TypeReference<List<Comment>>() {})
            );
        } catch (Exception e) {
            throw new IllegalStateException("post.json 읽기 실패", e);
        }
    }

    public void save(Comment comment){
        commentList.add(comment);
    }

    public Optional<Comment> findByUuid(UUID commentUuid){
        Comment comment = null;
        for(Comment findComment : commentList){
            if(findComment.getCommentUuid().equals(commentUuid)){
                comment = findComment;
                break;
            }
        }
        return Optional.ofNullable(comment);
    }

    public void deleteComment(Comment comment){
        commentList.remove(comment);
    }

    public List<Comment> findAllByPostUuid(UUID postUuid){
        List<Comment> findCommentList = new ArrayList<>();
        for(Comment findComment : commentList){
            if(findComment.getPostUuid().equals(postUuid)){
                findCommentList.add(findComment);
            }
        }
        return findCommentList;
    }
}
