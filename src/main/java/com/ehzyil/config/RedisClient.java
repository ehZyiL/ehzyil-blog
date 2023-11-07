package com.ehzyil.config;

import com.ehzyil.utils.JsonUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
//TODO 另一种redis的实现方式
public class RedisClient {
    private static final Charset CODE = StandardCharsets.UTF_8;
    private static final String KEY_PREFIX = "blog_";
    private static RedisTemplate<String, String> template;

    public static void register(RedisTemplate<String, String> template) {
        RedisClient.template = template;
    }

    public static void nullCheck(Object... args) {
        for (Object obj : args) {
            if (obj == null) {
                throw new IllegalArgumentException("redis argument can not be null!");
            }
        }
    }

    /**
     * 缓存值序列化处理
     *
     * @param val
     * @param <T>
     * @return
     */
    public static <T> byte[] valBytes(T val) {

        if (val instanceof String) {
            return ((String) val).getBytes(CODE);
        } else {
            return JsonUtil.toStr(val).getBytes(CODE);
        }
    }

    /**
     * 缓存key序列化处理
     *
     * @param key
     * @return
     */
    public static byte[] keyBytes(String key) {
        nullCheck(key);

        key = KEY_PREFIX + key;

        return key.getBytes(CODE);
    }

    /**
     * 缓存列表key序列化处理
     *
     * @param keys
     * @return
     */
    public static byte[][] keyBytes(List<String> keys) {
        byte[][] bytes = new byte[keys.size()][];
        int index = 0;
        for (String key : keys) {
            bytes[index++] = keyBytes(key);
        }
        return bytes;
    }

    /**
     * 获取set中的所有内容
     *
     * @param key
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> Set<T> setGetAll(String key, Class<T> clz) {
        return template.execute(new RedisCallback<Set<T>>() {
            @Override
            public Set<T> doInRedis(RedisConnection connection) throws DataAccessException {
                Set<byte[]> set = connection.sMembers(keyBytes(key));
                if (CollectionUtils.isEmpty(set)) {
                    return Collections.emptySet();
                }
                return set.stream().map(s -> toObj(s, clz)).collect(Collectors.toSet());
            }
        });
    }


    /**
     * 将字节数组转换为对象的方法
     *
     * @param ans 字节数组
     * @param clz 对象的Class类型
     * @return 转换后的对象
     */
    private static <T> T toObj(byte[] ans, Class<T> clz) {
        if (ans == null) {
            return null;
        }
        // 如果对象的Class类型是String类型，则将字节数组转换为字符串并返回

        if (clz == String.class) {
            return (T) new String(ans, CODE);
        }
        // 使用JsonUtil工具类将字节数组转换为对象，并返回转换后的对象
        return JsonUtil.toObj(new String(ans, CODE), clz);
    }

    /**
     * 往set中添加内容
     *
     * @param key
     * @param val
     * @param <T>
     * @return
     */
    public static <T> boolean setPut(String key, T val) {
        return template.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.sAdd(keyBytes(key), valBytes(val));
            }
        }) > 0;
    }

    /**
     * 移除set中的内容
     *
     * @param key
     * @param val
     * @param <T>
     */
    public static <T> void sDel(String key, T val) {
        template.execute(new RedisCallback<Void>() {
            @Override
            public Void doInRedis(RedisConnection connection) throws DataAccessException {
                connection.sRem(keyBytes(key), valBytes(val));
                return null;
            }
        });
    }

    /**
     * 判断value是否再set中
     *
     * @param key
     * @param value
     * @return
     */
    public static <T> Boolean setIsMember(String key, T value) {
        return template.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                return connection.sIsMember(keyBytes(key), valBytes(value));
            }
        });
    }
}