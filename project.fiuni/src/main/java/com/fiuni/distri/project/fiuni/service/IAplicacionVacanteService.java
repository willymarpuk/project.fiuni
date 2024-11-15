package com.fiuni.distri.project.fiuni.service;



import com.fiuni.distri.project.fiuni.dto.AplicacionVacanteDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAplicacionVacanteService {

    AplicacionVacanteDto actualizarAplicacionVacante(Integer id_cabecera, AplicacionVacanteDto apvDto, Integer id);

    Page<AplicacionVacanteDto> getAplicacionVacanteByid(int id, Pageable pageable);

    void deleteAplicacionVacante(int idcabecera, int id);

    AplicacionVacanteDto creaAplicacionVacante(Integer idCabecera, AplicacionVacanteDto apvDto);

    AplicacionVacanteDto obtenerAplicacionVacanteById(Integer idCabecera, Integer id);
}
