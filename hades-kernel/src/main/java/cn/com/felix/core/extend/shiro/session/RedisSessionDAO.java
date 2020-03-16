package cn.com.felix.core.extend.shiro.session;

import cn.com.felix.core.extend.shiro.cache.RedisCache;
import cn.com.felix.core.utils.Constants;
import cn.com.felix.core.utils.ServletUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.ValidatingSession;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 最开始通过继承AbstractSessionDAO实现，发现doReadSession方法调用过于频繁，所以改为通过集成CachingSessionDAO来实现
 * <p>
 * 针对自定义的ShiroRedisSession的Redis CRUD操作，通过isChanged标识符，确定是否需要调用Update方法
 * 通过配置securityManager在属性cacheManager查找从缓存中查找Session是否存在，如果找不到才调用下面方法
 * Shiro内部相应的组件（DefaultSecurityManager）会自动检测相应的对象（如Realm）是否实现了CacheManagerAware并自动注入相应的CacheManager。
 */

public class RedisSessionDAO extends CachingSessionDAO {

    private static Logger logger = LoggerFactory.getLogger(RedisSessionDAO.class);

    /**
     * expire time in seconds
     */
    private static final int DEFAULT_EXPIRE = -2;
    private static final int NO_EXPIRE = -1;
    /**
     * Please make sure expire is longer than sesion.getTimeout()
     */
    private int expire = DEFAULT_EXPIRE;
    private static final int MILLISECONDS_IN_A_SECOND = 1000;

    @Resource
    private RedisTemplate redisTemplate;

    public RedisSessionDAO() {
    }

    public RedisSessionDAO(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 重写CachingSessionDAO中readSession方法，如果Session中没有登陆信息就调用doReadSession方法从Redis中重读
     *
     * @param sessionId
     * @return
     * @throws UnknownSessionException
     */
    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        Session session = getCachedSession(sessionId);
        if (null == session || session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) == null) {
            session = this.doReadSession(sessionId);
            if (session == null) {
                throw new UnknownSessionException("There is no session with id [" + sessionId + "]");
            } else {
                cache(session, session.getId());
            }
        }

        return session;
    }

    /**
     * 根据会话ID获取会话
     *
     * @param sessionId
     * @return
     */
    @Override
    protected Session doReadSession(Serializable sessionId) {

        ShiroSession session = null;

        // 获取本次Request
        HttpServletRequest request = ServletUtils.getRequest();
        if (request != null) {
            String uri = request.getServletPath();

            if (logger.isTraceEnabled()) {
                logger.trace("[Shiro] |- Current Request URI [{}]", uri);
            }

            // 如果是静态文件，则不获取SESSION
            if (ServletUtils.isStaticFile(uri)) {
                return null;
            }

            session = (ShiroSession) request.getAttribute(sessionId.toString());
        }
        if (session != null) {
            return session;
        }

        try {

            session = (ShiroSession) redisTemplate.opsForValue().get(getSessionKey(sessionId));

            if (session != null && session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY) != null) {
                //检查session是否过期
                session.validate();
                // 重置Redis中Session的最后访问时间
                session.setLastAccessTime(new Date());
                this.storeSession(session);
            }

            if (logger.isTraceEnabled()) {
                logger.trace("[Shiro] |- Readed SessionId: [{}] from [{}]", sessionId, request != null ? request.getRequestURI() : "");
            }
        } catch (Exception e) {
            if (!(e instanceof ExpiredSessionException)) {
                logger.warn("[Shiro] |- Read Session Failure!!!!", e);
            } else {
                logger.warn("[Shiro] |- Session is invalidated:{}", e.getMessage());
            }
        }

        if (request != null && session != null){
            request.setAttribute(sessionId.toString(), session);
        }

        return session;
}

    /**
     * 如DefaultSessionManager在创建完session后会调用该方法；
     * 如保存到关系数据库/文件系统/NoSQL数据库；即可以实现会话的持久化；
     * 返回会话ID；主要此处返回的ID.equals(session.getId())；
     *
     * @param session
     * @return
     */
    @Override
    protected Serializable doCreate(Session session) {

        Serializable sessionId = this.generateSessionId(session);
        this.assignSessionId(session, sessionId);
        storeSession(session);

        if (logger.isTraceEnabled()) {
            logger.trace("[Shiro] |- SessionId: [{}] Name: [{}] is Created", sessionId, session.getClass().getName());
        }

        return sessionId;
    }

    private void storeSession(Session session) throws UnknownSessionException {

        if (session == null || session.getId() == null) {
            logger.error("[Shiro] |- Session or Session id is null!!");
            throw new UnknownSessionException("Session or Session id is null");
        }

        ShiroSession shiroSession = (ShiroSession) session;

        final Long expireValue = Long.valueOf(shiroSession.getTimeout() / MILLISECONDS_IN_A_SECOND);
        final String sessionKey = getSessionKey(shiroSession.getId());

        if (expire == DEFAULT_EXPIRE) {
            this.redisTemplate.execute(new SessionCallback<List<Object>>() {
                public List<Object> execute(RedisOperations operations) throws DataAccessException {
                    operations.multi();
                    operations.opsForValue().set(sessionKey, shiroSession);
                    operations.expire(sessionKey, expireValue.longValue(), TimeUnit.SECONDS);
                    return operations.exec();
                }
            });

            return;
        }
        if (expire != NO_EXPIRE && ((expire * MILLISECONDS_IN_A_SECOND) < session.getTimeout())) {
            logger.warn("[Shiro] |- Redis session expire time: {} is less than Session timeout: {}. It may cause some problems.", (expire * MILLISECONDS_IN_A_SECOND), session.getTimeout());
        }

        this.redisTemplate.execute(new SessionCallback<List<Object>>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForValue().set(sessionKey, shiroSession);
                operations.expire(sessionKey, expire, TimeUnit.SECONDS);
                return operations.exec();
            }
        });
    }

    //扩展更新缓存机制，每次请求不重新更新session，更新session会延长session的失效时间
    @Override
    public void update(Session session) throws UnknownSessionException {
        doUpdate(session);
        if (session instanceof ValidatingSession) {
            if (((ValidatingSession) session).isValid()) {
                //不更新ehcach中的session，使它在设定的时间内过期
                //cache(session, session.getId());
            } else {
                uncache(session);
            }
        } else {
            cache(session, session.getId());
        }
    }

    /**
     * 更新会话；如更新会话最后访问时间/停止会话/设置超时时间/设置移除属性等会调用
     */
    @Override
    protected void doUpdate(Session session) {

        // 获取本次Request
        HttpServletRequest request = ServletUtils.getRequest();
        if (request != null) {
            String uri = request.getServletPath();
            // 如果是静态文件，则不获取SESSION
            if (ServletUtils.isStaticFile(uri)) {
                return;
            }
        }

        //如果会话过期/停止 没必要再更新了
        try {
            if (session instanceof ValidatingSession && !((ValidatingSession) session).isValid()) {
                return;
            }

            if (session instanceof ShiroSession) {
                // 如果没有主要字段(除lastAccessTime以外其他字段)发生改变
                ShiroSession shiroSession = (ShiroSession) session;
                if (!shiroSession.isChanged()) {
                    return;
                }

                //如果没有返回 证明有调用 setAttribute往redis 放的时候永远设置为false
                shiroSession.setChanged(false);
                storeSession(shiroSession);

                if (logger.isTraceEnabled()) {
                    logger.trace("[Shiro] |- Session: [{}] Name: [{}] is Updated", shiroSession.getId(), session.getClass().getName());
                }

            } else if (session instanceof Serializable) {
                storeSession(session);
                if (logger.isTraceEnabled()) {
                    logger.trace("[Shiro] |- Session: [{}] Name: [{}] is Updated not as ShiroRedisSession", session.getId(), session.getClass().getName());
                }
            } else {
                logger.error("[Shiro] |- Session: [{}] Name: [{}] cannot Serialized, update failure!", session.getId(), session.getClass().getName());
            }
        } catch (Exception e) {
            logger.error("[Shiro] |- ValidatingSession error!");
        }
    }

    @Override
    protected void doDelete(Session session) {
        if (session == null || session.getId() == null) {
            logger.error("[Shiro] |- Session or Session id is null!!");
        }
        try {
            redisTemplate.delete(getSessionKey(session.getId()));
            if (logger.isTraceEnabled()) {
                logger.trace("[Shiro] |- Session: [{}] is Deleted", session.getId());
            }
        } catch (Exception e) {
            logger.warn("[Shiro] |- Delete Session Failure. Session ID: [{}]", session.getId());
        }
    }

    /**
     * 删除cache中缓存的Session
     */
    public void uncache(Serializable sessionId) {
        Session session = this.readSession(sessionId);
        super.uncache(session);

        if (logger.isTraceEnabled()) {
            logger.trace("[Shiro] |- Uncache Session : [{}] store.", session.getId());
        }
    }

    private String getSessionKey(Serializable sessionId) {
        return getSessionKeyPrefix() + sessionId;
    }

    private String getSessionKeyPrefix() {
        StringBuilder builder = new StringBuilder();
        builder.append(RedisCache.REDIS_SHIRO_CACHE);
        builder.append(this.getActiveSessionsCacheName());
        builder.append(Constants.COLON);
        return builder.toString();
    }

    @Override
    public Collection<Session> getActiveSessions() {
        List<Session> sessions = new ArrayList<>();

        Set keys = redisTemplate.keys(getSessionKeyPrefix() + "*");
        if (!CollectionUtils.isEmpty(keys)) {
            sessions = redisTemplate.opsForValue().multiGet(keys);
        }

        return sessions;
    }

}
