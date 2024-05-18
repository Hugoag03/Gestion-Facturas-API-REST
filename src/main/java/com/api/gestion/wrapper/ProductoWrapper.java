package com.api.gestion.wrapper;

import com.api.gestion.pojo.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoWrapper {

    private Integer id;
    private String nombre;
    private String descripcion;
    private Integer precio;
    private String status;
    private Integer categoriaId;
    private String nombreCategoria;
    private Date fechaCreacion;

    public ProductoWrapper(Producto producto) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.descripcion = producto.getDescripcion();
        this.precio = producto.getPrecio();
        this.status = producto.getStatus();
        this.categoriaId = producto.getCategoria().getId();
        this.nombreCategoria = producto.getCategoria().getNombre();
        this.fechaCreacion = producto.getFechaCreacion();
    }

    public ProductoWrapper(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }
}
