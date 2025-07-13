package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
    private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public List<Post> all() {
        return new ArrayList<>(posts.values());
    }

    public Optional<Post> getById(long id) {
        return Optional.ofNullable(posts.get(id));
    }

    public Post save(Post post) {
        if (post.getId() == 0) {
            long id = idCounter.incrementAndGet();
            Post newPost = new Post(id, post.getContent());
            posts.put(id, newPost);
            return newPost;
        } else {
            if (posts.replace(post.getId(), post) != null) {
                return post;
            } else {
                throw new NotFoundException("Post with id=" + post.getId() + " not found");
            }
        }
    }

    public void removeById(long id) {
        Post removedPost = posts.remove(id);
        if (removedPost == null) {
            throw new NotFoundException("Post with id=" + id + " not found");
        }
    }
}
