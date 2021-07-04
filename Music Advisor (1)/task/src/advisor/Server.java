package advisor;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    public String query = "";
    public boolean responseFromAPI = false;
    public static boolean stopServer = false;
    HttpServer createServer() throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(5001), 0);
        server.createContext("/",
                exchange -> {
                    String response = "";
                    query = exchange.getRequestURI().getQuery();
                    //System.out.println(query);
                    if(query == null || query.equals("error=access_denied")){
                        query = "";
                    }
                    if(!query.equals("")){
                        response = "Got the code. Return back to your program.";
                        System.out.println("code received");
                        stopServer = true;
                    }else{
                        response = "Authorization code not found. Try again.";
                    }
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();

                    if(stopServer){
                        server.stop(0);
                    }
                });
        return server;
    }
}
