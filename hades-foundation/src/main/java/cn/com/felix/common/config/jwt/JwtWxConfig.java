package cn.com.felix.common.config.jwt;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.com.felix.common.config.shiro.ShiroConfig;
import cn.com.felix.core.extend.shiro.cache.RedisCache;
import cn.com.felix.core.extend.shiro.cache.RedisCacheManager;
import cn.com.felix.core.extend.redis.RedisManager;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import org.apache.shiro.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Configuration
@ConditionalOnBean(value = {ShiroConfig.class})
public class JwtWxConfig {

    private static final Logger logger = LoggerFactory.getLogger(JwtWxConfig.class);

    private final String WX_OPEN_ID = "wxOpenId";
    private final String SESSION_KEY = "sessionKey";
    private final String JWT_ID = "jwtId";
    private final String CACHE_AREA_NAME = "realm:wxJwtTokenCache";

    @Resource
    private RedisCacheManager redisCacheManager;
    @Resource
    private RedisManager redisManager;

    /**
     * JWT 过期时间值 这里写死为和小程序时间一致 7200 秒，也就是两个小时
     */
    private static long expire_time = 7200;

    /**
     * 根据微信用户登陆信息创建 token
     * 注 : 这里的token会被缓存到redis中,用作为二次验证
     * redis里面缓存的时间应该和jwt token的过期时间设置相同
     *
     * @param wxSession 微信用户Session信息
     * @return 返回 jwt token
     */
    public String createTokenByWXSession(WxMaJscode2SessionResult wxSession) {

        //JWT 随机ID,做为验证的key
        String jwtId = UUID.randomUUID().toString();

        //1 . 加密算法进行签名得到token
        Algorithm algorithm = Algorithm.HMAC256(wxSession.getSessionKey());
        String token = JWT.create()
                .withClaim(WX_OPEN_ID, wxSession.getOpenid())
                .withClaim(SESSION_KEY, wxSession.getSessionKey())
                .withClaim(JWT_ID, jwtId)
                .withExpiresAt(new Date(System.currentTimeMillis() + expire_time * 1000))  //JWT 配置过期时间的正确姿势
                .sign(algorithm);

        //2 . 缓存JWT, 注 : 请和JWT过期时间一致.由于系统是Redis和Ehcache切换，所以先按照缓存的方式存。
        // redis 的方式应该是 redisTemplate.opsForValue().set("JWT-SESSION-" + getJwtIdByToken(token), redisToken, expire_time, TimeUnit.SECONDS);
        setToCache("JWT-SESSION-" + jwtId, token);
        setToCache("JWT-WX-SESSION-" + jwtId, wxSession.getSessionKey());

        return token;
    }

    /**
     * 校验token是否正确
     * 1 . 根据token解密，解密出jwt-id , 先从redis中查找出redisToken，匹配是否相同
     * 2 . 然后再对redisToken进行解密，解密成功则 继续流程 和 进行token续期
     *
     * @param token 密钥
     * @return 返回是否校验通过
     */
    public boolean verifyToken(String token) {
        try {
            //1 . 根据token解密，解密出jwt-id , 先从redis中查找出redisToken，匹配是否相同

            String jwtId = getJwtIdByToken(token);
            String tokenCacheKey = tokenCacheKey(jwtId);
            String secretCacheKey = secretCacheKey(jwtId);

            String cacheToken = getValueFromCache(tokenCacheKey);
            String cacheSecret = getValueFromCache(secretCacheKey);

            if (null == cacheToken || !cacheToken.equals(token)) {
                return false;
            }

            //2 . 得到算法相同的JWTVerifier
            Algorithm algorithm = Algorithm.HMAC256(cacheSecret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(WX_OPEN_ID, getWxOpenIdByToken(cacheToken))
                    .withClaim(SESSION_KEY, getSessionKeyByToken(cacheToken))
                    .withClaim(JWT_ID, getJwtIdByToken(cacheToken))
                    .acceptExpiresAt(System.currentTimeMillis() + expire_time * 1000)  //JWT 正确的配置续期姿势
                    .build();
            //3 . 验证token
            verifier.verify(cacheToken);
            //4 . Redis缓存JWT续期
            setToCache(tokenCacheKey, cacheToken);
            setToCache(secretCacheKey, cacheSecret);
            return true;
        } catch (Exception e) { //捕捉到任何异常都视为校验失败
            return false;
        }
    }

    /**
     * 根据Token获取wxOpenId(注意坑点 : 就算token不正确，也有可能解密出wxOpenId,同下)
     */
    public String getWxOpenIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim(WX_OPEN_ID).asString();
    }

    /**
     * 根据Token获取sessionKey
     */
    public String getSessionKeyByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim(SESSION_KEY).asString();
    }

    /**
     * 根据Token 获取jwt-id
     */
    private String getJwtIdByToken(String token) throws JWTDecodeException {
        return JWT.decode(token).getClaim(JWT_ID).asString();
    }

    private void setToCache(String key, String value) {
        getCache().put(key, value);
        if (isRedisCache()) {
            redisManager.expire(key, expire_time);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("[JWT] |- Store Token To Cache: [{}]", value);
        }
    }

    private String getValueFromCache(String key) {
        if (logger.isDebugEnabled()) {
            logger.debug("[JWT] |- Fetch Token From Cache");
        }
        return getCache().get(key);
    }

    private boolean isRedisCache() {
        if (null != getCache() && getCache() instanceof RedisCache) {
            return true;
        } else {
            return false;
        }
    }

    private Cache<String, String> getCache() {
        return redisCacheManager.getCache(CACHE_AREA_NAME);
    }

    private String tokenCacheKey(String jwtId) {
        return "JWT-SESSION-" + jwtId;
    }

    private String secretCacheKey(String jwtId) {
        return "JWT-WX-SESSION-" + jwtId;
    }


}
