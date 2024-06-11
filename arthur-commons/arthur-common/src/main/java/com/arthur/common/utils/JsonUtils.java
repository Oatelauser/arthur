package com.arthur.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT;
import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * jackson util
 *
 * @author DearYang
 * @date 2022-07-13
 * @since 1.0
 */
@SuppressWarnings({ "unused", "rawtypes" })
public final class JsonUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // ======================= 时间序列化规则 ===============================
        javaTimeModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateFormatterUtils.ofPattern(DateFormatterUtils.YYYY_MM_DD_HH_MM_DD)));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateFormatterUtils.ofPattern(DateFormatterUtils.YYYY_MM_DD)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateFormatterUtils.ofPattern(DateFormatterUtils.HH_MM_DD)));

        // ======================= 时间反序列化规则 ==============================
        javaTimeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateFormatterUtils.ofPattern(DateFormatterUtils.YYYY_MM_DD_HH_MM_DD)));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateFormatterUtils.ofPattern(DateFormatterUtils.YYYY_MM_DD)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateFormatterUtils.ofPattern(DateFormatterUtils.HH_MM_DD)));

        MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).disable(SerializationFeature.FAIL_ON_EMPTY_BEANS).configure(JsonParser.Feature.ALLOW_COMMENTS, true).configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true).configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true).configure(ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true).configure(READ_UNKNOWN_ENUM_VALUES_AS_NULL, true).setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).registerModule(javaTimeModule);
    }

    public static ArrayNode createArrayNode() {
        return MAPPER.createArrayNode();
    }

    public static ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

    public static JsonNode toJsonNode(Object obj) {
        return MAPPER.valueToTree(obj);
    }

    /**
     * json representation of object
     *
     * @param object  object
     * @param feature feature
     * @return object to json string
     */
    public static String toJsonString(Object object, SerializationFeature feature) {
        try {
            ObjectWriter writer = MAPPER.writer(feature);
            return writer.writeValueAsString(object);
        } catch (Exception e) {
            LOGGER.error("object to json exception!", e);
        }

        return null;
    }

    /**
     * This method deserializes the specified Json into an object of the specified class. It is not
     * suitable to use if the specified class is a generic type since it will not have the generic
     * type information because of the Type Erasure feature of Java. Therefore, this method should not
     * be used if the desired type is a generic type. Note that this method works fine if the any of
     * the fields of the specified object are generics, just the object itself should not be a
     * generic type.
     *
     * @param json  the string from which the object is to be deserialized
     * @param clazz the class of T
     * @param <T>   T
     * @return an object of type T from the string
     * classOfT
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            LOGGER.error("parse object exception!", e);
        }
        return null;
    }

    /**
     * deserialize
     *
     * @param src   byte array
     * @param clazz class
     * @param <T>   deserialize type
     * @return deserialize type
     */
    public static <T> T parseObject(byte[] src, Class<T> clazz) {
        if (src == null) {
            return null;
        }
        String json = new String(src, UTF_8);
        return parseObject(json, clazz);
    }

    public static <T> T parseObject(InputStream is, JavaType javaType) {
        try {
            return MAPPER.readValue(is, javaType);
        } catch (IOException e) {
            LOGGER.error("parse object exception!", e);
        }

        return null;
    }

    public static <T> T parseObject(String json, JavaType javaType) {
        try {
            return MAPPER.readValue(json, javaType);
        } catch (IOException e) {
            LOGGER.error("parse object exception!", e);
        }

        return null;
    }

    /**
     * json to list
     *
     * @param json  json string
     * @param clazz class
     * @param <T>   T
     * @return list
     */
    public static <T> List<T> toList(String json, Class<T> clazz) {
        if (StringUtils.isEmpty(json)) {
            return Collections.emptyList();
        }

        try {
            CollectionType listType = MAPPER.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return MAPPER.readValue(json, listType);
        } catch (Exception e) {
            LOGGER.error("parse list exception!", e);
        }

        return Collections.emptyList();
    }

    /**
     * json to set
     *
     * @param json  json string
     * @param clazz class
     * @param <T>   T
     * @return list
     */
    public static <T> Set<T> toSet(String json, Class<T> clazz) {
        if (!StringUtils.isEmpty(json)) {
            try {
                CollectionType listType = MAPPER.getTypeFactory().constructCollectionType(HashSet.class, clazz);
                return MAPPER.readValue(json, listType);
            } catch (Exception e) {
                LOGGER.error("parse list exception!", e);
            }
        }

        return Collections.emptySet();
    }

    /**
     * check json object valid
     *
     * @param json json
     * @return true if valid
     */
    public static boolean checkJsonValid(String json) {

        if (StringUtils.isEmpty(json)) {
            return false;
        }

        try {
            MAPPER.readTree(json);
            return true;
        } catch (IOException e) {
            LOGGER.error("check json object valid exception!", e);
        }

        return false;
    }

    /**
     * Method for finding a JSON Object field with specified name in this
     * node or its child nodes, and returning value it has.
     * If no matching field is found in this node or its descendants, returns null.
     *
     * @param jsonNode  json node
     * @param fieldName Name of field to look for
     * @return Value of first matching node found, if any; null if none
     */
    public static String findValue(JsonNode jsonNode, String fieldName) {
        JsonNode node = jsonNode.findValue(fieldName);

        if (node == null) {
            return null;
        }

        return node.asText();
    }

    /**
     * json to map
     * {@link #toMap(String, Class, Class)}
     *
     * @param json json
     * @return json to map
     */
    public static Map<String, String> toMap(String json) {
        return parseObject(json, new TypeReference<>() {});
    }

    /**
     * json to map
     *
     * @param json   json
     * @param classK classK
     * @param classV classV
     * @param <K>    K
     * @param <V>    V
     * @return to map
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> classK, Class<V> classV) {
        if (StringUtils.isEmpty(json)) {
            return Collections.emptyMap();
        }

        MapType javaType = MAPPER.getTypeFactory().constructMapType(HashMap.class, classK, classV);
        try {
            return MAPPER.readValue(json, javaType);
        } catch (Exception e) {
            LOGGER.error("json to map exception!", e);
        }

        return Collections.emptyMap();
    }

    /**
     * {@link InputStream} to map
     *
     * @param is     {@link InputStream}
     * @param classK classK
     * @param classV classV
     * @param <K>    K
     * @param <V>    V
     * @return to map
     */
    public static <K, V> Map<K, V> toMap(InputStream is, Class<? extends Map> mapType, Class<K> classK, Class<V> classV) {
        if (is == null) {
            return Collections.emptyMap();
        }

        MapType javaType = MAPPER.getTypeFactory().constructMapType(mapType, classK, classV);
        try {
            return MAPPER.readValue(is, javaType);
        } catch (Exception e) {
            LOGGER.error("json to map exception!", e);
        }

        return Collections.emptyMap();
    }

    /**
     * from the key-value generated json  to get the str value no matter the real type of value
     *
     * @param json     the json str
     * @param nodeName key
     * @return the str value of key
     */
    public static String getNodeString(String json, String nodeName) {
        try {
            JsonNode rootNode = MAPPER.readTree(json);
            JsonNode jsonNode = rootNode.findValue(nodeName);
            if (Objects.isNull(jsonNode)) {
                return "";
            }
            return jsonNode.isTextual() ? jsonNode.asText() : jsonNode.toString();
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    /**
     * json to object
     *
     * @param json json string
     * @param type type reference
     * @param <T>  convert type
     * @return return parse object
     */
    public static <T> T parseObject(String json, TypeReference<T> type) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }

        try {
            return MAPPER.readValue(json, type);
        } catch (Exception e) {
            LOGGER.error("json to map exception!", e);
        }

        return null;
    }

    /**
     * object to json string
     *
     * @param object object
     * @return json string
     */
    public static String toJsonString(Object object) {
        try {
            return MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Object json deserialization exception.", e);
        }
    }

    /**
     * serialize to json byte
     *
     * @param obj object
     * @param <T> object type
     * @return byte array
     */
    public static <T> byte[] toJsonByteArray(T obj) {
        if (obj == null) {
            return null;
        }
        String json = "";
        try {
            json = toJsonString(obj);
        } catch (Exception e) {
            LOGGER.error("json serialize exception.", e);
        }

        return json.getBytes(UTF_8);
    }

    public static ObjectNode parseObject(String text) {
        try {
            if (text.isEmpty()) {
                return parseObject(text, ObjectNode.class);
            } else {
                return (ObjectNode) MAPPER.readTree(text);
            }
        } catch (Exception e) {
            throw new RuntimeException("String json deserialization exception.", e);
        }
    }

    public static ArrayNode parseArray(String text) {
        try {
            return (ArrayNode) MAPPER.readTree(text);
        } catch (Exception e) {
            throw new RuntimeException("Json deserialization exception.", e);
        }
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }

    /**
     * json serializer
     */
    public static class JsonDataSerializer extends JsonSerializer<String> {

        @Override
        public void serialize(String value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeRawValue(value);
        }

    }

    /**
     * json data deserializer
     */
    public static class JsonDataDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);
            if (node instanceof TextNode) {
                return node.asText();
            } else {
                return node.toString();
            }
        }

    }
}
