package cn.com.felix.core.extend.shiro.session;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.CachingSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

public class ShiroSessionListener implements SessionListener {

    private static Logger logger = LoggerFactory.getLogger(ShiroSessionListener.class);

    private final AtomicInteger sessionCount = new AtomicInteger(0);

    @Resource
    private CachingSessionDAO sessionDAO;

    @Override
    public void onStart(Session session) {

        sessionCount.incrementAndGet();

        if (logger.isDebugEnabled()) {
            logger.debug("[Shiro] |- Listen >> Online User Added!!");
        }
    }

    @Override
    public void onStop(Session session) {

        sessionDAO.delete(session);

        sessionCount.decrementAndGet();

        if (logger.isDebugEnabled()) {
            logger.debug("[Shiro] |- Listen >> Online User Reduced!!");
        }
    }

    @Override
    public void onExpiration(Session session) {
        onStop(session);
    }

    /**
     * 获取在线人数使用
     * @return
     */
    public AtomicInteger getSessionCount() {
        return sessionCount;
    }
}
