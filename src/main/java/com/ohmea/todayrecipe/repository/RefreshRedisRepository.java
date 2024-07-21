package com.ohmea.todayrecipe.repository;

import com.ohmea.todayrecipe.entity.RefreshEntity;
import org.springframework.data.repository.CrudRepository;

public interface RefreshRedisRepository extends CrudRepository<RefreshEntity, String> {

}
