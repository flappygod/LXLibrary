/*
 * Copyright 2013 The JA-SIG Collaborative. All rights reserved.
 * distributed with this file and available online at
 * http://www.etong.com/
 */
package com.flappygod.lipo.lxlibrary.Tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author:李俊霖
 * @version:2015-5-6下午2:26:43
 * @since 1.0
 */
public class JSONTool {


    /****************
     * 转换对象为JSON
     * @param object 对象
     * @return
     */
    public static String ModelToJson(Object object) {
        //装换
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        return gson.toJson(object);
    }


    /*************
     * JOSN转换为hashmap
     *
     * @param Str json字符串
     * @return
     * @throws
     */
    public static HashMap<String, String> JSONStrToMap(String Str) {
        HashMap<String, String> map = new HashMap<String, String>();
        JSONObject jb;
        try {
            jb = new JSONObject(Str);
            Iterator<String> it = jb.keys();
            while (it.hasNext()) {
                String key = it.next();
                map.put(key, jb.getString(key));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return map;

    }

    /****************
     * 获取这个JSON中的所有value值
     *
     * @param str  json字符串
     * @return
     * @throws
     */
    public static List<String> JSONValueList(String str) {
        List<String> ret = new ArrayList<String>();
        JSONObject jb;
        try {
            jb = new JSONObject(str);
            Iterator<String> it = jb.keys();
            while (it.hasNext()) {
                String key = it.next();
                ret.add(jb.getString(key));
            }
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }

        return ret;

    }

    /************
     * 获取这个JSON中的所有key值
     *
     * @param str  json字符串
     * @return
     * @throws
     */
    public static List<String> JSONKeyList(String str) {
        List<String> ret = new ArrayList<String>();
        JSONObject jb;
        try {
            jb = new JSONObject(str);
            Iterator<String> it = jb.keys();
            while (it.hasNext()) {
                String key = it.next();
                ret.add(key);
            }
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }

        return ret;

    }

    /***************************
     * @param hash hashMap
     * @return
     */
    public static JSONObject LinkedMapToJson(LinkedTreeMap hash) {
        JSONObject jb = new JSONObject();
        try {
            Iterator<String> keys = hash.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                if (hash.get(key) instanceof HashMap) {
                    jb.put(key, HashMapToJson((HashMap) hash.get(key)));
                } else if (hash.get(key) instanceof LinkedTreeMap) {
                    jb.put(key, LinkedMapToJson((LinkedTreeMap) hash.get(key)));
                } else if (hash.get(key) instanceof ArrayList) {
                    JSONArray array = ArrayListToJson((ArrayList) hash.get(key));
                    jb.put(key, array);
                } else {
                    jb.put(key, hash.get(key));
                }
            }
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
        return jb;
    }


    /*************
     * hashMap转换为JSon对象
     * @param hash
     * @return
     */
    public static JSONObject HashMapToJson(HashMap hash) {
        JSONObject jb = new JSONObject();
        try {
            Iterator<String> keys = hash.keySet().iterator();
            while (keys.hasNext()) {
                String key = keys.next();
                if (hash.get(key) instanceof HashMap) {
                    jb.put(key, HashMapToJson((HashMap) hash.get(key)));
                } else if (hash.get(key) instanceof LinkedTreeMap) {
                    jb.put(key, LinkedMapToJson((LinkedTreeMap) hash.get(key)));
                } else if (hash.get(key) instanceof ArrayList) {
                    JSONArray array = ArrayListToJson((ArrayList) hash.get(key));
                    jb.put(key, array);
                } else {
                    jb.put(key, hash.get(key));
                }
            }
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        }
        return jb;
    }


    /********
     * 列表转换为jsonArray
     * @param list
     * @return
     */
    private static JSONArray ArrayListToJson(ArrayList list) {
        List datalist = (ArrayList) list;
        JSONArray array = new JSONArray();
        for (int s = 0; s < datalist.size(); s++) {
            if (datalist.get(s) instanceof LinkedTreeMap) {
                array.put(LinkedMapToJson((LinkedTreeMap) datalist.get(s)));
            } else if (datalist.get(s) instanceof ArrayList) {
                array.put(ArrayListToJson((ArrayList) datalist.get(s)));
            } else {
                array.put(datalist.get(s));
            }
        }
        return array;
    }


    /****************
     * 转换JSONString 为object
     *
     * @param str
     * @return
     * @throws
     */
    public static JSONObject JSONObjectFromStr(String str) {
        try {
            return new JSONObject(str);
        } catch (JSONException e) {
            return null;
        }
    }

}
