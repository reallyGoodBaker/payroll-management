package network;

import com.sun.net.httpserver.HttpExchange;

public interface ApiHandler {
    void handle(HttpExchange exchange);
}
