package com.x.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    @Query("SELECT COUNT(e) > 0 FROM #{#entityName} e WHERE e.id = ?1")
    boolean existsById(ID id);

}
