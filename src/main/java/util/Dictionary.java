package util;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Dictionary {
    private static ConcurrentHashMap<String, HashMap<String,Object>> dict = new ConcurrentHashMap<String, HashMap<String, Object>>();

    private Dictionary(){}

    private static Dictionary dictionary = new Dictionary();
    public  static Dictionary getInstance() {return dictionary;}

    public static Map<String,Object> load(String ns, Object dictionary) {
        if (dict.contains(ns))
            return dict.get(ns);

        Gson gson = new Gson();
        try {
            HashMap<String,Object> map = null;
            if (dictionary instanceof String)
                map = gson.fromJson(new FileReader(new File((String)dictionary)), HashMap.class);
            else if (dictionary instanceof Map) {
                map = new HashMap<>((LinkedTreeMap<String, Object>) dictionary);
            }
            dict.put(ns, map);

            return map;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
