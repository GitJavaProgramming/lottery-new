package org.pp.util;

/**
 * ************自强不息************
 * 参考
 * https://blog.csdn.net/weixin_44747933/article/details/108301626
 * https://juejin.cn/post/6844904166809157639
 *
 * @author 鹏鹏
 * @date 2022/9/23 20:34
 * ************厚德载物************
 **/

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Jackson工具类
 */
public final class JacksonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static byte[] toByteArray(Object data) {
        try {
            byte[] bytes = mapper.writeValueAsBytes(data);
            return bytes;
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e);
        }
    }

    public static String toJson(Object data) {
        try {
            String result = mapper.writeValueAsString(data);
            return result;
        } catch (JsonProcessingException e) {
            throw new JsonParseException(e);
        }
    }

    public static <T> T toBean(String jsonData, Class<T> beanType) {
        try {
            T result = mapper.readValue(jsonData, beanType);
            return result;
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public static <T> List<T> toList(String jsonData, Class<T> beanType) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> resultList = mapper.readValue(jsonData, javaType);
            return resultList;
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public static <K, V> Map<K, V> toMap(String jsonData, Class<K> keyType, Class<V> valueType) {
        JavaType javaType = mapper.getTypeFactory().constructMapType(Map.class, keyType, valueType);

        try {
            Map<K, V> resultMap = mapper.readValue(jsonData, javaType);
            return resultMap;
        } catch (Exception e) {
            throw new JsonParseException(e);
        }
    }

    public static Map<String, Object> jsonToMap(String json) {
        return nativeRead(json, new TypeReference<>() {
        });
    }

    public static List<Map<String, Object>> jsonToMapList(String json) {
        return nativeRead(json, new TypeReference<>() {
        });
    }

    public static <T> T objectToPojo(Object obj, Class<T> clazz) {
        return mapper.convertValue(obj, clazz);
    }

    private static <T> T nativeRead(String json, TypeReference<T> type) {
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }

    public static <T> T nativeRead(String json, Class<T> clazz) {
//        TypeReference<T> objectTypeReference = new TypeReference<>() {};
        try {
            return mapper.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }

    public static <T> T nativeRead(InputStream in, Class<T> clazz) {
        try {
            return mapper.readValue(in, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new JsonParseException(e);
        }
    }

    // 不要把json解析库的代码与业务逻辑混一起
//    public static JsonNode getNode(String json) {
//        try {
//            JsonNode jsonNode = mapper.readTree(json);
//            return jsonNode;
//        } catch (JsonProcessingException e) {
//            throw new JsonParseException(e);
//        }
//        return null;
//    }
}
