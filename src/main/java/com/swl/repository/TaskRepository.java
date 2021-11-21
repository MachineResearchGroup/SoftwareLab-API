package com.swl.repository;

import com.swl.models.project.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    Optional<List<Task>> findAllBySuperTaskId(Integer idSuperTask);

    Optional<List<Task>> findAllByColumnId(Integer idColumn);

    Optional<List<Task>> findAllByHistoryId(Integer idHistory);

    @Query(nativeQuery = true, value = "select * from task t join task_collaborators tc on collaborators_id =:idCollaborator")
    Optional<List<Task>> findAllByCollaboratorId(@Param("idCollaborator") Integer idCollaborator);
}
