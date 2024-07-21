package com.ohmea.todayrecipe.repository;

import com.ohmea.todayrecipe.entity.RefreshEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuthRepositoryWithRedis extends CrudRepository<RefreshEntity, String> {

}
