package com.api.gestion.service;

import com.api.gestion.pojo.Producto;
import com.api.gestion.wrapper.ProductoWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface ProductoService {

    ResponseEntity<String> addNuevoProducto(Map<String, String> requestMap);

    ResponseEntity<List<ProductoWrapper>> getAllProductos();

    ResponseEntity<String> updateProducto(Map<String, String> requestMap);

    ResponseEntity<String> updateProductoSmthg(Integer categoriaId, Integer id, String nombre);

    ResponseEntity<String> deleteProducto(Integer id);

    ResponseEntity<String> updateStatus(Map<String, String> requestMap);

    ResponseEntity<Producto> findProductoByNombre(String nombre);

    ResponseEntity<Page<ProductoWrapper>> getAllProductosOrderByFechaCreacion(Pageable pageable, String direction);

    ResponseEntity<List<ProductoWrapper>> getProductoByCategoria(Integer id);

    ResponseEntity<ProductoWrapper> getProductoById(Integer id);
}
