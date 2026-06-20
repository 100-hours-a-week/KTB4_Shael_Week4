package org.example.communityservice.service;

import lombok.RequiredArgsConstructor;
import org.example.communityservice.common.Exception.ForbiddenException;
import org.example.communityservice.common.Exception.NotFoundException;
import org.example.communityservice.common.Exception.UnauthorizedException;
import org.example.communityservice.common.dto.ErrorInfoDto;
import org.example.communityservice.common.dto.ErrorResponseDto;
import org.example.communityservice.dto.PostInfoResponseDto;
import org.example.communityservice.dummyObject.Post;
import org.example.communityservice.dummyObject.PostInfo;
import org.example.communityservice.dummyObject.User;
import org.example.communityservice.repository.PostInfoRepository;
import org.example.communityservice.repository.PostRepository;
import org.example.communityservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostInfoService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostInfoRepository postInfoRepository;

    public PostInfoResponseDto toggleLike(UUID userUuid, UUID postUuid){
        User user = userRepository.findByUuid(userUuid).orElseThrow(() -> new UnauthorizedException("not_exist"));
        Post post = postRepository.findByUuid(postUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("post", "not_exist")))));
        PostInfo postInfo = postInfoRepository.findByUuid(postUuid).orElseThrow(() -> new NotFoundException("not_found", new ErrorResponseDto(List.of(new ErrorInfoDto("postInfo", "not_exist")))));

        if(post.getWriterUuid().equals(userUuid)){
            throw new ForbiddenException();
        }
        if(user.getLikePost().contains(postUuid)) {
            userRepository.deleteLikePost(user, postUuid);
            postInfo.decreaseLikeCount();
        }
        else{
            user.getLikePost().add(postUuid);
            postInfo.increaseLikeCount();
        }
        return new PostInfoResponseDto(postInfo.getLikeCount());
    }
}
