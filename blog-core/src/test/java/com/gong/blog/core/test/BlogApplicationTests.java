package com.gong.blog.core.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    void contextLoads() throws IOException {
//        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
//        ScanOptions.ScanOptionsBuilder scanOptionsBuilder = ScanOptions.scanOptions();
//        Cursor<Map.Entry<String, String>> article =
//                hashOperations.scan("article:relation", scanOptionsBuilder.match("1728274734472585217*").build());
////        System.out.println(article.stream().count());
//
//        article.stream().toList().forEach(item -> {
//            System.out.println("article:relation" + item.getKey() + ":" + item.getValue());
//            hashOperations.delete("article:relation", item.getKey() + ":" + item.getValue());
//        });

//        JSON.parseObject("asdadasdasd", TextVo.class);

    }

}
