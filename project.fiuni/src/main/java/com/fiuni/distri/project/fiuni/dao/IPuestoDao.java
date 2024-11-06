package com.fiuni.distri.project.fiuni.dao;

import com.fiuni.distri.project.fiuni.domain.Puesto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPuestoDao extends CrudRepository<Puesto, Integer> {
     Page<Puesto> findAll(Pageable pageable);

     @Query(value = "SELECT * FROM puestos p WHERE p.name = :name", nativeQuery = true)
     Page<Puesto> findPuestosByName(Pageable pageable, @Param("name") String name);
}
