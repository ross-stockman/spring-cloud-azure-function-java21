package dev.stockman.functions.posts;

import dev.stockman.functions.validation.ValidationService;
import dev.stockman.functions.validation.Violation;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class PostService {

    private static final LinkedList<Post> posts = new LinkedList<>();
    private static final int maxSize = 10;

    public PostService(ValidationService validator) {
        this.validator = validator;
    }

    private static Post postMessage(Message message) {
        int nextId;
        String updated = Instant.now().toString();
        synchronized (posts) {
            if (posts.size() >= maxSize) {
                posts.remove(0);
            }
            nextId = posts.stream().mapToInt(Post::id).max().orElse(0)+1;
            posts.add(new Post(nextId, message.getMessage(), updated));
        }
        return new Post(nextId, message.getMessage(), updated);
    }

    private final ValidationService validator;

    public Post post(Message message) {
        validator.validate(message);
        return postMessage(message);
    }
    public Post store(Post message) {
        synchronized (posts) {
            validator.validate(message, (m) -> {
                Set<Violation> violations = new HashSet<>();
                if (posts.stream().anyMatch((post -> post.id().equals(m.id())))) {
                    violations.add(new Violation("id", "Post already exists!", m.id()));
                }
                return violations;
            });
            if (posts.size() >= maxSize) {
                posts.remove(0);
            }
            posts.add(message);
        }
        return message;
    }
    public List<Post> all() {
        return new ArrayList<>(posts);
    }
    public Optional<Post> get(int id) {
        return posts.stream().filter(post -> post.id().equals(id)).findFirst();
    }

    public void reset() {
        posts.clear();
    }

}
