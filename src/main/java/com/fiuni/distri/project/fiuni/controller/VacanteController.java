package com.fiuni.distri.project.fiuni.controller;

import com.fiuni.distri.project.fiuni.dto.AplicacionVacanteDto;

import com.fiuni.distri.project.fiuni.dto.ResponseDto;
import com.fiuni.distri.project.fiuni.dto.VacanteDto;
import com.fiuni.distri.project.fiuni.service.IVacanteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vacantes")
public class VacanteController {

    @Autowired
    private IVacanteService vacanteService;

    private static Logger logger = LoggerFactory.getLogger(PuestoController.class);

    @GetMapping({"", "/"})
    @ResponseBody
    //obtiene todos los vacantes
    public ResponseDto<Page<VacanteDto>> getAll(Pageable pageable) {
        logger.info("Obteniendo lista de vacantes");
        try {
            return new ResponseDto<>(200, true, "Lista de vacantes obtenida", vacanteService.findAllvacantes(pageable), null);
        }catch (Exception e){
            logger.error("Error al obtener la lista de vacantes", e);
            String[] error = {e.getMessage()};
            return new ResponseDto<>(500, false, "Error al obtener las vacantes", null, error);
        }
    }

    @GetMapping({"/{id}"})
    @ResponseBody
    //obtiene una vacante por el id
    public ResponseDto<VacanteDto> getById(@PathVariable int id){
        logger.info("Obteniendo una vacante");
        try {
            return new ResponseDto<>(200,true, "Vacante obtenido", vacanteService.getByid(id), null);
        }catch (Exception e){
            logger.error("Error al obtener un vacante", e);
            String[] error = {e.getMessage()};
            return new ResponseDto<>(500, false, "Error al obtener la vacante", null, error);
        }
    }

    @GetMapping({"/{id}/aplicacionVacante"})
    @ResponseBody
    //obtiene los detalles de la vacante con id
    public ResponseDto<Page<AplicacionVacanteDto>> getAplicacionVacante(@PathVariable int id, Pageable pageable){
        logger.info("Obteniendo los detalles de una vacante");
        try{
            return new ResponseDto<>(200, true, "Detalles de la vacante obtenidos", vacanteService.getAplicacionVacanteByid(id, pageable), null);
        }catch (Exception e){
            logger.error("Error al obtener los detalles de una vacante", e);
            String[] error = {e.getMessage()};
            return new ResponseDto<>(500, false, "Error al obtener los detalles de la vacante", null, error);
        }
    }

    @PostMapping({"", "/"})
    @ResponseBody
    //se crea una vacante
    public ResponseDto<VacanteDto> createVacante(@RequestBody VacanteDto vacanteDto){
        logger.info("Creando una vacante");
        try {
            return new ResponseDto<>(200, true, "Vacante creada", vacanteService.crearVacante(vacanteDto), null);
        }catch (Exception e){
            logger.error("Error al crear una vacante", e);
            String[] error = {e.getMessage()};
            return new ResponseDto<>(500, false, "Error al crear una vacante", null, error);
        }
    }

    @PutMapping({"/{id}/aplicacionVacante"})
    //actualiza los detalles de la vacante c/ id
    // puede agregar detalles si la cabecera existe.
    public ResponseDto<VacanteDto> updateAplicacionVacante(@PathVariable Integer id, @RequestBody AplicacionVacanteDto apvdto){
        logger.info("Actualizando los detalles de una vacante");
        try {
            return new ResponseDto<>(200, true, "Detalles de la vacante actualizados", vacanteService.actualizarAplicacionVacante(id, apvdto), null);
        }catch (Exception e){
            logger.error("Error al actualizar una vacante", e);
            String[] error = {e.getMessage()};
            return new ResponseDto<>(500, false, "Error al actualizar los detalles de una vacante", null, error);
        }
    }

    @PutMapping("/{id}")
    //actualiza la cabecera de las vacantes
    public ResponseDto<VacanteDto> updateVacanteCabecera(@PathVariable Integer id, @RequestBody VacanteDto vdto){
        logger.info("Actualizando una cabecera vacante");
        try {
            return new ResponseDto<>(200, true, "Vacante actualizado", vacanteService.actualizarCabeceraVacante(id, vdto), null);
        }catch (Exception e){
            logger.error("Error al actualizar una cabecera vacante", e);
            String[] error = {e.getMessage()};
            return new ResponseDto<>(500, false, "Error al actualizar una vacante", null, error);
        }
    }

    @DeleteMapping("/{id}")
    //borra una cabecera y detalles con id
    public ResponseDto<VacanteDto> deleteVacante(@PathVariable int id){
        logger.info("Eliminando una vacante");
        try {
            vacanteService.deleteVacante(id);
            return new ResponseDto<>(200, true, "Vacante eliminado", null, null);
        }catch (Exception e){
            logger.error("Error al eliminar una vacante", e);
            String[] error = {e.getMessage()};
            return new ResponseDto<>(500, true,"Error al eliminar la vacante", null, null);
        }
    }
}
