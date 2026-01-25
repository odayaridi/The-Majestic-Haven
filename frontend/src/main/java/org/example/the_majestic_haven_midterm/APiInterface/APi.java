package org.example.the_majestic_haven_midterm.APiInterface;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class APi {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static String executeAPI(String requestType
, String targetURL, String jsonPayload, String token) {
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(targetURL))
                    .header("Accept", "application/json");

            if (token != null && !token.isEmpty()) {
                requestBuilder.header("Authorization", "Bearer " + token);
            }

            switch (requestType.toUpperCase()) {
                case "GET":
                    requestBuilder.GET();
                    break;
                case "POST":
                    requestBuilder.header("Content-Type", "application/json");
                    if (jsonPayload != null) {
                        requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonPayload));
                    } else {
                        requestBuilder.POST(HttpRequest.BodyPublishers.noBody());
                    }
                    break;
                case "PUT":
                    requestBuilder.header("Content-Type", "application/json");
                    if (jsonPayload != null) {
                        requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(jsonPayload));
                    } else {
                        requestBuilder.PUT(HttpRequest.BodyPublishers.noBody());
                    }
                    break;
                case "DELETE":
                    requestBuilder.DELETE();
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported HTTP method: " + requestType);
            }

            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if (statusCode == 200 || statusCode == 201 || statusCode == 204) {
               return response.body();
            } else {
                System.err.println("HTTP error: " + statusCode + " - " + response.body());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
