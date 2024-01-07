package dev.stockman.functions.azure;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.BindingName;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import dev.stockman.functions.posts.Message;
import dev.stockman.functions.posts.Post;
import dev.stockman.functions.validation.ValidationException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Component
public class AzureFunctions {

    private final Function<Message, Post> post;
    private final Supplier<List<Post>> all;
    private final Function<Integer, Optional<Post>> get;
    private final Consumer<Message> publish;
    private final Runnable reset;
    private final UnaryOperator<Post> store;

    public AzureFunctions(Function<Message, Post> post, Supplier<List<Post>> all, Function<Integer, Optional<Post>> get, Consumer<Message> publish, Runnable reset, UnaryOperator<Post> store) {
        this.post = post;
        this.all = all;
        this.get = get;
        this.publish = publish;
        this.reset = reset;
        this.store = store;
    }

    @FunctionName("post")
    public HttpResponseMessage post(
            @HttpTrigger(
                    name = "request",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS
            )
            HttpRequestMessage<Message> request) {
        try {
            return handleResponse(request, Optional.ofNullable(post.apply(request.getBody())));
        } catch (Exception e) {
            return handleResponse(request, e);
        }
    }

    @FunctionName("all")
    public HttpResponseMessage all(
            @HttpTrigger(
                    name = "request",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS
            )
            HttpRequestMessage<Void> request) {
        try {
            return handleResponse(request, all.get());
        } catch (Exception e) {
            return handleResponse(request, e);
        }
    }

    @FunctionName("get")
    public HttpResponseMessage get(
            @HttpTrigger(
                    name = "request",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS,
                    route = "get/{id}"
            )
            HttpRequestMessage<Void> request,
            @BindingName("id") Integer id) {
        try {
            return handleResponse(request, get.apply(id));
        } catch (Exception e) {
            return handleResponse(request, e);
        }
    }

    @FunctionName("publish")
    public HttpResponseMessage publish(
            @HttpTrigger(
                    name = "request",
                    methods = {HttpMethod.POST},
                    authLevel = AuthorizationLevel.ANONYMOUS
            )
            HttpRequestMessage<Message> request) {
        try {
            publish.accept(request.getBody());
            return handleResponse(request);
        } catch (Exception e) {
            return handleResponse(request, e);
        }
    }

    @FunctionName("reset")
    public HttpResponseMessage reset(
            @HttpTrigger(
                    name = "request",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS
            )
            HttpRequestMessage<Void> request) {
        try {
            reset.run();
            return handleResponse(request);
        } catch (Exception e) {
            return handleResponse(request, e);
        }
    }

    @FunctionName("store")
    public HttpResponseMessage store(
            @HttpTrigger(
                    name = "request",
                    methods = {HttpMethod.PUT},
                    authLevel = AuthorizationLevel.ANONYMOUS
            )
            HttpRequestMessage<PostRequest> request) {
        try {
            System.out.println(request.getBody());
            return handleResponse(request, Optional.ofNullable(store.apply(request.getBody().toPost())));
        } catch (Exception e) {
            return handleResponse(request, e);
        }
    }

    record Response(
            Object data,
            Collection<?> items,
            Object error
    ){
        static Response data(Object data) {
            return new Response(data, null, null);
        }
        static Response items(Collection<?> items) {
            return new Response(null, items, null);
        }
        static Response errors(Object error) {
            return new Response(null, null, error);
        }
    }

    private HttpResponseMessage handleResponse(HttpRequestMessage<?> request, Collection<?> responseBody) {
        return handleResponse(request, Optional.ofNullable(responseBody));
    }

    private HttpResponseMessage handleResponse(HttpRequestMessage<?> request, Object responseBody) {
        return handleResponse(request, Optional.ofNullable(responseBody));
    }

    private HttpResponseMessage handleResponse(HttpRequestMessage<?> request, Optional<?> responseBody) {
        if (responseBody.isEmpty()) {
            return request.createResponseBuilder(HttpStatus.NOT_FOUND).build();
        } else if (responseBody.get() instanceof Collection<?> items) {
            return request.createResponseBuilder(HttpStatus.OK)
                    .body(Response.items(items))
                    .header("Content-Type", "application/json")
                    .build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK)
                    .body(Response.data(responseBody.get()))
                    .header("Content-Type", "application/json")
                    .build();
        }
    }

    private HttpResponseMessage handleResponse(HttpRequestMessage<?> request) {
        return request.createResponseBuilder(HttpStatus.NO_CONTENT)
                .build();
    }

    private HttpResponseMessage handleResponse(HttpRequestMessage<?> request, Throwable throwable) {
        if (throwable instanceof ValidationException validationException) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
                    .body(Response.errors(validationException.violations()))
                    .header("Content-Type", "application/json")
                    .build();
        } else {
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public static class PostRequest {
        private Integer id;
        private String message;
        private String created;

        public PostRequest(){}

        public PostRequest(Integer id, String message, String created) {
            this.id = id;
            this.message = message;
            this.created = created;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
        public Post toPost() {
            return new Post(this.id, this.message, this.created);
        }
    }
}
