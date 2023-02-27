package service;

import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

import network.ApiHandler;
import persistence.Store;
import utils.HttpUtils;
import utils.Tries;

public class Put implements ApiHandler {

    private StaffList staffList;
    private Store store;

    public Put(StaffList staffList, Store store) {
        this.staffList = staffList;
        this.store = store;
    }

    @Override
    public void handle(HttpExchange exchange) {
        Tries.flow(Void -> {
            Map<String, String> query = HttpUtils.parseQuery(new String(exchange.getRequestBody().readAllBytes()));

            Staff target = null;
            String uid = query.get("uid");

            for (Staff staff : staffList) {
                if (staff.getUid().equals(uid)) {
                    target = staff;
                    break;
                }
            }

            if (target == null) {
                target = new Staff(query.get("uid"));
                staffList.add(target);
            }

            editStaff(target, query);
            staffList.saveList(store);

            exchange.sendResponseHeaders(200, 0);

            return null;
        })
        .start();

        exchange.close();
    }

    private void editStaff(Staff staff, Map<String, String> record) {
        staff.setName(record.get("name"));
        staff.setPrimaryRole(RoleFactory.build(
            Integer.parseInt(record.get("primary"))
        ));
        staff.setSecondaryRole(
            !record.get("secondary").equals("null")
                ? RoleFactory.build(Integer.parseInt(record.get("secondary")))
                : null
        );
        staff.setWorkload(
            Integer.parseInt(record.get("workload"))
        );
    }
    
}
