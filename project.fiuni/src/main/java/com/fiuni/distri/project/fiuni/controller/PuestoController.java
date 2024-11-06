package com.fiuni.distri.project.fiuni.controller;


import com.fiuni.distri.project.fiuni.dto.PuestoDto;

import com.fiuni.distri.project.fiuni.dto.ResponseDto;
import com.fiuni.distri.project.fiuni.service.IPuestoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.web.servlet.error.ErrorController;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/puestos")
public class PuestoController implements ErrorController {

    @Autowired
    private IPuestoService puestoService;

    private static Logger logger = LoggerFactory.getLogger(PuestoController.class);


    @GetMapping({"", "/"})
    @ResponseBody
    //obtiene todos los puestos
    public ResponseDto<Page<PuestoDto>> getAll(Pageable pageable) {

        logger.info("Obteniendo lista de puestos");
        try {

        return new ResponseDto<>(200, "Puestos obtenidos", puestoService.findAllpuestos(pageable));

        }catch(Exception e) {
            logger.error("Ha ocurrido un error: " + e.getMessage());
        }
        return new ResponseDto<>(404, "No se pudo obtener los puestos,", null);
    }

    @GetMapping({"/{id}"})
    @ResponseBody
    //obtiene un puesto por el id
    public ResponseDto<PuestoDto> getById(@PathVariable int id){

        logger.info("Obteniendo un puesto");
        try{

        return new ResponseDto<>(200,"Puesto Obtenido", puestoService.getByid(id));

        }catch(Exception e) {
            logger.error("Ha ocurrido un error: " + e.getMessage());
        }
        return new ResponseDto<>(404, "No se encontro el Puesto", null);
    }

    @GetMapping({"/buscar={name}"})
    @ResponseBody
    //obtiene los puestos por el nombre
    public ResponseDto<Page<PuestoDto>> getByName(Pageable pageable, @PathVariable String name){
        logger.info("Obteniendo un puesto por el nombre");
        logger.info("El nombre es: {}",name);
      ;  try {
            return new ResponseDto<>(200, "Puesto Obtenido", puestoService.findAllPuestosByName(pageable, name));
        }catch (Exception e){
            logger.error("Ha ocurrido un error: " + e.getMessage());
        }
        return new ResponseDto<>(404, "No se encontro el Puesto", null);
    }

    @PostMapping({"", "/"})
    @ResponseBody
    // crea un puesto
    public ResponseDto<PuestoDto> createPuesto(@RequestBody PuestoDto puestoDto){
        logger.info("Creando un puesto");
        try {

        return new ResponseDto<>(201, "Puesto Creado",puestoService.crearPuesto(puestoDto));

        }catch(Exception e) {
            logger.error("Ha ocurrido un error: " + e.getMessage());
        }
        return new ResponseDto<>(500, "Error al crear un puesto", null);
    }

    @PutMapping({"/{id}/update"})
    //actualiza un puesto
    public ResponseDto<PuestoDto> updatePuesto(@RequestBody PuestoDto puestoDto, @PathVariable int id){
        logger.info("Actualizando un puesto");
        try {
            return new ResponseDto<>(201, "El puesto se ha actualizado", puestoService.updatePuesto(puestoDto, id));
        }catch(Exception e) {
            logger.error("Ha ocurrido un error: " + e.getMessage());
        }
        return new ResponseDto<>(500, "Error al actualizar un puesto", null);
    }

    @DeleteMapping("/{id}")
    //borra un puesto
    public ResponseDto<PuestoDto> deletePuesto(@PathVariable int id){
        logger.info("Eliminando un puesto");
        try {

            puestoService.deletePuesto(id);
            return new ResponseDto<>(200, "Puesto Eliminado", null);

        }catch(Exception e) {
            logger.error("Ha ocurrido un error: " + e.getMessage());
        }
        return new ResponseDto<>(500, "Error al eliminar un puesto", null);
    }

}