package dev.forum.forum.service;

import dev.forum.forum.model.ForumThread;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import dev.forum.forum.repository.ForumThreadRepo;
import dev.forum.forum.repository.PostRepo;
import dev.forum.forum.repository.UserRepo;
import dev.forum.forum.utils.dto.PostRequest;
import dev.forum.forum.utils.dto.PostResponse;
import dev.forum.forum.utils.exception.ResourceNotFoundException;
import dev.forum.forum.utils.mapper.PostMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    private final PostRepo postRepo;
    private final PostMapper postMapper;
    private final ForumThreadRepo forumThreadRepo;
    private final UserRepo userRepo;
    private final AuthService authService;

    public List<PostResponse> getAllByForumThread(String forumThreadName) {
        ForumThread forumThread = forumThreadRepo.findByName(forumThreadName)
                .orElseThrow(() -> new ResourceNotFoundException("Forum thread '" + forumThreadName + "' not found."));

        return postRepo.findAllByForumThreadOrderByCreatedDateDesc(forumThread)
                .stream().map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getAllByUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User '" + username + "' not found."));

        return postRepo.findAllByUserOrderByCreatedDateDesc(user)
                .stream().map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getLastPostsFromSubscribedThreads(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User '" + username + "' not found."));

        return postRepo.findAllByForumThreadInOrderByCreatedDateDesc(user.getSubscribedThreads())
                .stream().map(postMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public PostResponse getOneByPostId(Long postId) {
        return postMapper.mapToDto(postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + postId + " not found.")));
    }


    public PostResponse create(PostRequest postRequest) {
        ForumThread forumThread = forumThreadRepo.findByName(postRequest.getForumThreadName())
                .orElseThrow(() -> new ResourceNotFoundException("Forum thread '" + postRequest.getForumThreadName() + "' not found."));
        User currentUser = authService.getCurrentUser();

        Post postToSave = postMapper.mapDtoToPost(postRequest, forumThread, currentUser);

        postRepo.save(postToSave);
        return postMapper.mapToDto(postToSave);
    }
}
