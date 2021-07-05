package advisor;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class getRequests {
    public static Map<String, String> listOfIDs = new HashMap<>();
    public static String Request(String link, String Category) throws IOException, InterruptedException { //use gson parser
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .header("Authorization", "Bearer " + AccessToken.accessToken)
                .uri(URI.create(link))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return JsonParser(response.body(), Category);
    }

    public static String requestID(String category) throws IOException, InterruptedException {
        Request(Main.api_path + "/v1/browse/categories", "categories");
        boolean isCatequalKey = false;
        for (Map.Entry<String, String> entry : listOfIDs.entrySet()) {
            String key = entry.getKey();
            if(category.equals(key)){
                category = entry.getValue();
                isCatequalKey = true;
                //System.out.println(key + " " + category);
                break;
            }
        }
        if(isCatequalKey){
            //System.out.println("cat: " + category);
            HttpClient client = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .header("Authorization", "Bearer " + AccessToken.accessToken)
                    .uri(URI.create(Main.api_path+"/v1/browse/categories/" + category))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String res = response.body();
            //System.out.println(res);
            if(res.contains("error")){
                return "Specified id doesn't exist";
            }else {
                return category;
            }
        }
        return "Unknown category name.";
    }
    private static String JsonParser(String json, String Category){
        if(Category.equals("new")){
            return newRelease(json);
        }
        if(Category.equals("featured")){
            return featuredPlaylists(json);
        }
        if(Category.equals("categories")){
            return categories(json);
        }
        if(Category.equals("playlist")){
            return playlists(json);
        }
        return "";
    }

    private static String newRelease(String json){
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        jo = jo.getAsJsonObject("albums");
        String returnVal = "";

        for(JsonElement item : jo.getAsJsonArray("items")){
            JsonObject object = item.getAsJsonObject();
            String name = object.get("name").getAsString();

            JsonArray artist = object.getAsJsonArray("artists");
            String artists = "[";
            for(int i = 0; i < artist.size(); i++){
                JsonObject artistObj = artist.get(i).getAsJsonObject();
                artists += artistObj.get("name") + ", ";
            }

            artists = artists.replaceAll("\"", "");
            artists = artists.replaceAll(", $", "");
            artists += "]";

            JsonObject href = object.get("external_urls").getAsJsonObject();
            String link = href.get("spotify").getAsString();

            returnVal += name + "\n" + artists + "\n" + link + "\n\n";
        }

        return returnVal;
    }

    private static String featuredPlaylists(String json){
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        jo = jo.getAsJsonObject("playlists");
        String returnVal = "";
        for(JsonElement item : jo.getAsJsonArray("items")){
            JsonObject object = item.getAsJsonObject();
            String name = object.get("name").getAsString();
            JsonObject href = object.get("external_urls").getAsJsonObject();
            String link = href.get("spotify").getAsString();

            returnVal += name + "\n" + link + "\n\n";
        }

        return returnVal;
    }

    private static String categories(String json){
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        jo = jo.getAsJsonObject("categories");
        String returnVal = "";

        for(JsonElement item : jo.getAsJsonArray("items")){
            JsonObject object = item.getAsJsonObject();
            String name = object.get("name").getAsString();
            returnVal += name + "\n";

            String id = object.get("id").getAsString();
            listOfIDs.put(name, id);
            //System.out.println(id);
        }

        return returnVal;
    }

    private static String playlists(String json){

        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        if(json.contains("error")){
            System.out.println(json);
            return "";
        }
        jo = jo.getAsJsonObject("playlists");

        String returnVal = "";

        for(JsonElement item : jo.getAsJsonArray("items")){
            JsonObject object = item.getAsJsonObject();
            String name = object.get("name").getAsString();
            JsonObject href = object.get("external_urls").getAsJsonObject();
            String link = href.get("spotify").getAsString();
            returnVal += name + "\n" + link + "\n\n";
        }

        return returnVal;
    }

}
