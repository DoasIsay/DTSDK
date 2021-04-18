/*
 * Copyright (c) 2021, wenwu xie <870585356@qq.com>
 * All rights reserved.
 */

package util;

import com.google.gson.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;;

public class GsonHelper {
    static public <T> T get(String path, Class<T> c) {
        Gson gson = new Gson();
        try {
            return (T) gson.fromJson(new FileReader(new File(path)), c);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}