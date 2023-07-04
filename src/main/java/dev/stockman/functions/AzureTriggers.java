package dev.stockman.functions;

import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class AzureTriggers {

    private final Function<String, String> hello;
    private final Supplier<String> time;
    private final Consumer<String> publish;

    public AzureTriggers(Function<String, String> hello, Supplier<String> time, Consumer<String> publish) {
        this.hello = hello;
        this.time = time;
        this.publish = publish;
    }

    @FunctionName("hello")
    public HttpResponseMessage hello(
            @HttpTrigger(name = "request", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<String> request) {

        return request.createResponseBuilder(HttpStatus.OK)
                .body(hello.apply(request.getBody()))
                .header("Content-Type", "application/json")
                .build();
    }

    @FunctionName("time")
    public HttpResponseMessage time(
            @HttpTrigger(name = "request", methods = {HttpMethod.GET}, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<String> request) {

        return request.createResponseBuilder(HttpStatus.OK)
                .body(time.get())
                .header("Content-Type", "application/json")
                .build();
    }

    @FunctionName("publish")
    public HttpResponseMessage publish(
            @HttpTrigger(name = "request", methods = {HttpMethod.POST}, authLevel = AuthorizationLevel.ANONYMOUS)
            HttpRequestMessage<String> request) {

        publish.accept(request.getBody());

        return request.createResponseBuilder(HttpStatus.ACCEPTED).build();
    }
}
