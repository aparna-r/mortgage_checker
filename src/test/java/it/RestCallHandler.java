package it;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class RestCallHandler {
    private final static ThreadLocal<ResponseEntity<String>> lastResponse = new ThreadLocal<>();
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public RestCallHandler() {
        this.restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new NoOpResponseErrorHandler());
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    protected void executeGetEntity(String url) {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        lastResponse.set(response);
    }

    protected void executePostEntity(String url, Object body) {
        ResponseEntity<String> response = restTemplate.postForEntity(url, body, String.class);
        lastResponse.set(response);
    }

    protected int getLastResponseHttpStatus() {
        return lastResponse.get().getStatusCode().value();
    }

    @SneakyThrows
    protected <T> T getLastResponse(Class<T> responseType) {
        return objectMapper.readValue(lastResponse.get().getBody(), responseType);
    }

    private static class NoOpResponseErrorHandler extends DefaultResponseErrorHandler {
        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            // Do nothing, preventing RestTemplate from throwing exceptions on errors
        }
    }
}
