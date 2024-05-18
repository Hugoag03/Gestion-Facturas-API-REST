package com.api.gestion.service.impl;

import com.api.gestion.constantes.FacturaConstantes;
import com.api.gestion.dao.CategoriaDAO;
import com.api.gestion.pojo.Categoria;
import com.api.gestion.security.jwt.JwtFilter;
import com.api.gestion.service.CategoriaService;
import com.api.gestion.util.FacturaUtils;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaDAO categoriaDAO;

    @Autowired
    private JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNuevaCategoria(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                //Crearemos un método para validar la categoría
                if (validateCategoriaMap(requestMap)) {
                    categoriaDAO.save(getCategoriaFromMap(requestMap, false));
                    return FacturaUtils.getResponseEntity("Categoria agregada con éxito", HttpStatus.OK);
                }

            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Categoria>> getAllCategorias(String valueFilter) {
        try {
            if (!Strings.isNullOrEmpty(valueFilter) && valueFilter.equalsIgnoreCase("true")) {
                log.info("Usando el método getAllCategorias() de Categoria");
                return new ResponseEntity<List<Categoria>>(categoriaDAO.getAllCategorias(), HttpStatus.OK);
            }
            log.info("Usando el método findAll() de JpaRepository");
            return new ResponseEntity<List<Categoria>>(categoriaDAO.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<List<Categoria>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategoria(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                //Crearemos un método para validar la categoría
                if (validateCategoriaMap(requestMap)) {
                    Categoria categoria = categoriaDAO.findById(Integer.parseInt(requestMap.get("id"))).orElse(null);
                    if (categoria != null) {
                        categoriaDAO.save(getCategoriaFromMap(requestMap, true));
                        return FacturaUtils.getResponseEntity("Categoria actualizada con éxito", HttpStatus.OK);
                    } else {
                        return FacturaUtils.getResponseEntity("La categoria con ese ID no existe", HttpStatus.BAD_REQUEST);
                    }
                }
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    private boolean validateCategoriaMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("nombre")) {
            return true;
        }
        return false;
    }

    private Categoria getCategoriaFromMap(Map<String, String> requestMap, boolean isAdd) {
        Categoria categoria = new Categoria();
        if (isAdd) {
            categoria.setId(Integer.parseInt(requestMap.get("id")));
        }
        categoria.setNombre(requestMap.get("nombre"));
        return categoria;
    }
}
