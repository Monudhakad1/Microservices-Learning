package com.usermicro.userservicemicroservices.Repository;

import com.usermicro.userservicemicroservices.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,String> {


}
