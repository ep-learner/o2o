package com.o2o.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.SortingParams;
import redis.clients.util.SafeEncoder;

/**
 * @Description: Redis工具类
 *
 */
public class JedisUtil {
    /**
     * 缓存生存时间
     */
    private final int expire = 60000;
    /** 操作Key的方法 */
    public Keys KEYS;
    /** 对存储结构为String类型的操作 */
    public Strings STRINGS;
    /** 对存储结构为List类型的操作 */
    public Lists LISTS;


    private JedisPool jedisPool;

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPoolWriper jedisPoolWriper) {
        this.jedisPool = jedisPoolWriper.getJedisPool();
    }

    public JedisPool getPool() {
        return jedisPool;
    }

    /**
     * 从jedis连接池中获取获取jedis对象
     */
    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param seconds
     */
    public void expire(String key, int seconds) {
        if (seconds <= 0) {
            return;
        }
        Jedis jedis = getJedis();
        jedis.expire(key, seconds);
        jedis.close();
    }

    /**
     * 设置默认过期时间
     *
     * @param key
     */
    public void expire(String key) {
        expire(key, expire);
    }

    // *******************************************Keys*******************************************//
    public class Keys {
        JedisUtil jedisUtil;
        public Keys(JedisUtil jedisUtil){
            this.jedisUtil = jedisUtil;
        }
        public Keys(){

        }

        /**
         * 清空所有key
         */
        public String flushAll() {
            Jedis jedis = getJedis();
            String stata = jedis.flushAll();
            jedis.close();
            return stata;
        }

        /**
         * 更改key
         */
        public String rename(String oldkey, String newkey) {
            return rename(SafeEncoder.encode(oldkey), SafeEncoder.encode(newkey));
        }

        /**
         * 更改key,仅当新key不存在时才执行
         */
        public long renamenx(String oldkey, String newkey) {
            Jedis jedis = getJedis();
            long status = jedis.renamenx(oldkey, newkey);
            jedis.close();
            return status;
        }

        /**
         * 更改key
         */
        public String rename(byte[] oldkey, byte[] newkey) {
            Jedis jedis = getJedis();
            String status = jedis.rename(oldkey, newkey);
            jedis.close();
            return status;
        }

        /**
         * 设置key的过期时间，以秒为单位
         *
         */
        public long expired(String key, int seconds) {
            Jedis jedis = getJedis();
            long count = jedis.expire(key, seconds);
            jedis.close();
            return count;
        }

        /**
         * 设置key的过期时间,它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00，格里高利历）的偏移量。
         *
         */
        public long expireAt(String key, long timestamp) {
            Jedis jedis = getJedis();
            long count = jedis.expireAt(key, timestamp);
            jedis.close();
            return count;
        }

        /**
         * 查询key的过期时间
         *
         */
        public long ttl(String key) {
            Jedis sjedis = getJedis();
            long len = sjedis.ttl(key);
            sjedis.close();
            return len;
        }

        /**
         * 取消对key过期时间的设置
         * @param key
         * @return 影响的记录数
         */
        public long persist(String key) {
            Jedis jedis = getJedis();
            long count = jedis.persist(key);
            jedis.close();
            return count;
        }

        /**
         * 删除keys对应的记录,可以是多个key
         *
         */
        public long del(String... keys) {
            Jedis jedis = getJedis();
            long count = jedis.del(keys);
            jedis.close();
            return count;
        }

        /**
         * 删除keys对应的记录,可以是多个key
         *
         */
        public long del(byte[]... keys) {
            Jedis jedis = getJedis();
            long count = jedis.del(keys);
            jedis.close();
            return count;
        }

        /**
         * 判断key是否存在
         *
         */
        public boolean exists(String key) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = getJedis();
            boolean exis = sjedis.exists(key);
            sjedis.close();
            return exis;
        }

        /**
         * 对List,Set,SortSet进行排序,如果集合数据较大应避免使用这个方法
         *
         **/
        public List<String> sort(String key) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = getJedis();
            List<String> list = sjedis.sort(key);
            sjedis.close();
            return list;
        }

        /**
         * 对List,Set,SortSet进行排序或limit
         *
         **/
        public List<String> sort(String key, SortingParams parame) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = getJedis();
            List<String> list = sjedis.sort(key, parame);
            sjedis.close();
            return list;
        }

        /**
         * 返回指定key存储的类型
         *
         **/
        public String type(String key) {
            // ShardedJedis sjedis = getShardedJedis();
            Jedis sjedis = getJedis();
            String type = sjedis.type(key);
            sjedis.close();
            return type;
        }

        /**
         * 查找所有匹配给定的模式的键
         */
        public Set<String> keys(String pattern) {
            Jedis jedis = getJedis();
            Set<String> set = jedis.keys(pattern);
            jedis.close();
            return set;
        }
    }

    // *******************************************Strings*******************************************//
    public class Strings {
        JedisUtil jedisUtil;
        public Strings(JedisUtil jedisUtil){
            this.jedisUtil = jedisUtil;
        }
        public Strings(){

        }
        /**
         * 根据key获取记录

         */
        public String get(String key) {
            Jedis sjedis = getJedis();
            String value = sjedis.get(key);
            sjedis.close();
            return value;
        }

        /**
         * 根据key获取记录
         *
         */
        public byte[] get(byte[] key) {
            Jedis sjedis = getJedis();
            byte[] value = sjedis.get(key);
            sjedis.close();
            return value;
        }

        /**
         * 添加有过期时间的记录
         *
         */
        public String setEx(String key, int seconds, String value) {
            Jedis jedis = getJedis();
            String str = jedis.setex(key, seconds, value);
            jedis.close();
            return str;
        }

        /**
         * 添加有过期时间的记录
         *
         */
        public String setEx(byte[] key, int seconds, byte[] value) {
            Jedis jedis = getJedis();
            String str = jedis.setex(key, seconds, value);
            jedis.close();
            return str;
        }

        /**
         * 添加一条记录，仅当给定的key不存在时才插入
         *
         */
        public long setnx(String key, String value) {
            Jedis jedis = getJedis();
            long str = jedis.setnx(key, value);
            jedis.close();
            return str;
        }

        /**
         * 添加记录,如果记录已存在将覆盖原有的value
         *
         */
        public String set(String key, String value) {
            return set(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        /**
         * 添加记录,如果记录已存在将覆盖原有的value
         *
         */
        public String set(String key, byte[] value) {
            return set(SafeEncoder.encode(key), value);
        }

        /**
         * 添加记录,如果记录已存在将覆盖原有的value
         *
         */
        public String set(byte[] key, byte[] value) {
            Jedis jedis = getJedis();
            String status = jedis.set(key, value);
            jedis.close();
            return status;
        }

        /**
         * 从指定位置开始插入数据，插入的数据会覆盖指定位置以后的数据<br/>
         * 例:String str1="123456789";<br/>
         * 对str1操作后setRange(key,4,0000)，str1="123400009";
         *
         */
        public long setRange(String key, long offset, String value) {
            Jedis jedis = getJedis();
            long len = jedis.setrange(key, offset, value);
            jedis.close();
            return len;
        }

        /**
         * 在指定的key中追加value
         *
         **/
        public long append(String key, String value) {
            Jedis jedis = getJedis();
            long len = jedis.append(key, value);
            jedis.close();
            return len;
        }

        /**
         * 将key对应的value减去指定的值，只有value可以转为数字时该方法才可用
         *
         */
        public long decrBy(String key, long number) {
            Jedis jedis = getJedis();
            long len = jedis.decrBy(key, number);
            jedis.close();
            return len;
        }

        /**
         * <b>可以作为获取唯一id的方法</b><br/>
         * 将key对应的value加上指定的值，只有value可以转为数字时该方法才可用
         *
         */
        public long incrBy(String key, long number) {
            Jedis jedis = getJedis();
            long len = jedis.incrBy(key, number);
            jedis.close();
            return len;
        }

        /**
         * 对指定key对应的value进行截取
         *
         */
        public String getrange(String key, long startOffset, long endOffset) {
            Jedis sjedis = getJedis();
            String value = sjedis.getrange(key, startOffset, endOffset);
            sjedis.close();
            return value;
        }

        /**
         * 获取并设置指定key对应的value<br/>
         * 如果key存在返回之前的value,否则返回null
         *
         */
        public String getSet(String key, String value) {
            Jedis jedis = getJedis();
            String str = jedis.getSet(key, value);
            jedis.close();
            return str;
        }

        /**
         * 批量获取记录,如果指定的key不存在返回List的对应位置将是null
         *
         */
        public List<String> mget(String... keys) {
            Jedis jedis = getJedis();
            List<String> str = jedis.mget(keys);
            jedis.close();
            return str;
        }

        /**
         * 批量存储记录
         *
         */
        public String mset(String... keysvalues) {
            Jedis jedis = getJedis();
            String str = jedis.mset(keysvalues);
            jedis.close();
            return str;
        }

        /**
         * 获取key对应的值的长度
         *
         */
        public long strlen(String key) {
            Jedis jedis = getJedis();
            long len = jedis.strlen(key);
            jedis.close();
            return len;
        }
    }

    // *******************************************Lists*******************************************//
    public class Lists {

        JedisUtil jedisUtil;
        public Lists(JedisUtil jedisUtil){
            this.jedisUtil = jedisUtil;
        }
        public Lists(){

        }

        /**
         * List长度
         */
        public long llen(String key) {
            return llen(SafeEncoder.encode(key));
        }

        /**
         * list 长度
         * @param key
         * @return
         */
        public long llen(byte[] key) {
            Jedis sjedis = getJedis();
            long count = sjedis.llen(key);
            sjedis.close();
            return count;
        }

        /**
         * 覆盖操作,将覆盖List中指定位置的值
         *
         * @return 状态码
         */
        public String lset(byte[] key, int index, byte[] value) {
            Jedis jedis = getJedis();
            String status = jedis.lset(key, index, value);
            jedis.close();
            return status;
        }

        /**
         * 覆盖操作,将覆盖List中指定位置的值
         *
         * @param key
         * @return 状态码
         */
        public String lset(String key, int index, String value) {
            return lset(SafeEncoder.encode(key), index, SafeEncoder.encode(value));
        }

        /**
         * 在value的相对位置插入记录

         */
        public long linsert(String key, LIST_POSITION where, String pivot, String value) {
            return linsert(SafeEncoder.encode(key), where, SafeEncoder.encode(pivot), SafeEncoder.encode(value));
        }

        /**
         * 在指定位置插入记录
         *
         */
        public long linsert(byte[] key, LIST_POSITION where, byte[] pivot, byte[] value) {
            Jedis jedis = getJedis();
            long count = jedis.linsert(key, where, pivot, value);
            jedis.close();
            return count;
        }

        /**
         * 获取List中指定位置的值
         *
         **/
        public String lindex(String key, int index) {
            return SafeEncoder.encode(lindex(SafeEncoder.encode(key), index));
        }

        /**
         * 获取List中指定位置的值
         *
         **/
        public byte[] lindex(byte[] key, int index) {
            Jedis sjedis = getJedis();
            byte[] value = sjedis.lindex(key, index);
            sjedis.close();
            return value;
        }

        /**
         * 将List中的第一条记录移出List
         *
         */
        public String lpop(String key) {
            return SafeEncoder.encode(lpop(SafeEncoder.encode(key)));
        }

        /**
         * 将List中的第一条记录移出List
         *
         */
        public byte[] lpop(byte[] key) {
            Jedis jedis = getJedis();
            byte[] value = jedis.lpop(key);
            jedis.close();
            return value;
        }

        /**
         * 将List中最后第一条记录移出List
         *
         */
        public String rpop(String key) {
            Jedis jedis = getJedis();
            String value = jedis.rpop(key);
            jedis.close();
            return value;
        }

        /**
         * 向List尾部追加记录
         *
         */
        public long lpush(String key, String value) {
            return lpush(SafeEncoder.encode(key), SafeEncoder.encode(value));
        }

        /**
         * 向List头部追加记录
         *
         */
        public long rpush(String key, String value) {
            Jedis jedis = getJedis();
            long count = jedis.rpush(key, value);
            jedis.close();
            return count;
        }

        /**
         * 向List头部追加记录
         *
         */
        public long rpush(byte[] key, byte[] value) {
            Jedis jedis = getJedis();
            long count = jedis.rpush(key, value);
            jedis.close();
            return count;
        }

        /**
         * 向List中追加记录
         *
         */
        public long lpush(byte[] key, byte[] value) {
            Jedis jedis = getJedis();
            long count = jedis.lpush(key, value);
            jedis.close();
            return count;
        }

        /**
         * 获取指定范围的记录，可以做为分页使用

         */
        public List<String> lrange(String key, long start, long end) {
            Jedis sjedis = getJedis();
            List<String> list = sjedis.lrange(key, start, end);
            sjedis.close();
            return list;
        }

        /**
         * 获取指定范围的记录，可以做为分页使用
         *
         * @return List
         */
        public List<byte[]> lrange(byte[] key, int start, int end) {
            Jedis sjedis = getJedis();
            List<byte[]> list = sjedis.lrange(key, start, end);
            sjedis.close();
            return list;
        }

        /**
         * 删除List中c条记录，被删除的记录值为value
         *
         */
        public long lrem(byte[] key, int c, byte[] value) {
            Jedis jedis = getJedis();
            long count = jedis.lrem(key, c, value);
            jedis.close();
            return count;
        }

        /**
         * 删除List中c条记录，被删除的记录值为value
         *
         */
        public long lrem(String key, int c, String value) {
            return lrem(SafeEncoder.encode(key), c, SafeEncoder.encode(value));
        }

        /**
         * 算是删除吧，只保留start与end之间的记录
         *
         */
        public String ltrim(byte[] key, int start, int end) {
            Jedis jedis = getJedis();
            String str = jedis.ltrim(key, start, end);
            jedis.close();
            return str;
        }

        /**
         * 算是删除吧，只保留start与end之间的记录
         *
         */
        public String ltrim(String key, int start, int end) {
            return ltrim(SafeEncoder.encode(key), start, end);
        }
    }

}
