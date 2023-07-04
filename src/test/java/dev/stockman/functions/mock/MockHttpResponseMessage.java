package dev.stockman.functions.mock;

import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatusType;

import java.util.HashMap;
import java.util.Map;

public class MockHttpResponseMessage implements HttpResponseMessage {
    private final HttpStatusType httpStatus;
    private final Object body;
    private final Map<String, String> headers;

    public MockHttpResponseMessage(HttpStatusType status, Map<String, String> headers, Object body) {
        this.httpStatus = status;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public HttpStatusType getStatus() {
        return this.httpStatus;
    }

    @Override
    public int getStatusCode() {
        return getStatus().value();
    }

    @Override
    public String getHeader(String key) {
        return this.headers.get(key);
    }

    @Override
    public Object getBody() {
        return this.body;
    }

    public static class MockHttpResponseMessageBuilder implements HttpResponseMessage.Builder {
        private Object body;
        private final Map<String, String> headers = new HashMap<>();
        private HttpStatusType httpStatus;

        @Override
        public Builder status(HttpStatusType httpStatusType) {
            this.httpStatus = httpStatusType;
            return this;
        }

        @Override
        public HttpResponseMessage.Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        @Override
        public HttpResponseMessage.Builder body(Object body) {
            this.body = body;
            return this;
        }

        @Override
        public HttpResponseMessage build() {
            return new MockHttpResponseMessage(this.httpStatus, this.headers, this.body);
        }
    }
}
