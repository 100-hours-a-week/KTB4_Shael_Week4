package org.example.communityservice.dto;

import lombok.Getter;

import java.util.UUID;

@Getter
public class CommentResponseDto {
    private UUID commentUuid;
    private UUID commentWriterUuid;
    private String commentContent;

    public CommentResponseDto(UUID commentWriterUuid, UUID commentUuid){
        this.commentWriterUuid = commentWriterUuid;
        this.commentUuid = commentUuid;
    }

    public CommentResponseDto(String commentContent){
        this.commentContent = commentContent;
    }
}
