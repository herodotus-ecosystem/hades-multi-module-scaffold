package cn.com.felix.core.extend.shiro.authz;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.mindrot.jbcrypt.BCrypt;

import java.util.concurrent.atomic.AtomicInteger;

public class RetryLimitHashedCredentialsMatcher implements CredentialsMatcher {

    private int errorTimes;
    private int retryLimit;

    private Cache<String, AtomicInteger> passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher() {
        super();
    }

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager,
                                              int errorTimes, int retryLimit) {
        passwordRetryCache = cacheManager.getCache("realm:retryCache");
        this.errorTimes = errorTimes;
        this.retryLimit = retryLimit;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        //重复输错密码5次时需要等十分钟后重试
        int total = 0;
        if (errorTimes == 0) {
            total = retryLimit;
        } else {
            total = errorTimes;
        }
        if (retryCount.incrementAndGet() > total) {
            throw new ExcessiveAttemptsException();
        }
        boolean matches = isMatch(token, info);
        if (matches) {
            passwordRetryCache.remove(username);
        }
        return matches;
    }

    private boolean isMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

        //要验证的明文密码
        String plaintext = new String(usernamePasswordToken.getPassword());
        //数据库中的加密后的密文
        String hashed = info.getCredentials().toString();

        return BCrypt.checkpw(plaintext, hashed);
    }
}
