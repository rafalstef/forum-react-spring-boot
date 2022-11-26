package dev.forum.forum.service;

import dev.forum.forum.model.ForumThread;
import dev.forum.forum.model.user.User;
import dev.forum.forum.repository.ForumThreadRepo;
import dev.forum.forum.utils.dto.ForumThreadDto;
import dev.forum.forum.utils.exception.ResourceNotFoundException;
import dev.forum.forum.utils.mapper.ForumThreadMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class ForumThreadService {

    private final ForumThreadRepo forumThreadRepo;
    private final AuthService authService;
    private final ForumThreadMapper forumThreadMapper;

    private String getThreadNotFoundMessage(String name) {
        return "Forum thread '" + name + "' not found";
    }

    public ForumThreadDto getByName(String forumThreadName) {
        ForumThread forumThread = forumThreadRepo.findByName(forumThreadName)
                .orElseThrow(() -> new ResourceNotFoundException(getThreadNotFoundMessage(forumThreadName)));

        return forumThreadMapper.mapForumThreadToDto(forumThread);
    }

    public List<ForumThreadDto> getAllSortedByNumberOfSubscribers() {
        return forumThreadRepo.findAllSortedBySubscribersSize(Pageable.unpaged())
                .stream().map(forumThreadMapper::mapForumThreadToDto)
                .collect(Collectors.toList());
    }

    public List<ForumThreadDto> getTopFiveSortedBySubscribers() {
        return forumThreadRepo.findAllSortedBySubscribersSize(PageRequest.of(0, 5))
                .stream().map(forumThreadMapper::mapForumThreadToDto)
                .collect(Collectors.toList());
    }

    public ForumThreadDto createForumThread(ForumThreadDto forumThreadDto) {
        ForumThread threadToSave = forumThreadMapper.mapDtoToForumThread(forumThreadDto);
        forumThreadRepo.save(threadToSave);
        return forumThreadMapper.mapForumThreadToDto(threadToSave);
    }

    public ForumThreadDto updateDescription(String forumThreadName, String description) {
        ForumThread toUpdate = forumThreadRepo.findByName(forumThreadName)
                .orElseThrow(() -> new ResourceNotFoundException(getThreadNotFoundMessage(forumThreadName)));

        User currentUser = authService.getCurrentUser();
        if (currentUser != toUpdate.getUser()) {
            throw new AuthorizationServiceException("User " + currentUser.getUsername() +
                    " not authorized to update " + "thread '" + forumThreadName + "'.");
        }

        toUpdate.setDescription(description);
        forumThreadRepo.save(toUpdate);
        return forumThreadMapper.mapForumThreadToDto(toUpdate);
    }
}
