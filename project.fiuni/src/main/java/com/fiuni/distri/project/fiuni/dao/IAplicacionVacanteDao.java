package com.fiuni.distri.project.fiuni.dao;



import com.fiuni.distri.project.fiuni.domain.AplicacionVacante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAplicacionVacanteDao extends CrudRepository<AplicacionVacante, Integer> {

    @Query(value = "SELECT * FROM aplicacion_vacante ap WHERE ap.vacante_id = :id", nativeQuery = true)
    Page<AplicacionVacante> findAplicacionVacanteById(@Param("id") Integer id, Pageable pageable);

    @Query(value = "SELECT * FROM aplicacion_vacante ap WHERE ap.vacante_id = :id", nativeQuery = true)
    List<AplicacionVacante> findAplicacionVacanteById(@Param("id") Integer id);
}
