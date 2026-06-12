package org.example.communityservice.dto.post;

import lombok.Getter;
import org.example.communityservice.dummyObject.Comment;
import org.example.communityservice.dummyObject.Post;
import org.example.communityservice.dummyObject.PostInfo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
public class PostResponseDto {
    private UUID postUuid;
    private UUID writerUuid;
    private String writerNickname;
    private String postTitle;
    private String postContent;
    private LocalDateTime postDate;
    private PostInfo postInfo;
    private String postImage;
    private List<Comment> postComment;

    public PostResponseDto(UUID postUuid, UUID writerUuid){
        this.postUuid = postUuid;
        this.writerUuid = writerUuid;
    }

    public PostResponseDto(String postTitle, String postContent, String postImage){
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postImage = postImage;
    }

    public PostResponseDto(UUID postUuid, String postTitle, PostInfo postInfo, LocalDateTime postDate, String writerNickname){
        this.postUuid = postUuid;
        this.postTitle = postTitle;
        this.postInfo = postInfo;
        this.postDate = postDate;
        this.writerNickname = writerNickname;
    }

    public PostResponseDto(Post post, PostInfo postInfo, List<Comment> postComment){
        this.writerUuid = post.getWriterUuid();
        this.postTitle = post.getPostTitle();
        this.writerNickname = post.getWriterNickname();
        this.postDate = post.getPostDate();
        this.postImage = post.getPostImage();
        this.postContent = post.getPostContent();
        this.postInfo = postInfo;
        this.postComment = postComment;
    }
}
