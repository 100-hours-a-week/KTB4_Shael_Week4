package org.example.communityservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.communityservice.common.Exception.*;
import org.example.communityservice.common.dto.ErrorInfoDto;
import org.example.communityservice.common.dto.ErrorResponseDto;
import org.example.communityservice.dto.comment.CommentRequestDto;
import org.example.communityservice.dto.comment.CommentResponseDto;
import org.example.communityservice.dummyObject.Comment;
import org.example.communityservice.dummyObject.PostInfo;
import org.example.communityservice.repository.CommentRepository;
import org.example.communityservice.repository.PostInfoRepository;
import org.example.communityservice.repository.PostRepository;
import org.example.communityservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostInfoRepository postInfoRepository;

    public CommentResponseDto createComment(UUID userUuid, UUID postUuid, @Valid CommentRequestDto commentRequestDto) {
        userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));
        postRepository.findByUuid(postUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post", "not_exist")))));

        UUID commentUuid = UUID.randomUUID();
        LocalDateTime commentDate = LocalDateTime.now();

        commentRepository.save(new Comment(commentUuid, postUuid, userUuid, commentDate, commentRequestDto.getCommentContent()));
        PostInfo postInfo = postInfoRepository.findByUuid(postUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post_info", "not_exist")))));
        postInfo.increaseCommentCount();
        return new CommentResponseDto(userUuid, commentUuid);
    }

    public CommentResponseDto updateComment(UUID userUuid, UUID postUuid, UUID commentUuid, @Valid CommentRequestDto commentRequestDto) {
        userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));
        postRepository.findByUuid(postUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post", "not_exist")))));
        Comment comment = commentRepository.findByUuid(commentUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("comment", "not_exist")))));
        if(!userUuid.equals(comment.getCommentWriterUuid())){
            throw new ForbiddenException();
        }

        comment.setCommentContent(commentRequestDto.getCommentContent());
        return new CommentResponseDto(commentRequestDto.getCommentContent());
    }

    public void deleteComment(UUID userUuid, UUID postUuid, UUID commentUuid) {
        userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));
        postRepository.findByUuid(postUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post", "not_exist")))));
        Comment comment = commentRepository.findByUuid(commentUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("comment", "not_exist")))));
        if(!userUuid.equals(comment.getCommentWriterUuid())){
            throw new ForbiddenException();
        }
        commentRepository.deleteComment(comment);
        PostInfo postInfo = postInfoRepository.findByUuid(postUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post_info", "not_exist")))));
        postInfo.decreaseCommentCount();
    }
}
