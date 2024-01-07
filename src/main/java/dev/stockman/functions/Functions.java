package dev.stockman.functions;

import dev.stockman.functions.posts.Message;
import dev.stockman.functions.posts.Post;
import dev.stockman.functions.posts.PostService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Configuration
public class Functions {
    @Bean
    public Function<Message, Post> post(PostService postService) {
        return postService::post;
    }

    @Bean
    public Supplier<List<Post>> all(PostService postService) {
        return postService::all;
    }

    @Bean
    public Function<Integer, Optional<Post>> get(PostService postService) {
        return postService::get;
    }

    @Bean
    public Consumer<Message> publish(PostService postService) {
        return postService::post;
    }

    @Bean
    public Runnable reset(PostService postService) {
        return postService::reset;
    }

    @Bean
    public UnaryOperator<Post> store(PostService postService) {
        return postService::store;
    }
}
