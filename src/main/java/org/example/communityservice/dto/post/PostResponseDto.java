package org.example.communityservice.dto.post;

import lombok.Getter;
import org.example.communityservice.dto.comment.CommentResponseDto;
import org.example.communityservice.dummyObject.Post;

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
    private int likeCount;
    private int commentCount;
    private int viewCount;
    private String postImage;
    private List<CommentResponseDto> commentList;

    public PostResponseDto(UUID postUuid, UUID writerUuid){
        this.postUuid = postUuid;
        this.writerUuid = writerUuid;
    }

    public PostResponseDto(String postTitle, String postContent, String postImage){
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postImage = postImage;
    }

    public PostResponseDto(UUID postUuid, String postTitle, int likeCount, int commentCount, int viewCount, LocalDateTime postDate, String writerNickname){
        this.postUuid = postUuid;
        this.postTitle = postTitle;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.postDate = postDate;
        this.writerNickname = writerNickname;
    }

    public PostResponseDto(Post post, int likeCount, int commentCount, int viewCount, List<CommentResponseDto> commentList){
        this.writerUuid = post.getWriterUuid();
        this.postTitle = post.getPostTitle();
        this.writerNickname = post.getWriterNickname();
        this.postDate = post.getPostDate();
        this.postImage = post.getPostImage();
        this.postContent = post.getPostContent();
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.commentList = commentList;
    }
}
