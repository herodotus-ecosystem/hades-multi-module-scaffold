package cn.com.felix.core.extend.shiro.session;


import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

public class ShiroSessionFactory implements SessionFactory {
    @Override
    public Session createSession(SessionContext initData) {
        if (null != initData) {
            String host = initData.getHost();
            if (null != host) {
                return new ShiroSession(host);

            }
        }
        return new ShiroSession();
    }
}
