package net.ilexiconn.llibrary.server.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ListHashMap<K, V> extends LinkedHashMap<K, V> {
    public V getValue(int index) {
        Map.Entry<K, V> entry = this.getEntry(index);
        if (entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public Map.Entry<K, V> getEntry(int index) {
        Set<Map.Entry<K, V>> entries = entrySet();
        int j = 0;
        for (Map.Entry<K, V> entry : entries) {
            if (j++ == index) {
                return entry;
            }
        }
        return null;
    }
}

