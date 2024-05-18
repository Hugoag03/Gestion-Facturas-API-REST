package com.api.gestion.dao;

import com.api.gestion.pojo.Producto;
import com.api.gestion.wrapper.ProductoWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface ProductoDAO extends JpaRepository<Producto, Integer> {

    List<ProductoWrapper> getAllProductos();

    @Modifying
    @Transactional
    int updateProductoSmthg(@Param("categoriaId") Integer categoriaId, @Param("id") Integer id, @Param("nombre") String nombre);

    @Modifying
    @Transactional
    Integer updateStatus(@Param("status") String status, @Param("id") Integer id);

    Producto findProductoByNombre(@Param("nombre") String nombre);

    // Obtener productos ordenados por fechaCreacion de forma ascendente
    Page<ProductoWrapper> getAllProductosOrderByFechaCreacionASC(Pageable pageable);

    // Obtener productos ordenados por fechaCreacion de forma descendente
    Page<ProductoWrapper> getAllProductosOrderByFechaCreacionDESC(Pageable pageable);

    List<ProductoWrapper> getProductoByCategoria(@Param("id") Integer idCategoria);

    ProductoWrapper getProductoById(@Param("id") Integer id);
}