package com.fiuni.distri.project.fiuni.service;

import com.fiuni.distri.project.fiuni.domain.AplicacionVacante;
import com.fiuni.distri.project.fiuni.domain.Vacante;
import com.fiuni.distri.project.fiuni.dto.AplicacionVacanteDto;
import com.fiuni.distri.project.fiuni.dto.VacanteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IVacanteService {
    Page<VacanteDto> findAllvacantes(Pageable pageable);

    VacanteDto getByid(int id);

    Page<AplicacionVacanteDto> getAplicacionVacanteByid(int id, Pageable pageable);

    void deleteVacante(int id);

    VacanteDto crearVacante(VacanteDto vacante);

    VacanteDto actualizarAplicacionVacante(Integer id, AplicacionVacanteDto apvDto);

    VacanteDto actualizarCabeceraVacante(Integer id, VacanteDto vdto);
}