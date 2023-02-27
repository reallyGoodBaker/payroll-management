package service;

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import network.ApiHandler;
import persistence.Store;
import utils.HttpUtils;
import utils.Tries;

public class Delete implements ApiHandler {

    private StaffList staffList;
    private Store store;

    public Delete(StaffList staffList, Store store) {
        this.staffList = staffList;
        this.store = store;
    }

    @Override
    public void handle(HttpExchange exchange) {
        Tries.flow(Void -> {
            Map<String, String> query = HttpUtils.parseQuery(new String(exchange.getRequestBody().readAllBytes()));

            for (Staff staff : staffList) {
                if (staff.getUid().equals(query.get("uid"))) {
                    staffList.remove(staff);
                    staffList.saveList(store);
                }
            }

            exchange.sendResponseHeaders(200, 0);

            return null;
        })
        .start();
        
        exchange.close();
    }
    
}
