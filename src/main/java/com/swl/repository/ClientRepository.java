package com.swl.repository;

import com.swl.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("select c from Client c where c.user.id= :userId")
    Optional<Client> findClientByUserId(@Param("userId") Integer id);

    Optional<Client> findClientByUserEmail(@Param("userEmail") String email);

}
