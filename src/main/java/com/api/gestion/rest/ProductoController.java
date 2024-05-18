package com.api.gestion.rest;

import com.api.gestion.constantes.FacturaConstantes;
import com.api.gestion.pojo.Producto;
import com.api.gestion.service.ProductoService;
import com.api.gestion.util.FacturaUtils;
import com.api.gestion.wrapper.ProductoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping("/add")
    public ResponseEntity<String> agregarNuevoProducto(@RequestBody(required = true) Map<String, String> requestMap) {
        try {
            return productoService.addNuevoProducto(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get")
    public ResponseEntity<List<ProductoWrapper>> listarProductos() {
        try {
            return productoService.getAllProductos();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update")
    public ResponseEntity<String> actualizarProducto(@RequestBody Map<String, String> requestMap){
        try {
            return productoService.updateProducto(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping("/updateSmthg/{categoriaId}/{id}")
    public ResponseEntity<String> updateSmthg(@PathVariable Integer categoriaId, @PathVariable Integer id, @RequestParam String nombre){
        try {
            return productoService.updateProductoSmthg(categoriaId, id, nombre);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProducto(@PathVariable Integer id){
        try {
            return productoService.deleteProducto(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getByNombre")
    public ResponseEntity<Producto> buscarPorNombre(@RequestParam String nombre){
        try{
            return productoService.findProductoByNombre(nombre);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/getPaging")
    public ResponseEntity<Page<ProductoWrapper>> getProductosOrderByFechaCreacion(@RequestParam(defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "10") int size,
                                                                                  @RequestParam(defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size);
        return productoService.getAllProductosOrderByFechaCreacion(pageable, direction);
    }

    @PutMapping("/update/status")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, String> requestMap){
        try {
            return productoService.updateStatus(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.INVALID_DATA, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getByCategoria/{id}")
    public ResponseEntity<List<ProductoWrapper>> listarProductosPorCategoria(@PathVariable Integer id) {
        try {
            return productoService.getProductoByCategoria(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<ProductoWrapper> buscarProductoPorId(@PathVariable Integer id){
        try{
            return productoService.getProductoById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
