package persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Storage {
    private static Map<String, Store> record = new HashMap<>();

    public static Store open(String filePath) throws IOException, ClassNotFoundException {

        Store store;

        if ((store = record.get(filePath)) != null) {
            return store;
        }

        store = getNewStore(filePath);
        record.put(filePath, store);

        return store;
    }

    private static Store getNewStore(String filePath) throws IOException {
        File target = new File(filePath);
        
        if (!target.exists()) {
            target.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(target);
            fw.write("");
            fw.close();
        }

        return new StoreImpl(filePath);
    }
}
