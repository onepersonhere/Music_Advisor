package advisor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import javax.management.timer.Timer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class Main {
    private static String access_token = "";
    private static boolean auth = false;
    public static final String client_id = "d644546874e74c11af7ebbc3b04608c7";
    public static final String client_secret = "7050d04f770c4b84873a83857286b995";
    public static final String redirect_uri = "http://localhost:5001&response_type=code";
    public static String server_path = "https://accounts.spotify.com";
    private static final String auth_link = server_path+"/authorize?client_id="+client_id+"&redirect_uri="+redirect_uri;
    private static HttpServer server;
    private static Server httpserver;

    public static void main(String[] args) throws IOException, InterruptedException {
        for(int i = 0; i < args.length; i+=2){
            if(args[i].equals("-access")){
                server_path = args[i+1];
            }
        }
        commandProcessor();
    }
    private static void createServer() throws IOException {
        httpserver= new Server();
        server = httpserver.createServer();
    }

    private static void commandProcessor() throws IOException, InterruptedException {
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
                createServer();
                server.start();

                System.out.println("use this link to request the access code:");
                System.out.println(auth_link);
                Authentication authentication = new Authentication(auth_link, httpserver);
                if(authentication.responseCode == 200) {
                    String query = "";
                    while (query.equals("")) {
                        query = httpserver.query;
                        //System.out.println(query);
                        Thread.sleep(1000);
                    }
                    query = query.split("=")[1];
                    System.out.println("making http request for access_token...");
                    AccessToken accessToken = new AccessToken(query);
                    auth = true;
                    System.out.println("---SUCCESS---");
                }
            }
            if (action.equals("exit")) {
                System.out.println("---GOODBYE!---");
                break;
            }
        }
    }
}
