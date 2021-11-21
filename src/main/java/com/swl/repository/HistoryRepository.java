package com.swl.repository;

import com.swl.models.project.History;
import com.swl.models.project.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

    Optional<List<History>> findAllByColumnId(Integer idColumn);

    @Query(nativeQuery = true, value = "select * from history h join history_collaborators hc on collaborators_id =:idCollaborator")
    Optional<List<History>> findAllByCollaboratorId(@Param("idCollaborator") Integer idCollaborator);
}
