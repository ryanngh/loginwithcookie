package com.ryan.loginwithcookie2.util;

import com.ryan.loginwithcookie2.config.JpaConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class JPAConfig {
    public static EntityManagerFactory getEntityManagerFactory() {
        return JpaConfig.getEntityManagerFactory();
    }

    public static EntityManager getEntityManager() {
        return JpaConfig.getEntityManager();
    }

    public static void shutdown() {
        JpaConfig.shutdown();
    }
}
