import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class WeatherClient {
    private final HttpClient client;

    WeatherClient() {
        client = HttpClient.newHttpClient();
    }

    public String getWeatherData(String city) {
        URI uri = URI.create("https://functions.yandexcloud.net/d4eo3a1nvqedpic89160?scale=C&city=" + city);

        HttpRequest request = HttpRequest
                .newBuilder()
                .GET()
                .uri(uri)
                .setHeader("Access", "application/json")
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return "Что-то пошло не так. Сервер вернул код состояния: " + response.statusCode();
            }

            JsonElement jsonElement = JsonParser.parseString(response.body());
            if (jsonElement.isJsonNull()) {
                throw new InterruptedException();
            }

            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject == null) {
                throw new InterruptedException();
            }

            if (jsonObject.get("cities").isJsonNull()) {
                throw new InterruptedException();
            }

            JsonObject cities = jsonObject.get("cities").getAsJsonObject();
            if (cities == null) {
                throw new InterruptedException();
            }

            if (cities.get(city).isJsonNull()) {
                throw new InterruptedException();
            }

            JsonObject cityObject = cities.get(city).getAsJsonObject();
            if (cityObject == null) {
                throw new InterruptedException();
            }

            return "Город: " + cityObject.get("city").getAsString() + ". " + cityObject.get("conditions").getAsString() + ", " + cityObject.get("temperature").getAsString();

        } catch (IOException | InterruptedException e) {
            return "Во время выполнения запроса возникла ошибка.\n Проверьте, пожалуйста, параметры запроса и повторите попытку.";
        }

    }

}
