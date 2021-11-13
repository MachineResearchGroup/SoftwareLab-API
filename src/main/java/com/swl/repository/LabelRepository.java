package com.swl.repository;

import com.swl.models.project.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LabelRepository extends JpaRepository<Label, Integer> {

}
