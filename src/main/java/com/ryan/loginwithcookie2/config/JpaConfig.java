package com.ryan.loginwithcookie2.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@PersistenceContext
public class JpaConfig {
    private static EntityManagerFactory factory;

    static {
        try {
            String dir = new File(".").getAbsolutePath();
            String currentPath = System.getProperty("java.library.path", "");
            if (!currentPath.contains(dir)) {
                System.setProperty("java.library.path", dir + File.pathSeparator + currentPath);
            }
        } catch (Exception ignored) {}
    }

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (factory == null || !factory.isOpen()) {
            try {
                factory = Persistence.createEntityManagerFactory("ShoppingServiceMVC");
                EntityManager testEm = factory.createEntityManager();
                testEm.close();
            } catch (Exception e1) {
                try {
                    Map<String, Object> props = new HashMap<>();
                    props.put("jakarta.persistence.jdbc.url",
                            "jdbc:sqlserver://localhost:1433;databaseName=ShoppingServiceMVC;encrypt=true;trustServerCertificate=true;");
                    props.put("jakarta.persistence.jdbc.user", "sa");
                    props.put("jakarta.persistence.jdbc.password", "1234@a$");
                    factory = Persistence.createEntityManagerFactory("ShoppingServiceMVC", props);
                } catch (Exception e2) {
                    throw new RuntimeException("Không thể kết nối CSDL qua JPA: " + e1.getMessage(), e1);
                }
            }
        }
        return factory;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static synchronized void shutdown() {
        if (factory != null && factory.isOpen()) {
            factory.close();
            factory = null;
        }
    }
}
