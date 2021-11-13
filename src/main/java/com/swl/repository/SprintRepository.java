package com.swl.repository;

import com.swl.models.project.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SprintRepository extends JpaRepository<Sprint, Integer> {

}
