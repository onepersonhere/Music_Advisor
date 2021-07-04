package advisor;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AccessToken {
    AccessToken(String query) throws IOException, InterruptedException {
        String input = Main.client_id+":"+Main.client_secret;
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic "+ Base64.getEncoder().encodeToString(input.getBytes()))
                .uri(URI.create(Main.server_path+"/api/token"))
                .POST(HttpRequest.BodyPublishers.ofString(
                        "grant_type=authorization_code" +
                                "&code="+query
                                +"&redirect_uri="+"http://localhost:5001"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }
}
