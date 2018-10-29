package com.hzlf.sampletest.others;

import java.lang.reflect.Type;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hzlf.sampletest.entityclass.Source;
import com.hzlf.sampletest.entityclass.User;

public class GsonTools {

    public GsonTools() {

    }

    /*
     * // 将Json数据解析成相应的映射对象 public static <T> T parseJsonWithGson(String
     * jsonData, Class<T> type) { Gson gson = new Gson(); T result =
     * gson.fromJson(jsonData, type); return result; }
     */

    public static LinkedList<User> getUsers(String result) {

        try {
            Type listType = new TypeToken<LinkedList<User>>() {
            }.getType();
            Gson gson = new Gson();

            LinkedList<User> users = gson.fromJson(result, listType);
            return users;
        } catch (Exception e) {
        }
        return null;
    }

    public static LinkedList<Source> getAllSource(String result) {

        try {
            Type listType = new TypeToken<LinkedList<Source>>() {
            }.getType();
            Gson gson = new Gson();
            LinkedList<Source> source = gson.fromJson(result, listType);
            return source;
        } catch (Exception e) {
        }
        return null;
    }

}