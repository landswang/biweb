package com.hongkuncheng.vcoin.helper;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 多线程帮助类
 * @author 洪坤成
 *
 */
public class ThreadHelper {

	/**
	 * 绑定HibernateSession到线程
	 * @param sessionFactory
	 * @return
	 */
	public static boolean bindHibernateSessionToThread(SessionFactory sessionFactory) {
        if (TransactionSynchronizationManager.hasResource(sessionFactory)) {
            return true;
        } else {
            Session session = sessionFactory.openSession();
            session.setFlushMode(FlushMode.MANUAL);
            SessionHolder sessionHolder = new SessionHolder(session);
            TransactionSynchronizationManager.bindResource(sessionFactory, sessionHolder);
        }
        return false;
    }
	
	/**
	 * 从线程关闭HibernateSession
	 * @param participate
	 * @param sessionFactory
	 */
    public static void closeHibernateSessionFromThread(boolean participate, Object sessionFactory) {
        if (!participate) {
            SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
            sessionHolder.getSession().flush();
            SessionFactoryUtils.closeSession(sessionHolder.getSession());
        }
    }
	
}
