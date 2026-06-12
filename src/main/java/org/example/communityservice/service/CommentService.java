package org.example.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.example.communityservice.common.CustomException;
import org.example.communityservice.common.dto.ErrorInfoDto;
import org.example.communityservice.common.dto.ErrorResponseDto;
import org.example.communityservice.dto.CommentResponseDto;
import org.example.communityservice.dummyObject.Comment;
import org.example.communityservice.dummyObject.PostInfo;
import org.example.communityservice.repository.CommentRepository;
import org.example.communityservice.repository.PostInfoRepository;
import org.example.communityservice.repository.PostRepository;
import org.example.communityservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostInfoRepository postInfoRepository;

    public CommentResponseDto createComment(UUID userUuid, UUID postUuid, String commentContent) {
        if(commentContent == null){
            throw new CustomException(HttpStatus.BAD_REQUEST,"invalid_request");
        }
        userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));
        postRepository.findByUuid(postUuid).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post", "not_exist")))));

        UUID commentUuid = UUID.randomUUID();
        LocalDateTime commentDate = LocalDateTime.now();

        commentRepository.save(new Comment(commentUuid, postUuid, userUuid, commentDate, commentContent));
        PostInfo postInfo = postInfoRepository.findByUuid(postUuid).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post_info", "not_exist")))));
        postInfo.increaseCommentCount();
        return new CommentResponseDto(userUuid, commentUuid);
    }

    public CommentResponseDto updateComment(UUID userUuid, UUID postUuid, UUID commentUuid, String commentContent) {
        if(commentContent == null){
            throw new CustomException(HttpStatus.BAD_REQUEST,"invalid_request");
        }
        userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));
        postRepository.findByUuid(postUuid).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post", "not_exist")))));
        Comment comment = commentRepository.findByUuid(commentUuid).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("comment", "not_exist")))));
        if(!userUuid.equals(comment.getCommentWriterUuid())){
            throw new CustomException(HttpStatus.FORBIDDEN,"unauthorized");
        }

        comment.setCommentContent(commentContent);
        return new CommentResponseDto(commentContent);
    }

    public void deleteComment(UUID userUuid, UUID postUuid, UUID commentUuid) {
        userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));
        postRepository.findByUuid(postUuid).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post", "not_exist")))));
        Comment comment = commentRepository.findByUuid(commentUuid).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("comment", "not_exist")))));
        if(!userUuid.equals(comment.getCommentWriterUuid())){
            throw new CustomException(HttpStatus.FORBIDDEN,"unauthorized");
        }
        commentRepository.deleteComment(comment);
        PostInfo postInfo = postInfoRepository.findByUuid(postUuid).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post_info", "not_exist")))));
        postInfo.decreaseCommentCount();
    }
}
