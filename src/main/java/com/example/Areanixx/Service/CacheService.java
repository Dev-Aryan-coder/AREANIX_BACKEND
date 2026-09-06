package com.example.Areanixx.Service;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
	private final ConcurrentHashMap<String, Object> memoryCache = new ConcurrentHashMap<>();

	public void put(String key, Object value) {
		memoryCache.put(key, value);
	}

	public Object get(String key) {
		return memoryCache.get(key);
	}

	public void evict(String key) {
		memoryCache.remove(key);
	}
}
