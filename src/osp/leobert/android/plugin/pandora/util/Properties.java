package osp.leobert.android.plugin.pandora.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * <p><b>Package:</b> osp.leobert.android.plugin.pandora.util </p>
 * <p><b>Project:</b> Pandora-Plugin </p>
 * <p><b>Classname:</b> PropertyUtil </p>
 * <p><b>Description:</b> TODO </p>
 * Created by leobert on 2019/4/28.
 */
public class Properties {

    private final java.util.Properties properties;

    private Properties(File propFile) throws IOException {
        properties = new java.util.Properties();
        properties.load(new FileInputStream(propFile));
    }

    public boolean containsKey(final Object key) {
        return properties.containsKey(key);
    }

    public boolean containsValue(final Object value) {
        return properties.containsValue(value);
    }

    public Set<Map.Entry<Object, Object>> entrySet() {
        return properties.entrySet();
    }

    public String getProperty(final String name) {
        return properties.getProperty(name);
    }

    public String getProperty(final String name, final String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

    public boolean isEmpty() {
        return properties.isEmpty();
    }

    public Enumeration<Object> keys() {
        return properties.keys();
    }

    public Set<Object> keySet() {
        return properties.keySet();
    }

    public int size() {
        return properties.size();
    }

    public Collection<Object> values() {
        return properties.values();
    }

    public static Properties newInstance(File propFile) throws IOException {
        return new Properties(propFile);
    }
}
