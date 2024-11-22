package com.fiuni.distri.project.fiuni.service;

import com.fiuni.distri.project.fiuni.dao.IPuestoDao;

import com.fiuni.distri.project.fiuni.domain.Puesto;
import com.fiuni.distri.project.fiuni.dto.PuestoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
public class PuestoServiceImpl implements IPuestoService{
    @Autowired
    private IPuestoDao puestoDao;

    private static Logger logger = LoggerFactory.getLogger(PuestoServiceImpl.class);

    @Autowired
    private CacheManager cacheManager;

    @Override
    public Page<PuestoDto> findAllpuestos(Pageable pageable) {
        List<PuestoDto> puestoDtos = new ArrayList<>();
        try {
            logger.info("Buscando los puestos");
        Page<Puesto> p = puestoDao.findAll(pageable);
       List<Puesto> puestos = p.getContent();

       puestos.forEach(puesto -> {
           puestoDtos.add(convertDOMAINtoDTO(puesto));

       });

       puestoDtos.forEach(puestoDto -> {

           Objects.requireNonNull(cacheManager.getCache("Puesto")).put(puestoDto.getId(), puestoDto);

       });
            logger.info("Puestos Cacheados");

       }catch(Exception e) {
           logger.error("Ha ocurrido un error al obtener la lista de puestos", e);
       }
        return new PageImpl<>(puestoDtos);
    }

    @Override
    @Cacheable(value = "Puesto", key ="#id")
    public PuestoDto getByid(int id) {
        logger.info("Buscando un puesto");
        Puesto p = null;
        try {
            p = puestoDao.findById(id).get();
            logger.info("Puesto Cacheado");
        } catch (Exception e) {
            logger.error("Ha ocurrido un error al obtener el objeto", e);
        }

        return convertDOMAINtoDTO(p);
    }



    @Override
    @Cacheable(value = "Puesto", key ="#puestoDto.id")
    public PuestoDto crearPuesto(PuestoDto puestoDto) {
        logger.info("Creando un puesto");
        try {

            Puesto p = convertDTOtoDomain(puestoDto);
            puestoDao.save(p);
            logger.info("Puesto Cacheado");
        }catch(Exception e) {
            logger.error("Ha ocurrido un error al crear el objeto", e);
        }
        return puestoDto;

    }

    @Override
    @CachePut(value = "Puesto", key ="#puestoDto.id")
    public PuestoDto updatePuesto(PuestoDto puestoDto, Integer id) {
        logger.info("Actualizando un puesto");
        try {
            if (puestoDto.getId() == id){
                Puesto p = convertDTOtoDomain(puestoDto);
                puestoDao.save(p);
                logger.info("Puesto Cacheado");
                return puestoDto;
            }
        }catch(Exception e) {
            logger.error("Ha ocurrido un error al actualizar el puesto", e);
        }
        return null;
    }

    @Override
    @CacheEvict(value = "Puesto", key = "#id")
    public void deletePuesto(int id) {
        logger.info("Eliminando un puesto");
        try {
            puestoDao.deleteById(id);
            logger.info("Puesto Elimidado del Cache");
        }catch(Exception e) {
            logger.error("Ha ocurrido un error al borrar el objeto", e);
        }
    }

    @Override
    public Page<PuestoDto> findAllPuestosByName(Pageable pageable, String name) {
        logger.info("Buscando todos los puestos por nombre");
        Page<Puesto> p = null;
        List<PuestoDto> puestoDtos = null;
        try {
            p = puestoDao.findPuestosByName(pageable, name);
            List<Puesto> puestos = p.getContent();
            puestoDtos = new ArrayList<>();
            List<PuestoDto> finalPuestoDtos = puestoDtos;
            puestos.forEach(puesto -> {
                finalPuestoDtos.add(convertDOMAINtoDTO(puesto));
            });
            finalPuestoDtos.forEach(puestoDto -> {

                  Objects.requireNonNull(cacheManager.getCache("Puesto")).put(puestoDto.getId(), puestoDto);

            });
            logger.info("Puestos Cacheados");

        } catch (Exception e) {
            logger.error("Ha ocurrido un error al obtener los puestos", e);
        }
        return new PageImpl<>(puestoDtos);
    }


    private PuestoDto convertDOMAINtoDTO(Puesto domain) {
        logger.info("Convirtiendo el Puesto a PuestoDTO");
        PuestoDto pdto = null;
        try {

            pdto = new PuestoDto();
            pdto.setId(domain.getId());
            pdto.setNombre(domain.getName());
            pdto.setSueldo(domain.getSueldo());


        } catch (Exception e) {
            logger.error("Ha ocurrido un error al convertir el Entity a Dto", e);
        }
        return pdto;
    }

    private Puesto convertDTOtoDomain(PuestoDto pdto) {
        logger.info("Convirtiendo el PuestoDto a Puesto");
        Puesto p = null;
        try {


            p = new Puesto();
            p.setId(pdto.getId());
            p.setName(pdto.getNombre());
            p.setSueldo(pdto.getSueldo());


        } catch (Exception e) {
            logger.error("Ha ocurrido un error al convertir el Dto a Entity", e);
        }
        return p;
    }
}
