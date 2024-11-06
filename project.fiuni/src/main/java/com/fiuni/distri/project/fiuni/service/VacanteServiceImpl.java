package com.fiuni.distri.project.fiuni.service;

import com.fiuni.distri.project.fiuni.dao.IAplicacionVacanteDao;
import com.fiuni.distri.project.fiuni.dao.IPuestoDao;
import com.fiuni.distri.project.fiuni.dao.IVacanteDao;
import com.fiuni.distri.project.fiuni.domain.AplicacionVacante;
import com.fiuni.distri.project.fiuni.domain.Vacante;
import com.fiuni.distri.project.fiuni.dto.AplicacionVacanteDto;
import com.fiuni.distri.project.fiuni.dto.VacanteDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
//@Transactional
public class VacanteServiceImpl implements IVacanteService{
    @Autowired
    private IVacanteDao vacanteDao;
    @Autowired
    private IPuestoDao puestoDao;
    @Autowired
    private IAplicacionVacanteDao aplicacionVacanteDao;
    @Autowired
    private IAplicacionVacanteService aplicacionVacanteService;

    @Autowired
    private CacheManager cacheManager;

    private static Logger logger = LoggerFactory.getLogger(VacanteServiceImpl.class);

    @Override
    public Page<VacanteDto> findAllvacantes(Pageable pageable) {
        List<VacanteDto> vacanteDtos = null;
        logger.info("Buscando vacantes");
        try {

            Page<Vacante> vacantes = vacanteDao.findAll(pageable);
            vacanteDtos = new ArrayList<>();
            List<Vacante> vacanteList = vacantes.getContent();
            for (Vacante vacante : vacanteList) {
                VacanteDto vacanteDto = convertDOMAINtoDTO(vacante);
                vacanteDtos.add(vacanteDto);
            }

            vacanteDtos.forEach(vacanteDto -> {

                Objects.requireNonNull(cacheManager.getCache("Vacante")).put(vacanteDto.getId(), vacanteDto);
            });
            logger.info("Vacantes Cacheados");
        } catch (Exception e) {
            logger.error("Ha ocurrido un error al obtener las vacantes", e);
        }

        return new PageImpl<>(vacanteDtos);
    }

    @Override
    @Cacheable(value = "Vacante", key = "#id")
    public VacanteDto getByid(int id) {
        logger.info("Buscando vacante");
        Vacante v = null;
        try {
            v = vacanteDao.findById(id).get();
            logger.info("Vacante Cacheado");
        } catch (Exception e) {
            logger.error("Ha ocurrido un error al obtener la vacante", e);
        }
        return convertDOMAINtoDTO(v);
    }

    @Override
    public Page<AplicacionVacanteDto> getAplicacionVacanteByid(int id, Pageable pageable) {
       
        return aplicacionVacanteService.getAplicacionVacanteByid( id, pageable);

    }

    @Override
    @Cacheable(value = "Vacante", key = "#vacanteDto.id")
    public VacanteDto crearVacante(VacanteDto vacanteDto){
        logger.info("Creando Vacante");
        try {

        Vacante vacante = convertDtoToDOMAIN(vacanteDto);
        vacanteDao.save(vacante);
        logger.info("Vacante Cacheado");
        }catch (Exception e){
            logger.error("Ha ocurrido un error al crear la vacante", e);
        }
        return vacanteDto;
    }

    @Override
    @CachePut(value = "Vacante", key = "#vdto.id")
    public VacanteDto actualizarCabeceraVacante(Integer id, VacanteDto vdto) {
        logger.info("Actualizando Vacante");
        Vacante v = null;
        try {

            v = vacanteDao.findById(id).get();
            v.setPuesto(puestoDao.findById(vdto.getPuesto_id()).get());
            v.setDescripcion(vdto.getDescripcion());
            v.setEsta_disponible(vdto.isEsta_disponible());
            vacanteDao.save(v);

            logger.info("Vacante Cacheado");

        } catch (Exception e) {
            logger.error("Ha ocurrido un error al actualizar la vacante", e);
        }
        return convertDOMAINtoDTO(v);
    }



    @Override
    public VacanteDto actualizarAplicacionVacante(Integer id, AplicacionVacanteDto apvDto) {
        
        aplicacionVacanteService.crearActualizarAplicacionVacante(id, apvDto);
        Vacante v = vacanteDao.findById(apvDto.getVacante_id()).get();
        VacanteDto vdto = convertDOMAINtoDTO(v);
        return vdto;
    }

    @Override
    public void deleteVacante(int id) {
        logger.info("Eliminando Vacante");
        try {

        Vacante v = vacanteDao.findById(id).get();
        List<AplicacionVacante> av = aplicacionVacanteDao.findAplicacionVacanteById(v.getId());

        aplicacionVacanteDao.deleteAll(av);
        vacanteDao.delete(v);

        av.forEach(vdto -> {
            Objects.requireNonNull(cacheManager.getCache("AplicacionVacante")).evict(vdto.getId());
        });

        Objects.requireNonNull(cacheManager.getCache("Vacante")).evict(v.getId());
        logger.info("Vacante y sus detalles eliminados del Cache");

        }catch (Exception e){
            logger.error("Ha ocurrido un error al eliminar la vacante", e);
        }
    }


    private VacanteDto convertDOMAINtoDTO(Vacante domain) {
        logger.info("Convirtiendo la Vacante a VacanteDTO");
        VacanteDto vdto = null;
        try {

            vdto = new VacanteDto();
            vdto.setId(domain.getId());
            vdto.setDescripcion(domain.getDescripcion());
            vdto.setPuesto_id(domain.getPuesto().getId());
            vdto.setEsta_disponible(domain.getEsta_disponible());
        } catch (Exception e) {
            logger.error("Ha ocurrido un error al convertir el Entity a Dto", e);
        }
        return vdto;
    }

    private Vacante convertDtoToDOMAIN(VacanteDto dto) {
        logger.info("Convirtiendo la VacanteDto a Vacante");
        Vacante v = null;
        try {

            v = new Vacante();
            v.setId(dto.getId());
            v.setDescripcion(dto.getDescripcion());
            v.setPuesto(puestoDao.findById(dto.getPuesto_id()).get());
            v.setEsta_disponible(dto.isEsta_disponible());
        } catch (Exception e) {
            logger.error("Ha ocurrido un error al convertir el Dto a Entity", e);
        }
        return v;
    }
}
