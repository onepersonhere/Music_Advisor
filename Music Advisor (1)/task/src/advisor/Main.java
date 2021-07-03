package advisor;

import httpClient.Request;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {
    private static String access_token = "";
    private static boolean auth = false;
    private static final String client_id = "d644546874e74c11af7ebbc3b04608c7";
    private static final String client_secret = "7050d04f770c4b84873a83857286b995";
    private static final String redirect_uri = "http://localhost:5001&response_type=code";
    private static final String auth_link = "https://accounts.spotify.com/authorize?client_id="+client_id+"&redirect_uri="+redirect_uri;
    private static HttpServer server;

    public static void main(String[] args) throws IOException {
        createServer();
        server.start();
    }

    private static void createServer() throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(5001), 0);
        server.createContext("/",
                new HttpHandler() {
                    @Override
                    public void handle(HttpExchange exchange) throws IOException {
                        String query = exchange.getRequestURI().getQuery();
                        exchange.sendResponseHeaders(200, query.length());
                        exchange.getResponseBody().write(query.getBytes());
                        exchange.getResponseBody().close();
                    }
                });
    }
    private static void commandProcessor(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            String action = scanner.nextLine();
            String[] actionArr = action.split(" ");
            if (action.equals("new")) {
                if(auth){
                    System.out.println("---NEW RELEASES---\n" +
                            "Mountains [Sia, Diplo, Labrinth]\n" +
                            "Runaway [Lil Peep]\n" +
                            "The Greatest Show [Panic! At The Disco]\n" +
                            "All Out Life [Slipknot]");
                }else{
                    System.out.println("Please, provide access for application.");
                }

            }
            if (action.equals("featured")) {
                if(auth) {
                    System.out.println("---FEATURED---\n" +
                            "Mellow Morning\n" +
                            "Wake Up and Smell the Coffee\n" +
                            "Monday Motivation\n" +
                            "Songs to Sing in the Shower");
                }else{
                    System.out.println("Please, provide access for application.");
                }
            }
            if (action.equals("categories")) {
                if(auth) {
                    System.out.println("---CATEGORIES---\n" +
                            "Top Lists\n" +
                            "Pop\n" +
                            "Mood\n" +
                            "Latin");
                }else{
                    System.out.println("Please, provide access for application.");
                }
            }
            if (actionArr[0].equals("playlists")) {
                if(auth) {
                    System.out.println("---" + actionArr[1] + " PLAYLISTS---\n" +
                            "Walk Like A Badass  \n" +
                            "Rage Beats  \n" +
                            "Arab Mood Booster  \n" +
                            "Sunday Stroll\n" +
                            "> exit");
                }else{
                    System.out.println("Please, provide access for application.");
                }
            }
            if(action.equals("auth")){
                System.out.println(auth_link);
                auth = true;
                System.out.println("---SUCCESS---");
            }
            if (action.equals("exit")) {
                System.out.println("---GOODBYE!---");
                break;
            }
        }
    }
}
