package dev.stockman.functions;

import dev.stockman.functions.posts.Message;
import dev.stockman.functions.posts.Post;
import dev.stockman.functions.posts.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}

	@Bean
	public Function<Message, Post> post(PostService postService) {
		return postService::post;
	}

	@Bean
	public Supplier<Post> last(PostService postService) {
		return postService::last;
	}

	@Bean
	public Consumer<Message> publish(PostService postService) {
		return postService::post;
	}

}
