package com.zhph.base.shiro;

import com.zhph.base.redis.RedisUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.ExpiredSessionException;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionKey;
import org.apache.shiro.session.mgt.SimpleSession;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.session.mgt.WebSessionManager;

import java.util.Collection;
import java.util.Iterator;
import static com.alibaba.druid.support.http.ResourceServlet.SESSION_USER_KEY;

/**
 * Created by zhph on 2017-08-02.
 */
public class SimpleWebSessionManager extends DefaultWebSessionManager implements
        WebSessionManager {

    private CacheManager cacheManager;

    private final static Logger logger = Logger.getLogger(SimpleWebSessionManager.class);

    public SimpleWebSessionManager() {
        super();
    }

    /**
     * 验证session的有效性
     */
    public void validateSessions() {
        if (logger.isInfoEnabled())
            logger.info("Validating all active sessions...");
        int invalidCount = 0;
        Collection<?> activeSessions = getActiveSessions();
        if (activeSessions != null && !activeSessions.isEmpty()) {
            for (Iterator<?> i$ = activeSessions.iterator(); i$.hasNext(); ) {
                Session session = (Session) i$.next();
                try {
                    SessionKey key = new DefaultSessionKey(session.getId());
                    validate(session, key);
                } catch (InvalidSessionException e) {
                    if (cacheManager != null) {
                        SimpleSession s = (SimpleSession) session;
                        if (s.getAttribute(SESSION_USER_KEY) != null)
                            cacheManager.getCache(null).remove(
                                    s.getAttribute(SESSION_USER_KEY));
                    }
                    if (logger.isDebugEnabled()) {
                        boolean expired = e instanceof ExpiredSessionException;
                        String msg = (new StringBuilder()).append(
                                "Invalidated session with id [").append(
                                session.getId()).append("]").append(
                                expired ? " (expired)" : " (stopped)")
                                .toString();
                        logger.debug(msg);
                    }
                    try {
                        RedisUtil.del(session.getId().toString());
                    } catch (Exception e1) {
                        logger.error("删除session异常", e1);
                    }
                    invalidCount++;
                }
            }

        }
        if (logger.isInfoEnabled()) {
            String msg = "Finished session validation.";
            if (invalidCount > 0)
                msg = (new StringBuilder()).append(msg).append("  [").append(
                        invalidCount).append("] sessions were stopped.")
                        .toString();
            else
                msg = (new StringBuilder()).append(msg).append(
                        "  No sessions were stopped.").toString();
            logger.info(msg);
        }
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
}
