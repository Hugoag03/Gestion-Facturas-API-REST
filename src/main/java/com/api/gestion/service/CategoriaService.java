package com.api.gestion.service;

import com.api.gestion.pojo.Categoria;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoriaService {

    ResponseEntity<String> addNuevaCategoria(Map<String, String> requestMap);

    ResponseEntity<List<Categoria>> getAllCategorias(String valueFilter);

    ResponseEntity<String> updateCategoria(Map<String, String> requestMap);
}
