package org.example.communityservice.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.communityservice.common.Exception.*;
import org.example.communityservice.common.dto.ErrorInfoDto;
import org.example.communityservice.common.dto.ErrorResponseDto;
import org.example.communityservice.dto.comment.CommentResponseDto;
import org.example.communityservice.dto.post.PostRequestDto;
import org.example.communityservice.dto.post.PostResponseDto;
import org.example.communityservice.dto.post.PostUpdateRequestDto;
import org.example.communityservice.dummyObject.Comment;
import org.example.communityservice.dummyObject.Post;
import org.example.communityservice.dummyObject.PostInfo;
import org.example.communityservice.dummyObject.User;
import org.example.communityservice.repository.CommentRepository;
import org.example.communityservice.repository.PostInfoRepository;
import org.example.communityservice.repository.PostRepository;
import org.example.communityservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostInfoRepository postInfoRepository;
    private final CommentRepository commentRepository;

    public List<PostResponseDto> showPostList(UUID userUuid){
        userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        List<Post> postList = postRepository.showAllPost();
        List<PostInfo> postInfoList = postInfoRepository.showAllPostInfo();

        for(int i=0; i<postList.size(); i++){
            for(int j=0; j<postInfoList.size(); j++){
                if(postList.get(i).getPostUuid().equals(postInfoList.get(j).getPostUuid())){
                    postResponseDtoList.add(new PostResponseDto(postList.get(i).getPostUuid(), postList.get(i).getPostTitle(), postInfoList.get(j).getLikeCount(),
                            postInfoList.get(j).getCommentCount(), postInfoList.get(j).getViewCount(), postList.get(i).getPostDate(), postList.get(i).getWriterNickname()));
                    break;
                }
            }
        }
        return postResponseDtoList;
    }

    public PostResponseDto createPost(UUID userUuid, @Valid PostRequestDto postRequestDto){
        User user = userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));

        UUID postUuid = UUID.randomUUID();
        String writerNickname = user.getNickname();
        LocalDateTime postDate = LocalDateTime.now();
        Post post = new Post(postUuid, userUuid, writerNickname, postRequestDto.getPostTitle(), postDate, postRequestDto.getPostContent(), postRequestDto.getPostImage());
        postRepository.save(post);
        PostInfo postInfo = new PostInfo(postUuid);
        postInfoRepository.save(postInfo);
        return new PostResponseDto(postUuid, userUuid);
    }

    public PostResponseDto showPostDetail(UUID userUuid, UUID postUuid){

        userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));
        Post post = postRepository.findByUuid(postUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post", "not_exist")))));
        PostInfo postInfo = postInfoRepository.findByUuid(postUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post_info", "not_exist")))));
        postInfo.increaseViewCount();
        List<Comment> commentList = commentRepository.findAllByPostUuid(postUuid);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            User user = userRepository.findByUuid(comment.getCommentWriterUuid()).orElseThrow(() -> new UnauthorizedException("not_exist"));
            String writerProfileImage = user.getProfileImage();
            String writerNickname = user.getNickname();
            commentResponseDtoList.add(new CommentResponseDto(comment, writerProfileImage, writerNickname));
        }

        return new PostResponseDto(post, postInfo.getLikeCount(), postInfo.getCommentCount(), postInfo.getViewCount(), commentResponseDtoList);
    }

    public PostResponseDto updatePost(UUID userUuid, UUID postUuid, @Valid PostUpdateRequestDto postUpdateRequestDto){
        userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));
        Post existPost = postRepository.findByUuid(postUuid).orElseThrow(() -> new NotFoundException ("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto(null, "not_exist")))));

        if(!userUuid.equals(existPost.getWriterUuid())){
            throw new ForbiddenException();
        }

        if(postUpdateRequestDto.getPostTitle()==null && postUpdateRequestDto.getPostContent()==null && postUpdateRequestDto.getPostImage()==null){
            throw new BadRequestException("invalid_request");
        }

        if(postUpdateRequestDto.getPostTitle()!=null){
            existPost.setPostTitle(postUpdateRequestDto.getPostTitle());
        }
        if(postUpdateRequestDto.getPostContent()!=null){
            existPost.setPostContent(postUpdateRequestDto.getPostContent());
        }
        if(postUpdateRequestDto.getPostImage()!=null){
            existPost.setPostImage(postUpdateRequestDto.getPostImage());
        }
        return new PostResponseDto(existPost.getPostTitle(), existPost.getPostContent(), existPost.getPostImage());
    }

    public void deletePost(UUID userUuid, UUID postUuid){
        userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));
        Post existPost = postRepository.findByUuid(postUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto(null, "not_exist")))));

        if(!userUuid.equals(existPost.getWriterUuid())){
            throw new ForbiddenException();
        }
        postRepository.delete(existPost);
    }
}
