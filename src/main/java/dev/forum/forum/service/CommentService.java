package dev.forum.forum.service;

import dev.forum.forum.model.Comment;
import dev.forum.forum.model.Post;
import dev.forum.forum.model.user.User;
import dev.forum.forum.repository.CommentRepo;
import dev.forum.forum.repository.PostRepo;
import dev.forum.forum.repository.UserRepo;
import dev.forum.forum.utils.dto.CommentRequest;
import dev.forum.forum.utils.dto.CommentResponse;
import dev.forum.forum.utils.exception.ResourceNotFoundException;
import dev.forum.forum.utils.mapper.CommentMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
@Transactional
public class CommentService {

    private CommentRepo commentRepo;
    private CommentMapper commentMapper;
    private UserRepo userRepo;
    private PostRepo postRepo;
    private AuthService authService;

    public List<CommentResponse> getAllByUser(String username) {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User '" + username + "' not found."));

        return commentRepo.findAllByUserOrderByCreatedDateDesc(user)
                .stream().map(commentMapper::mapCommentToDto)
                .collect(Collectors.toList());
    }

    public List<CommentResponse> getAllByPost(Long postId) {
        Post post = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + postId + " not found."));

        return commentRepo.findAllByPostOrderByCreatedDateDesc(post)
                .stream().map(commentMapper::mapCommentToDto)
                .collect(Collectors.toList());
    }

    public CommentResponse create(CommentRequest commentRequest) {
        User user = authService.getCurrentUser();
        Post post = postRepo.findById(commentRequest.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post with id " + commentRequest.getPostId() + " not" +
                        " " +
                        "found."));

        Comment newComment = commentMapper.mapDtoToComment(commentRequest, post, user);
        commentRepo.save(newComment);
        return commentMapper.mapCommentToDto(newComment);
    }
}
