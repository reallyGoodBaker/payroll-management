package service;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;

import network.ApiHandler;
import utils.HttpUtils;

public class All implements ApiHandler {

    private StaffList staffList;

    public All(StaffList staffList) {
        this.staffList = staffList;
    }

    @Override
    public void handle(HttpExchange exchange) {
        HttpUtils.addJsonHeader(exchange);
        byte[] body = staffList.toJson().getBytes();
        HttpUtils.debugInfo(" => " + new String(body));
        try {
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            exchange.close();
        }
    }
    
}
