package com.fiuni.distri.project.fiuni.service;


import com.fiuni.distri.project.fiuni.dto.PuestoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPuestoService {

    Page<PuestoDto> findAllpuestos(Pageable pageable);

    PuestoDto getByid(int id);

    PuestoDto crearPuesto(PuestoDto puestoDto);

    PuestoDto updatePuesto(PuestoDto puestoDto, Integer id);

    void deletePuesto(int id);

    Page<PuestoDto> findAllPuestosByName(Pageable pageable, String name);
}
