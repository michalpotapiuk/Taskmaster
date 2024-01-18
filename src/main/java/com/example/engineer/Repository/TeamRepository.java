package com.example.engineer.Repository;

import com.example.engineer.Model.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {

    @Query("select t from TeamEntity t where t.name=:tName")
    Optional<TeamEntity> findByName(@Param("tName") String name);

    @Query("select distinct t from TeamEntity t join fetch t.users where t.id in (select t2.id from TeamEntity t2 join t2.users m where m.id = :userId)")
    Set<TeamEntity> findTeamEntitiesByUser(@Param("userId") Long userId);

}
