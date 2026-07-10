package com.sbmvcprojects.mvcendtoend.repository;

import com.sbmvcprojects.mvcendtoend.entity.UserEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
}
