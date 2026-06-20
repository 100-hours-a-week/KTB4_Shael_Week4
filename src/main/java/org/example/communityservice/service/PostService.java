package org.example.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.example.communityservice.common.CustomException;
import org.example.communityservice.common.dto.ErrorInfoDto;
import org.example.communityservice.common.dto.ErrorResponseDto;
import org.example.communityservice.dto.post.PostRequestDto;
import org.example.communityservice.dto.post.PostResponseDto;
import org.example.communityservice.dummyObject.Comment;
import org.example.communityservice.dummyObject.Post;
import org.example.communityservice.dummyObject.PostInfo;
import org.example.communityservice.repository.CommentRepository;
import org.example.communityservice.repository.PostInfoRepository;
import org.example.communityservice.repository.PostRepository;
import org.example.communityservice.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostInfoRepository postInfoRepository;
    private final CommentRepository commentRepository;

        userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));
    public List<PostResponseDto> showPostList(UUID userUuid){

        List<PostResponseDto> postResponseDtoList = new ArrayList<>();

        List<Post> postList = postRepository.showAllPost();
        List<PostInfo> postInfoList = postInfoRepository.showAllPostInfo();

        for(int i=0; i<postList.size(); i++){
            for(int j=0; j<postInfoList.size(); j++){
                if(postList.get(i).getPostUuid().equals(postInfoList.get(j).getPostUuid())){
                    postResponseDtoList.add(new PostResponseDto(postList.get(i).getPostUuid(), postList.get(i).getPostTitle(), postInfoList.get(j), postList.get(i).getPostDate(), postList.get(i).getWriterNickname()));
                    break;
                }
            }
        }
        return postResponseDtoList;
    }

    public PostResponseDto createPost(UUID userUuid, PostRequestDto postRequestDto){

        userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));
        if(postRequestDto.getPostTitle()==null || postRequestDto.getPostContent()==null || postRequestDto.getPostImage()==null){
            throw new CustomException(HttpStatus.BAD_REQUEST,"invalid_request");
        }

        UUID postUuid = UUID.randomUUID();
        String writerNickname = userRepository.findByUuid(userUuid).orElseThrow().getNickname();
        LocalDateTime postDate = LocalDateTime.now();
        Post post = new Post(postUuid, userUuid, writerNickname, postRequestDto.getPostTitle(), postDate, postRequestDto.getPostContent(), postRequestDto.getPostImage());
        postRepository.save(post);
        PostInfo postInfo = new PostInfo(postUuid);
        postInfoRepository.save(postInfo);
        return new PostResponseDto(postUuid, userUuid);
    }

    public PostResponseDto showPostDetail(UUID userUuid, UUID postUuid){

        userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));

        Post post = postRepository.findByUuid(postUuid).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post", "not_exist")))));
        PostInfo postInfo = postInfoRepository.findByUuid(postUuid).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post_info", "not_exist")))));
        postInfo.increaseViewCount();
        List<Comment> commentList = commentRepository.findAllByPostUuid(postUuid);

        return new PostResponseDto(post, postInfo, commentList);
    }

    public PostResponseDto updatePost(UUID userUuid, UUID postUuid, PostRequestDto postRequestDto){
        userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));
        Post existPost = postRepository.findByUuid(postUuid).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"not_found", new ErrorResponseDto(List.of(new ErrorInfoDto(null, "not_exist")))));

        if(!userUuid.equals(existPost.getWriterUuid())){
            throw new CustomException(HttpStatus.FORBIDDEN,"unauthorized");
        }

        if(postRequestDto.getPostTitle()==null && postRequestDto.getPostContent()==null && postRequestDto.getPostImage()==null){
            throw new CustomException(HttpStatus.BAD_REQUEST,"invalid_request");
        }

        if(postRequestDto.getPostTitle()!=null){
            existPost.setPostTitle(postRequestDto.getPostTitle());
        }
        if(postRequestDto.getPostContent()!=null){
            existPost.setPostContent(postRequestDto.getPostContent());
        }
        if(postRequestDto.getPostImage()!=null){
            existPost.setPostImage(postRequestDto.getPostImage());
        }
        return new PostResponseDto(existPost.getPostTitle(), existPost.getPostContent(), existPost.getPostImage());
    }

    public void deletePost(UUID userUuid, UUID postUuid){
        userRepository.findByUuid(userUuid).orElseThrow(() -> new CustomException(HttpStatus.UNAUTHORIZED, "not_exist"));
        Post existPost = postRepository.findByUuid(postUuid).orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"not_found", new ErrorResponseDto(List.of(new ErrorInfoDto(null, "not_exist")))));

        if(!userUuid.equals(existPost.getWriterUuid())){
            throw new CustomException(HttpStatus.FORBIDDEN,"unauthorized");
        }
        postRepository.delete(existPost);
    }
}
