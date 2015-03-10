package org.tmf.dsmapi.subscriber;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.net.InetSocketAddress;
import java.util.List;
import org.tmf.dsmapi.subscriber.resource.Current;
import org.tmf.dsmapi.subscriber.resource.Resource;
import org.tmf.dsmapi.subscriber.resource.History;

public class Server {

    public static void main(String[] args) throws Exception {

        int port = 9000;
        if (args.length > 0) {
            port = Integer.valueOf(args[0]);
        }

        File directory = new File(Config.DIRECTORY);

        if (!directory.exists()) {
            directory.mkdir();
        }

        HttpServer server = null;
        server = HttpServer.create(new InetSocketAddress(port), 0);

        String basePath = Config.BASE_PATH;
        server.createContext(basePath + "/event", new Resource());
        server.createContext(basePath + "/history", new History());
        server.createContext(basePath + "/current", new Current());
        server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(5));
        server.start();

        System.out.println("Server started");
        System.out.println("Output directory: " + Config.DIRECTORY);
        System.out.println("Base URL: http://localhost:" + port + basePath);
        System.out.println("Operations:");
        System.out.println("  POST   /event");
        System.out.println("  GET    /history");
        System.out.println("  DELETE /history");
        System.out.println("  GET    /current");

    }

    public static void setHeaders(Headers requestHeaders, Headers responseHeaders) {
        responseHeaders.set("Content-Type", "application/json");
        responseHeaders.set("Accept", "application/json");
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        responseHeaders.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");

        if (!requestHeaders.isEmpty()) {
            if (requestHeaders.containsValue("Access-Control-Request-Headers")) {
                List<String> heads = requestHeaders.get("Access-Control-Request-Headers");
                responseHeaders.put("Access-Control-Allow-Headers", heads);
            }
        }
    }
}
