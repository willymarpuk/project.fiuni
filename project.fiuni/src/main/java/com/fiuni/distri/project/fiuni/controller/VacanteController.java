package com.fiuni.distri.project.fiuni.controller;

import com.fiuni.distri.project.fiuni.dto.AplicacionVacanteDto;

import com.fiuni.distri.project.fiuni.dto.ResponseDto;
import com.fiuni.distri.project.fiuni.dto.VacanteDto;
import com.fiuni.distri.project.fiuni.service.IAplicacionVacanteService;
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

    @Autowired
    private IAplicacionVacanteService aplicacionVacanteService;

    private static Logger logger = LoggerFactory.getLogger(VacanteController.class);

    @GetMapping({"", "/"})
    @ResponseBody
    //obtiene todos los vacantes
    public ResponseDto<Page<VacanteDto>> getAll(Pageable pageable) {
        logger.info("Obteniendo lista de vacantes");
        try {
            return new ResponseDto<>(200, "Lista de vacantes obtenida", vacanteService.findAllvacantes(pageable));
        }catch (Exception e){
            logger.error("Error al obtener la lista de vacantes", e);
        }
        return new ResponseDto<>(500, "Error al obtener las vacantes", null);
    }

    @GetMapping({"/{id}"})
    @ResponseBody
    //obtiene una vacante por el id
    public ResponseDto<VacanteDto> getById(@PathVariable int id){
        logger.info("Obteniendo una vacante");
        try {
            return new ResponseDto<>(200, "Vacante obtenido", vacanteService.getByid(id));
        }catch (Exception e){
            logger.error("Error al obtener un vacante", e);
        }
        return new ResponseDto<>(500, "Error al obtener la vacante", null);
    }

    @GetMapping({"/{id}/aplicacionVacante"})
    @ResponseBody
    //obtiene los detalles de la vacante con id
    public ResponseDto<Page<AplicacionVacanteDto>> getAplicacionVacante(@PathVariable int id, Pageable pageable){
        logger.info("Obteniendo los detalles de una vacante");
        try{
            return new ResponseDto<>(200, "Detalles de la vacante obtenidos", vacanteService.getAplicacionVacanteByid(id, pageable));
        }catch (Exception e){
            logger.error("Error al obtener los detalles de una vacante", e);
        }
        return new ResponseDto<>(500, "Error al obtener los detalles de la vacante", null);
    }

    @PostMapping({"", "/"})
    @ResponseBody
    //se crea una vacante
    public ResponseDto<VacanteDto> createVacante(@RequestBody VacanteDto vacanteDto){
        logger.info("Creando una vacante");
        try {
            return new ResponseDto<>(201, "Vacante creada", vacanteService.crearVacante(vacanteDto));
        }catch (Exception e){
            logger.error("Error al crear una vacante", e);
        }
        return new ResponseDto<>(500, "Error al crear una vacante", null);
    }

    @PutMapping({"/{id_cabecera}/aplicacionVacante/{id}"})
    //actualiza los detalles de la vacante c/ id
    public ResponseDto<AplicacionVacanteDto> updateAplicacionVacante(@PathVariable Integer id_cabecera, @RequestBody AplicacionVacanteDto apvdto, @PathVariable Integer id){
        logger.info("Actualizando los detalles de una vacante");
        try {
            return new ResponseDto<>(201, "Detalles de la vacante actualizados", vacanteService.actualizarAplicacionVacante(id_cabecera, apvdto, id));
        }catch (Exception e){
            logger.error("Error al actualizar una vacante", e);
        }
        return new ResponseDto<>(500, "Error al actualizar los detalles de una vacante", null);
    }

    // agrega detalles si la cabecera existe.
    @PostMapping({"/{id_cabecera}/aplicacionVacante"})
    public ResponseDto<AplicacionVacanteDto> createAplicacionVacante(@PathVariable Integer id_cabecera, @RequestBody AplicacionVacanteDto apvdto){
        logger.info("Creando detalle de una vacante");
        try {
            return new ResponseDto<>(201, "Detalle de la vacante creado", vacanteService.crearAplicacionVacante(id_cabecera, apvdto));
        }catch (Exception e){
            logger.error("Error al crear detalle de una vacante", e);
        }
        return new ResponseDto<>(500, "Error al crear el detalles de una vacante", null);
    }




    @PutMapping("/{id}")
    //actualiza la cabecera de las vacantes
    public ResponseDto<VacanteDto> updateVacanteCabecera(@PathVariable Integer id, @RequestBody VacanteDto vdto){
        logger.info("Actualizando una cabecera vacante");
        try {
            return new ResponseDto<>(201, "Vacante actualizado", vacanteService.actualizarCabeceraVacante(id, vdto));
        }catch (Exception e){
            logger.error("Error al actualizar una cabecera vacante", e);
        }
        return new ResponseDto<>(500, "Error al actualizar una vacante", null);
    }

    @DeleteMapping("/{id}")
    //borra una cabecera y detalles con id
    public ResponseDto<VacanteDto> deleteVacante(@PathVariable int id){
        logger.info("Eliminando una vacante");
        try {
            vacanteService.deleteVacante(id);
            return new ResponseDto<>(200, "Vacante eliminado", null);
        }catch (Exception e){
            logger.error("Error al eliminar una vacante", e);
        }
        return new ResponseDto<>(500, "Error al eliminar la vacante", null);
    }

    @DeleteMapping("/{idcabecera}/aplicacionVacante/{id}")
    //borra detalle con id de la cabecera idcabecera
    public ResponseDto<VacanteDto> deleteAplicacionVacantebyId(@PathVariable int id, @PathVariable int idcabecera){
        logger.info("Eliminando un detalle de la vacante");
        try {
            aplicacionVacanteService.deleteAplicacionVacante(idcabecera, id);
            return new ResponseDto<>(200, "Detalle de la Vacante eliminado", null);
        }catch (Exception e){
            logger.error("Error al eliminar un detalle de la vacante", e);
        }
        return new ResponseDto<>(500, "Error al eliminar un detalle de la vacante", null);
    }


    @GetMapping({"/{id_cabecera}/aplicacionVacante/{id}"})
    @ResponseBody
    //obtiene un detalle
    public ResponseDto<AplicacionVacanteDto> getAplicacionVacante(@PathVariable int id_cabecera, @PathVariable Integer id){
        logger.info("Obteniendo un detalle");
        try{
            return new ResponseDto<>(200, "Detalles de la vacante obtenido", vacanteService.obtenerAplicacionVacanteByid(id_cabecera, id));
        }catch (Exception e){
            logger.error("Error al obtener el detalle de una vacante", e);
        }
        return new ResponseDto<>(500, "Error al obtener el detalle de la vacante", null);
    }


}
