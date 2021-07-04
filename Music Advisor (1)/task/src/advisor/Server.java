package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server {
    public String query = "";
    public boolean responseFromAPI = false;
    HttpServer createServer() throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(5001), 0);
        server.createContext("/",
                exchange -> {
                    String response = "";
                    query = exchange.getRequestURI().getQuery();

                    if(!query.isEmpty()){
                        response = "Got the code. Return back to your program.";
                        System.out.println("code received");
                    }else{
                        response = "Authorization code not found. Try again.";
                    }
                    exchange.sendResponseHeaders(200, response.length());
                    exchange.getResponseBody().write(response.getBytes());
                    exchange.getResponseBody().close();
                });
        return server;
    }
}
