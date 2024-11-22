package com.fiuni.distri.project.fiuni.dao;


import com.fiuni.distri.project.fiuni.domain.Vacante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVacanteDao extends CrudRepository<Vacante, Integer> {
    Page<Vacante> findAll(Pageable pageable);
}
