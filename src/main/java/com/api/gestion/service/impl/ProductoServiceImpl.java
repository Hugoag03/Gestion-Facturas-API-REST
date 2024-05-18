package com.api.gestion.service.impl;

import com.api.gestion.constantes.FacturaConstantes;
import com.api.gestion.dao.ProductoDAO;
import com.api.gestion.pojo.Categoria;
import com.api.gestion.pojo.Producto;
import com.api.gestion.security.jwt.JwtFilter;
import com.api.gestion.service.ProductoService;
import com.api.gestion.util.FacturaUtils;
import com.api.gestion.wrapper.ProductoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductoServiceImpl implements ProductoService {


    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private ProductoDAO productoDAO;

    @Override
    public ResponseEntity<String> addNuevoProducto(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductoMap(requestMap)) {
                    productoDAO.save(getProductoFromMap(requestMap, false));
                    return FacturaUtils.getResponseEntity("Producto agregado con éxito", HttpStatus.OK);
                }
                return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.BAD_REQUEST);
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductoWrapper>> getAllProductos() {
        try {
            return new ResponseEntity<>(productoDAO.getAllProductos(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProducto(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                if (validateProductoMap(requestMap)) {
                    Optional<Producto> producto = productoDAO.findById(Integer.parseInt(requestMap.get("id")));
                    if (!producto.isEmpty()) {
                        productoDAO.save(getProductoFromMap(requestMap, true));
                        return FacturaUtils.getResponseEntity("Producto actualizado con éxito", HttpStatus.OK);

                    } else {
                        return FacturaUtils.getResponseEntity("Ese producto no existe", HttpStatus.NOT_FOUND);
                    }
                } else {

                    return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.BAD_REQUEST);

                }
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateProductoSmthg(Integer categoriaId, Integer id, String nombre) {
        try{
            if (jwtFilter.isAdmin()){
                if(categoriaId != null && id != null && nombre != null){
                    Producto producto = productoDAO.findById(id).orElse(null);
                    if(producto != null){

                        productoDAO.updateProductoSmthg(categoriaId, id, nombre);
                        return new ResponseEntity<>("Producto actualizado correctamente", HttpStatus.OK);

                    }else{
                        return new ResponseEntity<>("Ese producto no existe", HttpStatus.BAD_REQUEST);
                    }
                }else{
                    return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.BAD_REQUEST);
                }
            }else{
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> deleteProducto(Integer id) {
        try{
            if (jwtFilter.isAdmin()){
                Optional producto = productoDAO.findById(id);
                if (!producto.isEmpty()){
                    productoDAO.deleteById(id);
                    return FacturaUtils.getResponseEntity("Producto eliminado con éxito", HttpStatus.OK);
                }else{
                    return FacturaUtils.getResponseEntity("Este producto no existe", HttpStatus.BAD_REQUEST);
                }
            }else{
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional producto = productoDAO.findById(Integer.parseInt(requestMap.get("id")));
                if (!producto.isEmpty()) {
                    Producto producto1 = (Producto) producto.get();
                    productoDAO.updateStatus(requestMap.get("status"), producto1.getId());
                    return FacturaUtils.getResponseEntity("Status del producto actualizado con éxito", HttpStatus.OK);
                } else {
                    return FacturaUtils.getResponseEntity("Este producto no existe", HttpStatus.BAD_REQUEST);
                }
            } else {
                return FacturaUtils.getResponseEntity(FacturaConstantes.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<Producto> findProductoByNombre(String nombre) {
        try{
            Producto producto = productoDAO.findProductoByNombre(nombre);

            if (producto != null){
                return new ResponseEntity<>(producto, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    @Override
    public ResponseEntity<Page<ProductoWrapper>> getAllProductosOrderByFechaCreacion(Pageable pageable, String direction) {
        try {
            Page<ProductoWrapper> productosPaginados;
            if ("asc".equalsIgnoreCase(direction)) {
                productosPaginados = productoDAO.getAllProductosOrderByFechaCreacionASC(pageable);
            } else if ("desc".equalsIgnoreCase(direction)) {
                productosPaginados = productoDAO.getAllProductosOrderByFechaCreacionDESC(pageable);
            } else {

                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(productosPaginados, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<List<ProductoWrapper>> getProductoByCategoria(Integer id) {
        try {
            return new ResponseEntity<>(productoDAO.getProductoByCategoria(id), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<ProductoWrapper> getProductoById(Integer id) {
        try{
            ProductoWrapper producto = productoDAO.getProductoById(id);
            if (producto != null){
                return new ResponseEntity<>(producto, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

    }


    private Producto getProductoFromMap(Map<String, String> requestMap, boolean isAdd) {
        Categoria categoria = new Categoria();
        categoria.setId(Integer.parseInt(requestMap.get("categoria_id")));

        Producto producto = new Producto();
        if (isAdd) {
            producto.setId(Integer.parseInt(requestMap.get("id")));
            producto.setStatus("true");
        } else {
            producto.setStatus("true");
        }
        producto.setCategoria(categoria);
        producto.setNombre(requestMap.get("nombre"));
        producto.setPrecio(Integer.parseInt(requestMap.get("precio")));
        producto.setFechaCreacion(new Date());
        producto.setDescripcion(requestMap.get("descripcion"));

        return producto;
    }

    private boolean validateProductoMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("nombre")) {
            return true;
        }
        return false;
    }
}
