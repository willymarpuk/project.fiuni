
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

            return new ResponseDto<>(200, true, "Puestos obtenidos", puestoService.findAllpuestos(pageable), null);

        }catch(Exception e) {
            logger.error("Ha ocurrido un error: " + e.getMessage());
            String[] error = {e.getMessage()};
            return new ResponseDto<>(404, false, "No se pudo obtener los puestos,", null, error );
        }

    }

    @GetMapping({"/{id}"})
    @ResponseBody
    //obtiene un puesto por el id
    public ResponseDto<PuestoDto> getById(@PathVariable int id){

        logger.info("Obteniendo un puesto");
        try{

            return new ResponseDto<>(200,true, "Puesto Obtenido", puestoService.getByid(id), null);

        }catch(Exception e) {
            logger.error("Ha ocurrido un error: " + e.getMessage());
            String[] error = {e.getMessage()};
            return new ResponseDto<>(404, false, "No se encontro el Puesto", null, error);
        }
    }

    @GetMapping({"/buscar={name}"})
    @ResponseBody
    //obtiene los puestos por el nombre
    public ResponseDto<Page<PuestoDto>> getByName(Pageable pageable, @PathVariable String name){
        logger.info("Obteniendo un puesto por el nombre");
        logger.info("El nombre es: {}",name);
        ;  try {
            String buscador = name.replace("+", " ");
            return new ResponseDto<>(200, true, "Puesto Obtenido", puestoService.findAllPuestosByName(pageable, buscador), null);
        }catch (Exception e){
            logger.error("Ha ocurrido un error: " + e.getMessage());
            String[] error = {e.getMessage()};
            return new ResponseDto<>(404, false, "No se encontro el Puesto", null, error);
        }
    }

    @PostMapping({"", "/"})
    @ResponseBody
    // crea un puesto
    public ResponseDto<PuestoDto> createPuesto(@RequestBody PuestoDto puestoDto){
        logger.info("Creando un puesto");
        try {

            return new ResponseDto<>(200, true, "Puesto Creado", puestoService.crearPuesto(puestoDto), null);

        }catch(Exception e) {
            logger.error("Ha ocurrido un error: " + e.getMessage());
            String[] error = {e.getMessage()};
            return new ResponseDto<>(500, false,"Error al crear un puesto", null, error);
        }

    }

    @PutMapping({"/{id}/update"})
    //actualiza un puesto
    public ResponseDto<PuestoDto> updatePuesto(@RequestBody PuestoDto puestoDto, @PathVariable int id){
        logger.info("Actualizando un puesto");
        try {
            return new ResponseDto<>(200, true, "El puesto se ha actualizado", puestoService.updatePuesto(puestoDto, id), null);
        }catch(Exception e) {
            logger.error("Ha ocurrido un error: " + e.getMessage());
            String[] error = {e.getMessage()};
            return new ResponseDto<>(500, false, "Error al actualizar un puesto", null, error);
        }
    }

    @DeleteMapping("/{id}")
    //borra un puesto
    public ResponseDto<PuestoDto> deletePuesto(@PathVariable int id){
        logger.info("Eliminando un puesto");
        try {

            puestoService.deletePuesto(id);
            return new ResponseDto<>(200, true, "Puesto Eliminado", null, null);

        }catch(Exception e) {
            logger.error("Ha ocurrido un error: " + e.getMessage());
            String[] error = {e.getMessage()};
            return new ResponseDto<>(500, false, "Error al eliminar un puesto", null, error);
        }

    }

}
