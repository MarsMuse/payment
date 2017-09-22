package com.zhph.base.shiro;

import com.zhph.base.redis.RedisUtil;
import org.apache.log4j.Logger;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by zhph on 2017-03-01.
 */
public class RedisSessionDao extends AbstractSessionDAO {


    Logger log= Logger.getLogger(RedisSessionDao.class);

    private List<Session> sessionList = new ArrayList<Session>();

    public void update(Session session)  {
        try {
            log.debug(String.format("更新seesion,id=[%s]",session.getId().toString()));
            RedisUtil.setObject(session.getId().toString(),session,new Long(session.getTimeout()).intValue() / 1000);
        } catch (Exception e) {
            log.error("更新seesion异常",e);
        }
    }


    public void delete(Session session) {
        log.debug(String.format("删除seesion,id=[%s]",session.getId().toString()));
        try {
            RedisUtil.del(session.getId().toString());
            sessionList.remove(session);
        } catch (Exception e) {
            log.error("删除seesion异常",e);
        }

    }


    public Collection<Session> getActiveSessions() {
        log.debug("获取存活的session");
        return sessionList;
    }


    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        log.debug(String.format("创建seesion,id=[%s]",session.getId().toString()));
        try {
            RedisUtil.setObject(sessionId.toString(),session,new Long(session.getTimeout()).intValue() / 1000);
            sessionList.add(session);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return sessionId;
    }


    protected Session doReadSession(Serializable sessionId) {
        log.debug(String.format("获取seesion,id=[%s]",sessionId.toString()));
        Session session = null;
        try {
            session = (Session) RedisUtil.getObject(sessionId.toString());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return session;
    }
}
