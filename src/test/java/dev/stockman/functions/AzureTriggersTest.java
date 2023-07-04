package dev.stockman.functions;

import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import dev.stockman.functions.mock.MockHttpResponseMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.Optional;

class AzureTriggersTest {

    private AzureTriggers systemUnderTest = new AzureTriggers(
            (a) -> a,
            () -> "a",
            (a) -> {}
    );

    @Test
    void hello() {
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<String> mockRequest = Mockito.mock(HttpRequestMessage.class);

        final String body = "hello";
        Mockito.doReturn(body).when(mockRequest).getBody();

        Mockito.doAnswer((Answer<HttpResponseMessage.Builder>) invocation -> {
            HttpStatus status = (HttpStatus) invocation.getArguments()[0];
            return new MockHttpResponseMessage.MockHttpResponseMessageBuilder().status(status);
        }).when(mockRequest).createResponseBuilder(Mockito.any(HttpStatus.class));

        HttpResponseMessage result = systemUnderTest.hello(mockRequest);
        Assertions.assertEquals("hello", result.getBody());
        Assertions.assertEquals(200, result.getStatusCode());
    }

    @Test
    void time() {
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<String> mockRequest = Mockito.mock(HttpRequestMessage.class);

        Mockito.doAnswer((Answer<HttpResponseMessage.Builder>) invocation -> {
            HttpStatus status = (HttpStatus) invocation.getArguments()[0];
            return new MockHttpResponseMessage.MockHttpResponseMessageBuilder().status(status);
        }).when(mockRequest).createResponseBuilder(Mockito.any(HttpStatus.class));

        HttpResponseMessage result = systemUnderTest.time(mockRequest);
        Assertions.assertEquals("a", result.getBody());
        Assertions.assertEquals(200, result.getStatusCode());
    }

    @Test
    void publish() {
        @SuppressWarnings("unchecked")
        final HttpRequestMessage<String> mockRequest = Mockito.mock(HttpRequestMessage.class);

        final String body = "hello";
        Mockito.doReturn(body).when(mockRequest).getBody();

        Mockito.doAnswer((Answer<HttpResponseMessage.Builder>) invocation -> {
            HttpStatus status = (HttpStatus) invocation.getArguments()[0];
            return new MockHttpResponseMessage.MockHttpResponseMessageBuilder().status(status);
        }).when(mockRequest).createResponseBuilder(Mockito.any(HttpStatus.class));

        HttpResponseMessage result = systemUnderTest.publish(mockRequest);
        Assertions.assertNull(result.getBody());
        Assertions.assertEquals(202, result.getStatusCode());
    }
}