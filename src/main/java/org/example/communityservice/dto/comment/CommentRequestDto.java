package org.example.communityservice.dto.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommentRequestDto {
    @NotBlank(message = "comment_content_required")
    private String commentContent;
}
