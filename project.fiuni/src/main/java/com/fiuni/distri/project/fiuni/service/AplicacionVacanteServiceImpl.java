package com.fiuni.distri.project.fiuni.service;


import com.fiuni.distri.project.fiuni.dao.IAplicacionVacanteDao;
import com.fiuni.distri.project.fiuni.dao.IEmpleadoDao;
import com.fiuni.distri.project.fiuni.dao.IVacanteDao;
import com.fiuni.distri.project.fiuni.domain.AplicacionVacante;
import com.fiuni.distri.project.fiuni.domain.Vacante;
import com.fiuni.distri.project.fiuni.dto.AplicacionVacanteDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
//@Transactional
public class AplicacionVacanteServiceImpl implements IAplicacionVacanteService {
    @Autowired
    private IVacanteDao vacanteDao;

    @Autowired
    private IAplicacionVacanteDao aplicacionVacanteDao;

    @Autowired
    private IEmpleadoDao empleadoDao;

    @Autowired
    private CacheManager cacheManager;

    private static Logger logger = LoggerFactory.getLogger(AplicacionVacanteServiceImpl.class);


    @Override
    public AplicacionVacanteDto actualizarAplicacionVacante(Integer id_cabecera, AplicacionVacanteDto apvDto, Integer id) {
        logger.info("Actualizando detalle del Vacante");
        Vacante vacante = null;
        AplicacionVacante aplicacionVacante = null;
        try {


            vacante = vacanteDao.findById(id_cabecera).get();
            aplicacionVacante = aplicacionVacanteDao.findById(id).get();
            if (vacante.getId() == aplicacionVacante.getVacante().getId() && aplicacionVacante.getId() == apvDto.getId()) {
                AplicacionVacante apv = convertDtoToDOMAIN(apvDto);
                aplicacionVacanteDao.save(apv);
            

                Objects.requireNonNull(cacheManager.getCache("AplicacionVacante")).evict(apvDto.getId());
                Objects.requireNonNull(cacheManager.getCache("AplicacionVacante")).put(apvDto.getId(), apvDto);
                logger.info("AplicacionVacante Cacheado");
            }
        } catch (Exception e) {
            logger.error("Ha ocurrido un error al actualizar el detalle la vacante", e);
        }
        return apvDto;
    }


    @Override
    public Page<AplicacionVacanteDto> getAplicacionVacanteByid(int id, Pageable pageable) {
        logger.info("Buscando detalles de la vacante");
        List<AplicacionVacanteDto> apvpDtos = null;
        try {

            Vacante v = vacanteDao.findById(id).get();
            Page<AplicacionVacante> apvp = aplicacionVacanteDao.findAplicacionVacanteById(v.getId(), pageable);

            apvpDtos = new ArrayList<>();
            List<AplicacionVacante> apvpList = apvp.getContent();
            for (AplicacionVacante avp : apvpList) {
                AplicacionVacanteDto vdto = convertDOMAINtoDTO(avp);
                apvpDtos.add(vdto);
            }

            apvpDtos.forEach(vdto -> {
                  Objects.requireNonNull(cacheManager.getCache("AplicacionVacante")).put(vdto.getId(), vdto);
            });

            logger.info("Detalles de la vacante Cacheados");
        } catch (Exception e) {
            logger.error("Ha ocurrido un error al obtener los detalles de la vacante", e);
        }

        return new PageImpl<>(apvpDtos);
    }

    @Override
    public void deleteAplicacionVacante(int idcabecera, int id) {
        logger.info("Borrando detalle de una cabecera");
        try {
            List<AplicacionVacante> apv = aplicacionVacanteDao.findAplicacionVacanteById(idcabecera);
            AplicacionVacante borrar = apv.get(id);
            aplicacionVacanteDao.delete(borrar);
            logger.info("Borrando detalle de una cabecera");

        }catch (Exception e) {
            logger.error("Ocurrio un error al borrar el detalle de una cabecera");
        }
    }

    @Override
    public AplicacionVacanteDto creaAplicacionVacante(Integer idCabecera, AplicacionVacanteDto apvDto) {
        logger.info("Creando detalle del Vacante");
        Vacante vacante = null;
        try {

            vacante = vacanteDao.findById(idCabecera).get();
            if (apvDto.getVacante_id() == vacante.getId()) {
                AplicacionVacante apv = convertDtoToDOMAIN(apvDto);
                aplicacionVacanteDao.save(apv);


                Objects.requireNonNull(cacheManager.getCache("AplicacionVacante")).evict(apvDto.getId());
                Objects.requireNonNull(cacheManager.getCache("AplicacionVacante")).put(apvDto.getId(), apvDto);
                logger.info("AplicacionVacante Cacheado");
            }
        } catch (Exception e) {
            logger.error("Ha ocurrido un error al crear el detalle la vacante", e);
        }
        return apvDto;
    }

    @Override
    public AplicacionVacanteDto obtenerAplicacionVacanteById(Integer idCabecera, Integer id) {

        AplicacionVacanteDto apvDto = null;

       List<AplicacionVacante> avl = aplicacionVacanteDao.findAplicacionVacanteById(idCabecera);

       apvDto = convertDOMAINtoDTO(avl.get(id));


        return apvDto;
    }


    private AplicacionVacanteDto convertDOMAINtoDTO(AplicacionVacante avp) {
        logger.info("Convirtiendo la aplicacionvacante a aplicacionVacanteDTO");
        AplicacionVacanteDto dto = null;
        try {

            dto = new AplicacionVacanteDto();
            dto.setId(avp.getId());
            dto.setVacante_id(avp.getVacante().getId());
            dto.setCv(avp.getCv());
            dto.setEncargado_id(avp.getEncargado().getId());
            dto.setFue_revisado(avp.getFue_revisado());

        } catch (Exception e) {
            logger.error("Ha ocurrido un error al convertir el Entity a Dto");
        }
        return dto;
    }

    private AplicacionVacante convertDtoToDOMAIN(AplicacionVacanteDto apvDto) {
        logger.info("convirtiendo la aplicacionvacanteDto a AplicacionVacante");
        AplicacionVacante avp = null;
        try {

            avp = new AplicacionVacante();
            avp.setId(apvDto.getId());
            avp.setCv(apvDto.getCv());

            Vacante vacante = vacanteDao.findById(apvDto.getVacante_id()).get();
            avp.setVacante(vacante);
            avp.setFue_revisado(apvDto.isFue_revisado());
            avp.setEncargado(empleadoDao.findById(apvDto.getEncargado_id()).get());

        } catch (Exception e) {
            logger.error("Ha ocurrido un error al convertir el Dto a Entity");
        }
        return avp;

    }
}
