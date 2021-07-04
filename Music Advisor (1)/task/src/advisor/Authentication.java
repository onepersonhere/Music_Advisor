package advisor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class Authentication {
    public int responseCode;
    Authentication(String link, Server server) throws IOException, InterruptedException {
        URL url = new URL(link);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        System.out.println("waiting for code...");
        //System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        responseCode = http.getResponseCode();
        if(http.getResponseCode() == 200){
            server.responseFromAPI = true;
        }
        http.disconnect();
    }
}
