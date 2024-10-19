package com.yard.ms_security.Repositories;

import com.yard.ms_security.Models.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface PermissionRepository extends MongoRepository<Permission, String> {
    @Query("{'url':?0,'method':?1}") //Se hacen la consultas a MongoDB
    Permission getPermission(String url,
                             String method);
}
