package org.example.communityservice.dummyObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PostInfo {
    private UUID postUuid;
    private int likeCount;
    private int commentCount;
    private int viewCount;

    public PostInfo(UUID postUuid){
        this.postUuid  = postUuid;
        this.likeCount = 0;
        this.commentCount = 0;
        this.viewCount = 0;
    }

    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount(){
        if(this.likeCount>0){
            this.likeCount--;
        }
    }

    public void increaseCommentCount(){
        this.commentCount++;
    }

    public void decreaseCommentCount(){
        if(this.commentCount>0){
            this.commentCount--;
        }
    }

    public void increaseViewCount(){
        this.viewCount++;
    }
}
