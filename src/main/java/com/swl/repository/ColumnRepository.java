package com.swl.repository;

import com.swl.models.project.Columns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ColumnRepository extends JpaRepository<Columns, Integer> {

}
