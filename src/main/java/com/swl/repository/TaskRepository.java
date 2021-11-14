package com.swl.repository;

import com.swl.models.project.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    Optional<List<Task>> findAllBySuperTaskId(Integer idSuperTask);

    Optional<List<Task>> findAllByColumnId(Integer idColumn);
}
