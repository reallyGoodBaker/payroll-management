package network;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {
    public static HttpServer listen(int port, WebHandler handler) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(port), 0);

        httpServer.createContext("/", handler);
        httpServer.setExecutor(Executors.newFixedThreadPool(10));
        httpServer.start();

        System.out.println("服务器开启  " + "http://localhost:" + Integer.toString(port));

        return httpServer;
    }
}
