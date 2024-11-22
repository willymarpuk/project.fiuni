package com.fiuni.distri.project.fiuni.service;



import com.fiuni.distri.project.fiuni.domain.AplicacionVacante;
import com.fiuni.distri.project.fiuni.dto.AplicacionVacanteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAplicacionVacanteService {

    AplicacionVacanteDto crearActualizarAplicacionVacante(Integer id, AplicacionVacanteDto apvDto);

    Page<AplicacionVacanteDto> getAplicacionVacanteByid(int id, Pageable pageable);
}
