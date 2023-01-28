package com.bearcode.ovf.tools.redis;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisCallback;

/**
 * A custom RedisTemplate class that will optionally ignore connection failures. In case the redis cache service is
 * unavailble, the cache will be ignored entirely. An exception is logged but not propogated
 *
 * @author Daemmon Hughes
 * @date 9/14/16
 */
public class RedisTemplate<K, V> extends org.springframework.data.redis.core.RedisTemplate<K, V> {

    private boolean ignoreConnectionFailure;

    public boolean isIgnoreConnectionFailure(){
        return ignoreConnectionFailure;
    }

    public void setIgnoreConnectionFailure(boolean ignoreConnectionFailure){
        this.ignoreConnectionFailure = ignoreConnectionFailure;
    }

    @Override
    public <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {
        try {
            return super.execute(action,exposeConnection,pipeline);
        } catch(RedisConnectionFailureException ex) {
            if(ignoreConnectionFailure){
                logger.warn("Ignoring Redis connection failure", ex);
                return null;
            } else {
                throw ex;
            }
        }
    }
}
