package app.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FetchTools {

    public <T> T getFromApi(String uri, Class<T> dtoClass) {

        ObjectMapper objectMapper = new ObjectMapper(); // Jackson prep
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest
                    .newBuilder()
                    .header("Accept", "application/json")
                    .uri(new URI(uri))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), dtoClass);
            } else {
                System.out.println("GET request failed. Status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> getFromApiList(List<String> endpoints, Class<T> dto) {
        List<T> responses = new ArrayList<>();
        List<Callable<T>> tasks = new ArrayList<>();
        ExecutorService executorService = createThreadPool(endpoints.size());

        for (String endpoint : endpoints) {
            tasks.add(() -> getFromApi(endpoint, dto));
        }

        try {
            List<Future<T>> futures = executorService.invokeAll(tasks);

            for (Future<T> future : futures) {
                responses.add(future.get());
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            executorService.shutdown();
        }

        return responses;
    }

    private static ExecutorService createThreadPool(int threadPoolSize) {
        int cores = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(Math.min(threadPoolSize, cores));
    }
}