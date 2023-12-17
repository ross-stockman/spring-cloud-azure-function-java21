package dev.stockman.functions.posts;

import dev.stockman.functions.validation.ValidationService;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PostService {

    private static Post lastPost = new Post(1, "initial post", Instant.now().toString());

    public PostService(ValidationService validator) {
        this.validator = validator;
    }

    private static Post update(Post post) {
        lastPost = post;
        return lastPost;
    }

    private final ValidationService validator;

    public Post post(Message message) {
        validator.validate(message);
        return update(new Post(lastPost.id()+1, message.getMessage(), Instant.now().toString()));
    }
    public Post last() {
        return lastPost;
    }

}
