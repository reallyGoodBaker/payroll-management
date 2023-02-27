package service;

import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import network.ApiHandler;
import persistence.Store;
import utils.HttpUtils;

public class Initialize implements ApiHandler {

    private StaffList staffList;
    private Map<String, ApiHandler> handlers = new HashMap<>(5);

    public void init(Store store) {
        staffList = StaffList.getList(store, "staff-list");

        handlers.put("all", new All(staffList));
        handlers.put("put", new Put(staffList, store));
        handlers.put("delete", new Delete(staffList, store));
        handlers.put("query", new Query(staffList));
    }

    @Override
    public void handle(HttpExchange exchange) {
        String uri = exchange
            .getRequestURI()
            .toString()
            .substring("/api/".length());

        HttpUtils.debugInfo("<= " + uri);

        ApiHandler handler;
        if ((handler = handlers.get(uri)) != null) {
            handler.handle(exchange);
        } else {
            exchange.close();
        }

    }
    
}
