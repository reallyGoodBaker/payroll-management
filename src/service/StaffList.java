package service;

import java.util.ArrayList;

import persistence.Store;
import utils.ArrayUtils;

public class StaffList extends ArrayList<Staff> {

    final private String name;

    public StaffList(String name) {
        super();

        this.name = name;
    }

    public void saveList(Store store) {
        store.set(name, this);
        store.commit();
    }

    public static StaffList getList(Store store, String name) {
        StaffList list = (StaffList) store.get(name);

        if (list == null) {
            return new StaffList("staff-list");
        }

        return list;
    }

    public String toJson() {
        ArrayList<String> list = new ArrayList<>();

        for (Staff staff : this) {
            list.add(staff.toJson());
        }

        return "[" + ArrayUtils.join(list.toArray(new String[0]), ",") + "]";
    }

}
