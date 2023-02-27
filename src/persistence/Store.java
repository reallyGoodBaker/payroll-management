package persistence;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

import utils.Tries;

public interface Store {
    String getFilePath();
    void set(String key, Object val);
    boolean delete(String k);
    Object get(String key);
    Set<String> keys();
    void commit();
}

class StoreImpl implements Store {

    private String filePath;
    private HashMap<String, Object> map = new HashMap<>();

    StoreImpl(String filePath) {
        this.filePath = filePath;

        Tries.flow(Void -> {
            initialize(
                new ObjectInputStream(
                    new FileInputStream(filePath)
                )
            );
            return null;
        })
        .then(Void -> {
            writeMap();
            return null;
        })
        .caught(Void -> null)
        .start();
    }

    private void initialize(ObjectInputStream in) {
        Tries.flow(Void -> {
            while (true) {
                Pair p = (Pair) in.readObject();
                map.put(p.key(), p.val());
            }
        })
        .caught((e) -> {
            if (e instanceof EOFException) {
                in.close();
                return null;
            }

            return e;
        })
        .start();
    }

    public String getFilePath() {
        return filePath;
    }

    public Object get(String key) {
        return map.get(key);
    }

    private void writeMap() {
        Tries.flow(Void -> {
            return new ObjectOutputStream(new FileOutputStream(filePath));
        })
        .then((_out) -> {
            ObjectOutputStream out = (ObjectOutputStream) _out;

            for (Entry<String, Object> entry : map.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                out.writeObject(new Pair(key, val));
            }

            out.close();

            return null;
        })
        .start();
    }

    public void set(String key, Object val) {
        map.put(key, val);
    }

    public boolean delete(String key) {
        if (!map.containsKey(key)) {
            return false;
        }

        map.remove(key);
        return true;
    }

    public void commit() {
        writeMap();
    }

    public Set<String> keys() {
        return map.keySet();
    }

}
