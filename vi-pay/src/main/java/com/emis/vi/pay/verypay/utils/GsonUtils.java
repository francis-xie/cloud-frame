package com.emis.vi.pay.verypay.utils;

import com.alibaba.fastjson.JSON;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.sf.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * $Id$
 * 2018/02/02 Francis.xie Modify 需求 #42028 [客制-宜芝多]费睿
 */
public class GsonUtils {

  public static Gson getGson() {
    Type type = new TypeToken<Map<String, Object>>() {
    }.getType();
    return new GsonBuilder().registerTypeAdapter(type, new JsonDeserializer<TreeMap<String, Object>>() {
      @Override
      public TreeMap<String, Object> deserialize(JsonElement json, Type typeOfT,
                                                 JsonDeserializationContext context) throws JsonParseException {
        TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
        JsonObject jsonObject = json.getAsJsonObject();
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
          treeMap.put(entry.getKey(), entry.getValue().getAsString());
        }
        return treeMap;
      }
    }).create();
  }

  /**
   * @param map
   * @return
   * @Title: parseMapToJson
   * @Description:map转json，map为string，Object类型
   */
  public static String parseMapObjToJson(Map<String, Object> map) {
    if (map == null) {
      return null;
    }
    String json = null;
    JSONObject jsonObject = JSONObject.fromObject(map);
    json = jsonObject.toString();
    return json;
  }

  public static Map<String, Object> paseJsonToMap(String json) {
    return JSON.parseObject(json, Map.class);
  }
}