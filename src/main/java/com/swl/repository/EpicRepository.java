package com.swl.repository;

import com.swl.models.project.Epic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EpicRepository extends JpaRepository<Epic, Integer> {

}
