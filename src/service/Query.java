package service;

import java.util.Map;
import java.util.UUID;

import com.sun.net.httpserver.HttpExchange;

import network.ApiHandler;
import utils.HttpUtils;
import utils.Tries;

public class Query implements ApiHandler {

    private StaffList staffList;

    public Query(StaffList staffList) {
        this.staffList = staffList;
    }

    @Override
    public void handle(HttpExchange exchange) {
        HttpUtils.addJsonHeader(exchange);

        Tries.flow(Void -> {
            Map<String, String> query = HttpUtils.parseQuery(new String(exchange.getRequestBody().readAllBytes()));
            String searchValue = query.get("s");

            StaffList uidRes = new StaffList(UUID.randomUUID().toString());
            StaffList nameRes = new StaffList(UUID.randomUUID().toString());

            for (Staff staff : staffList) {
                String uid = staff.getUid();
                String name = staff.getName();

                if (uid.contains(searchValue)) {
                    uidRes.add(staff);
                }

                if (name.contains(searchValue)) {
                    nameRes.add(staff);
                }
            }

            byte[] resultJson = (
                "{\"uid\":" + uidRes.toJson() +
                ",\"name\":" + nameRes.toJson() +
                "}"
            ).getBytes();

            HttpUtils.debugInfo(" => " + new String(resultJson));

            exchange.sendResponseHeaders(200, resultJson.length);
            exchange.getResponseBody().write(resultJson);

            return null;
        })
        .start();

        exchange.close();
    }
    
}
