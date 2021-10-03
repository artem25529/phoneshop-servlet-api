package com.es.phoneshop.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static final long THRESHOLD = 20;
    private Map<String, Long> countMap = new ConcurrentHashMap<>();
    private Map<String, Long> timeMap = new ConcurrentHashMap<>();

    private static class SingletonHelper {
        private static final DefaultDosProtectionService INSTANCE = new DefaultDosProtectionService();
    }

    public static DefaultDosProtectionService getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long timeMills = timeMap.get(ip);
        Long count = countMap.get(ip);
        if (count == null || System.currentTimeMillis() - timeMills > 60 * 1000) {
            countMap.put(ip, 1L);
            timeMap.put(ip, System.currentTimeMillis());
        } else {
            if (count > THRESHOLD) {
                return false;
            }
            count++;
            countMap.put(ip, count);
        }
        return true;
    }
}
