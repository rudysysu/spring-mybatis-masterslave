package com.dy.spring.mybatis.masterslave.service;

import com.dy.spring.mybatis.masterslave.dao.UserMapper;
import com.dy.spring.mybatis.masterslave.domain.User;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Service
public class UserService {
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    @Qualifier("masterTransactionManager")
    private DataSourceTransactionManager masterTransactionManager;

    @Autowired
    @Qualifier("masterSqlSessionFactory")
    private SqlSessionFactoryBean masterSqlSessionFactory;

    @Autowired
    private UserMapper slaveUserMapper; // 这个由Spring管理，受...Slave...Factory控制，不受masterTransactionManager控制，下面的rollback对其"无效"

    public void rollbackFail() throws Exception {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus tx = masterTransactionManager.getTransaction(def);
        try {
            User user = new User("Rudy");
            slaveUserMapper.insert(user); // rollback无效

            int a = 1;
            if (a == 1) {
                throw new Exception("模拟事务失败");
            }

            masterTransactionManager.commit(tx);
        } catch (Exception e) {
            LOG.error("save effect error:", e);
            masterTransactionManager.rollback(tx);
            throw e;
        }
    }

    public void rollbackSuccess() throws Exception {
        SqlSession sqlSession = masterSqlSessionFactory.getObject().openSession();
        try {
            UserMapper masterUserMapper = sqlSession.getMapper(UserMapper.class);// 这个不由Spring管理，受...Master...Factory控制，下面的rollback对其"有效"

            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            TransactionStatus tx = masterTransactionManager.getTransaction(def);
            try {
                User user = new User("Rudy");
                masterUserMapper.insert(user); // rollback有效

                int a = 1;
                if (a == 1) {
                    throw new Exception("模拟事务失败");
                }

                masterTransactionManager.commit(tx);
            } catch (Exception e) {
                LOG.error("save effect error:", e);
                masterTransactionManager.rollback(tx);
                throw e;
            }
        } finally {
            sqlSession.close();
        }
    }
}
