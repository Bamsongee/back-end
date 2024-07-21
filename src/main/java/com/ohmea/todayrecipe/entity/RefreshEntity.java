package com.ohmea.todayrecipe.entity;

import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "refresh", timeToLive = 86400)
public class RefreshEntity {

    @Id
    private String refresh;

    private String username;
}