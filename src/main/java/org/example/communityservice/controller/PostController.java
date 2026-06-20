package org.example.communityservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.communityservice.common.dto.CommonResponseDto;
import org.example.communityservice.dto.PostInfoResponseDto;
import org.example.communityservice.dto.post.PostRequestDto;
import org.example.communityservice.dto.post.PostResponseDto;
import org.example.communityservice.service.PostInfoService;
import org.example.communityservice.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{userUuid}/posts")
public class PostController {

    private final PostService postService;
    private final PostInfoService postInfoService;

    @GetMapping
        List<PostResponseDto> postResponseDtoList = postService.showPosts(userUuid);
    public ResponseEntity<CommonResponseDto<List<PostResponseDto>>> showPostList(@PathVariable UUID userUuid){

        return ResponseEntity.ok(new CommonResponseDto<>("fetch_success", postResponseDtoList));
    }

    @PostMapping
    public ResponseEntity<CommonResponseDto<PostResponseDto>> createPost(@PathVariable UUID userUuid, @RequestBody PostRequestDto postRequestDto){
        PostResponseDto postResponseDto = postService.createPost(userUuid, postRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new CommonResponseDto<>("register_success", postResponseDto));
    }

    @GetMapping("/{postUuid}")
        PostResponseDto postResponseDto = postService.showPost(userUuid, postUuid);
    public ResponseEntity<CommonResponseDto<PostResponseDto>> showPostDetail(@PathVariable UUID userUuid, @PathVariable UUID postUuid){

        return ResponseEntity.ok(new CommonResponseDto<>("fetch_success", postResponseDto));
    }

    @PatchMapping("/{postUuid}")
    public ResponseEntity<CommonResponseDto<PostResponseDto>> updatePost(@PathVariable UUID userUuid, @PathVariable UUID postUuid, @RequestBody PostRequestDto postRequestDto){
        PostResponseDto postResponseDto = postService.updatePost(userUuid, postUuid, postRequestDto);

        return ResponseEntity.ok(new CommonResponseDto<>("update_success", postResponseDto));
    }

    @DeleteMapping("/{postUuid}")
    public ResponseEntity<CommonResponseDto<Void>> deletePost(@PathVariable UUID userUuid, @PathVariable UUID postUuid){
        postService.deletePost(userUuid, postUuid);

        return ResponseEntity.ok(new CommonResponseDto<>("delete_success", null));
    }

    @PostMapping("/{postUuid}/like")
    public ResponseEntity<CommonResponseDto<PostInfoResponseDto>> toggleLike(@PathVariable UUID userUuid, @PathVariable UUID postUuid){
        PostInfoResponseDto postInfoResponseDto = postInfoService.toggleLike(userUuid, postUuid);

        return ResponseEntity.ok(new CommonResponseDto<>("register_success", postInfoResponseDto));
    }
}
