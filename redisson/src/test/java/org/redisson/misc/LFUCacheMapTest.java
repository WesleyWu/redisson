package org.redisson.misc;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class LFUCacheMapTest {

    @Test
    public void testMaxIdleTimeEviction() throws InterruptedException {
        Cache<Integer, Integer> map = new LFUCacheMap<Integer, Integer>(2, 0, 0);
        map.put(1, 0, 0, TimeUnit.MILLISECONDS, 400, TimeUnit.MILLISECONDS);
        assertThat(map.get(1)).isEqualTo(0);
        Thread.sleep(200);
        assertThat(map.get(1)).isEqualTo(0);
        Thread.sleep(200);
        assertThat(map.get(1)).isEqualTo(0);
        Thread.sleep(200);
        assertThat(map.get(1)).isEqualTo(0);
        Thread.sleep(410);
        assertThat(map.keySet()).isEmpty();
    }

    @Test
    public void testTTLEviction() throws InterruptedException {
        Cache<Integer, Integer> map = new LFUCacheMap<Integer, Integer>(2, 0, 0);
        map.put(1, 0, 500, TimeUnit.MILLISECONDS, 0, TimeUnit.MILLISECONDS);
        assertThat(map.get(1)).isEqualTo(0);
        Thread.sleep(100);
        assertThat(map.get(1)).isEqualTo(0);
        assertThat(map.keySet()).containsOnly(1);
        Thread.sleep(500);
        assertThat(map.keySet()).isEmpty();
    }
    
    @Test
    public void testSizeLFUEviction() throws InterruptedException {
        Cache<Integer, Integer> map = new LFUCacheMap<Integer, Integer>(3, 0, 0);
        
        map.put(1, 0);
        map.put(2, 0);
        map.put(6, 0);

        map.get(1);
        map.put(3, 0);
        
        assertThat(map.keySet()).containsOnly(3, 1, 6);
        
        map.get(1);
        map.put(4, 0);
        
        assertThat(map.keySet()).contains(4, 1).hasSize(3);
    }
    
    @Test
    public void testSizeEviction() throws InterruptedException {
        Cache<Integer, Integer> map = new LFUCacheMap<Integer, Integer>(2, 0, 0);
        map.put(1, 0);
        map.put(2, 0);
        
        assertThat(map.keySet()).containsOnly(1, 2);
        
        map.put(3, 0);
        
        assertThat(map.keySet()).contains(3).hasSize(2);
        
        map.put(4, 0);
        
        assertThat(map.keySet()).contains(4).hasSize(2);
        
        map.put(5, 0);
        
        assertThat(map.keySet()).contains(5).hasSize(2);
    }
    
}
