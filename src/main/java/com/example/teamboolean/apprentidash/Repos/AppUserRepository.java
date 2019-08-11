package com.example.teamboolean.apprentidash.Repos;

import com.example.teamboolean.apprentidash.Models.AppUser;
import org.springframework.data.repository.CrudRepository;


public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    AppUser findByUsername(String username);

}
