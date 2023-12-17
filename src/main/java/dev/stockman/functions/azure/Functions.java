package dev.stockman.functions.azure;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
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

@Component
public class Functions {

    private final Function<Message, Post> post;
    private final Supplier<Post> last;
    private final Consumer<Message> publish;

    public Functions(Function<Message, Post> post, Supplier<Post> last, Consumer<Message> publish) {
        this.post = post;
        this.last = last;
        this.publish = publish;
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

    @FunctionName("last")
    public HttpResponseMessage last(
            @HttpTrigger(
                    name = "request",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS
            )
            HttpRequestMessage<Void> request) {
        try {
            return handleResponse(request, Optional.ofNullable(last.get()));
        } catch (Exception e) {
            return handleResponse(request, e);
        }
    }

    @FunctionName("recent")
    public HttpResponseMessage recent(
            @HttpTrigger(
                    name = "request",
                    methods = {HttpMethod.GET},
                    authLevel = AuthorizationLevel.ANONYMOUS
            )
            HttpRequestMessage<Void> request) {
        try {
            var items = last.get();
            return handleResponse(request, Optional.of(
                Objects.isNull(items)
                    ? Collections.emptyList()
                    : List.of(items)
                )
            );
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
        static Response error(Object error) {
            return new Response(null, null, error);
        }
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
                    .body(Response.error(validationException.violations()))
                    .header("Content-Type", "application/json")
                    .build();
        } else {
            return request.createResponseBuilder(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

}
