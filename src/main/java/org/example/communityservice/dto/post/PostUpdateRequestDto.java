package org.example.communityservice.dto.post;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostUpdateRequestDto {
    @Size(max = 26, message = "post_title_too_long")
    private String postTitle;

    private String postContent;

    @Size(max = 255, message = "post_image_too_long")
    private String postImage;
}
