package httpClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Request {
    private static HttpClient client;

    public static void main(String[] args) throws IOException, InterruptedException {
            client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString("login=admin&password=admin"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .uri(URI.create("http://localhost:8080"))
                    .build();
            System.out.println(request.toString());

    }
}

