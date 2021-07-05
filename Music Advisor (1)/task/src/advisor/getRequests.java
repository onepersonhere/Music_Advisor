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
    public static List<String> Request(String link, String Category) throws IOException, InterruptedException { //use gson parser
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
    private static List<String> JsonParser(String json, String Category){
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
        return new ArrayList<>();
    }

    private static List<String> newRelease(String json){
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        jo = jo.getAsJsonObject("albums");
        List<String> list = new ArrayList<>();

        for(JsonElement item : jo.getAsJsonArray("items")){
            JsonObject object = item.getAsJsonObject();
            String name = object.get("name").getAsString();
            //artist
            JsonArray artist = object.getAsJsonArray("artists");
            String artists = "[";
            for(int i = 0; i < artist.size(); i++){
                JsonObject artistObj = artist.get(i).getAsJsonObject();
                artists += artistObj.get("name") + ", ";
            }
            artists = artists.replaceAll("\"", "");
            artists = artists.replaceAll(", $", "");
            artists += "]";
            //url
            JsonObject href = object.get("external_urls").getAsJsonObject();
            String link = href.get("spotify").getAsString();

            String returnVal = name + "\n" + artists + "\n" + link + "\n\n";
            list.add(returnVal);
        }

        return list;
    }

    private static List<String> featuredPlaylists(String json){
        List<String> list = new ArrayList<>();
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        jo = jo.getAsJsonObject("playlists");

        for(JsonElement item : jo.getAsJsonArray("items")){
            JsonObject object = item.getAsJsonObject();
            String name = object.get("name").getAsString();
            JsonObject href = object.get("external_urls").getAsJsonObject();
            String link = href.get("spotify").getAsString();

            String returnVal = name + "\n" + link + "\n\n";
            list.add(returnVal);
        }

        return list;
    }

    private static List<String> categories(String json){
        List<String> list = new ArrayList<>();
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        jo = jo.getAsJsonObject("categories");

        for(JsonElement item : jo.getAsJsonArray("items")){
            JsonObject object = item.getAsJsonObject();
            String name = object.get("name").getAsString();
            String returnVal = name + "\n";

            String id = object.get("id").getAsString();
            listOfIDs.put(name, id);
            //System.out.println(id);
            list.add(name);
        }

        return list;
    }

    private static List<String> playlists(String json){
        List<String> list = new ArrayList<>();
        JsonObject jo = JsonParser.parseString(json).getAsJsonObject();
        if(json.contains("error")){
            System.out.println(json);
            return list;
        }
        jo = jo.getAsJsonObject("playlists");

        for(JsonElement item : jo.getAsJsonArray("items")){
            JsonObject object = item.getAsJsonObject();
            String name = object.get("name").getAsString();
            JsonObject href = object.get("external_urls").getAsJsonObject();
            String link = href.get("spotify").getAsString();
            String returnVal = name + "\n" + link + "\n\n";
            list.add(returnVal);
        }

        return list;
    }

}
