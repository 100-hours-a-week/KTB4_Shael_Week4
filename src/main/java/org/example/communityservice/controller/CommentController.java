package org.example.communityservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.communityservice.dto.comment.CommentRequestDto;
import org.example.communityservice.dto.comment.CommentResponseDto;
import org.example.communityservice.common.dto.CommonResponseDto;
import org.example.communityservice.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{userUuid}/posts/{postUuid}/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommonResponseDto<CommentResponseDto>> createComment(@PathVariable UUID userUuid, @PathVariable UUID postUuid, @Valid @RequestBody CommentRequestDto commentRequestDto){
        CommentResponseDto commentResponseDto = commentService.createComment(userUuid, postUuid, commentRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseDto<>("register_success", commentResponseDto));
    }

    @PatchMapping("/{commentUuid}")
    public ResponseEntity<CommonResponseDto<CommentResponseDto>> updateComment(@PathVariable UUID userUuid, @PathVariable UUID postUuid, @PathVariable UUID commentUuid, @Valid @RequestBody CommentRequestDto commentRequestDto){
        CommentResponseDto commentResponseDto = commentService.updateComment(userUuid, postUuid, commentUuid, commentRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("update_success", commentResponseDto));
    }

    @DeleteMapping("/{commentUuid}")
    public ResponseEntity<CommonResponseDto<Void>> deleteComment(@PathVariable UUID userUuid, @PathVariable UUID postUuid, @PathVariable UUID commentUuid){
        commentService.deleteComment(userUuid, postUuid, commentUuid);

        return ResponseEntity.ok(new CommonResponseDto<>("register_success", null));
    }
}
