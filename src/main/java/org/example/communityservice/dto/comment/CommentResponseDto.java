package org.example.communityservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.communityservice.dummyObject.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CommentResponseDto {
    private UUID commentWriterUuid;
    private UUID commentUuid;
    private String writerProfileImage;
    private String writerNickname;
    private LocalDateTime commentDate;
    private String commentContent;

    public CommentResponseDto(UUID commentWriterUuid, UUID commentUuid){
        this.commentWriterUuid = commentWriterUuid;
        this.commentUuid = commentUuid;
    }

    public CommentResponseDto(String commentContent){
        this.commentContent = commentContent;
    }

    public CommentResponseDto(Comment comment, String writerProfileImage, String writerNickname){
        this.commentWriterUuid = comment.getCommentWriterUuid();
        this.commentUuid = comment.getCommentUuid();
        this.writerProfileImage = writerProfileImage;
        this.writerNickname = writerNickname;
        this.commentDate = comment.getCommentDate();
        this.commentContent = comment.getCommentContent();
    }
}
