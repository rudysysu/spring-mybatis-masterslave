package com.dy.spring.mybatis.masterslave;

import com.dy.spring.mybatis.masterslave.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    // VM Param: -Dspring.profiles.active=test
    private static final String DEFAULT_PROFILE = "test";

    public static void main(String[] args) throws Exception {
        String profile = System.getProperty("spring.profiles.active");
        if (StringUtils.isBlank(profile)) {
            LOG.warn("Do not specify profile, so use default profile: {}", DEFAULT_PROFILE);
            profile = DEFAULT_PROFILE;
            System.setProperty("spring.profiles.active", profile);
        }

        LOG.warn("spring.profiles.active: {}", profile);

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:/META-INF/spring/application.xml");
        context.start();

        UserService service = context.getBean(UserService.class);
        // service.rollbackFail();
        service.rollbackSuccess();
    }
}
