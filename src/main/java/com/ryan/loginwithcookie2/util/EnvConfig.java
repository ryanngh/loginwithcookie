package com.ryan.loginwithcookie2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EnvConfig {
    private static final Map<String, String> ENV_VARS = new HashMap<>();

    static {
        loadEnv();
    }

    private static void loadEnv() {
        // Tìm file .env ở thư mục hiện tại hoặc thư mục cha/con
        File[] candidates = new File[] {
                new File(".env"),
                new File("LoginWithCookie2/.env"),
                new File("../.env")
        };

        File envFile = null;
        for (File candidate : candidates) {
            if (candidate.exists() && candidate.isFile()) {
                envFile = candidate;
                break;
            }
        }

        if (envFile != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(envFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.isEmpty() || line.startsWith("#")) {
                        continue;
                    }
                    int eqIndex = line.indexOf('=');
                    if (eqIndex > 0) {
                        String key = line.substring(0, eqIndex).trim();
                        String value = line.substring(eqIndex + 1).trim();
                        // Bỏ dấu nháy kép hoặc đơn nếu có
                        if ((value.startsWith("\"") && value.endsWith("\"")) ||
                            (value.startsWith("'") && value.endsWith("'"))) {
                            value = value.substring(1, value.length() - 1);
                        }
                        ENV_VARS.put(key, value);
                    }
                }
            } catch (IOException e) {
                System.err.println("Không thể đọc file .env: " + e.getMessage());
            }
        }
    }

    public static String get(String key) {
        String sysEnv = System.getenv(key);
        if (sysEnv != null && !sysEnv.isEmpty()) {
            return sysEnv;
        }
        return ENV_VARS.get(key);
    }

    public static String get(String key, String defaultValue) {
        String val = get(key);
        return (val != null && !val.isEmpty()) ? val : defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        String val = get(key);
        if (val != null && !val.isEmpty()) {
            try {
                return Integer.parseInt(val);
            } catch (NumberFormatException ignored) {}
        }
        return defaultValue;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String val = get(key);
        if (val != null && !val.isEmpty()) {
            return Boolean.parseBoolean(val);
        }
        return defaultValue;
    }
}
