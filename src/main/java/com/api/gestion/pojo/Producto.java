package com.api.gestion.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@NamedQuery(name = "Producto.getAllProductos", query = "select new com.api.gestion.wrapper.ProductoWrapper(p.id, p.nombre, p.descripcion, p.precio, p.status, p.categoria.id, p.categoria.nombre) from Producto p")
@NamedQuery(name = "Producto.updateProductoSmthg", query = "UPDATE Producto p SET p.categoria.id = :categoriaId, p.nombre = :nombre WHERE p.id = :id")
@NamedQuery(name = "Producto.updateStatus", query = "UPDATE Producto p SET p.status=:status where p.id=:id")
@NamedQuery(name = "Producto.findProductoByNombre", query = "SELECT p from Producto p where p.nombre=:nombre")
@NamedQuery(name = "Producto.getProductoByCategoria", query = "select new com.api.gestion.wrapper.ProductoWrapper(p.id, p.nombre) from Producto p where p.categoria.id=:id and p.status='true'")
@NamedQuery(name = "Producto.getProductoById", query = "select new com.api.gestion.wrapper.ProductoWrapper(p) from Producto p where p.id =:id")
// Consulta nombrada para ordenar los productos por fechaCreacion de forma ascendente
@NamedQuery(name = "Producto.getAllProductosOrderByFechaCreacionASC", query = "SELECT new com.api.gestion.wrapper.ProductoWrapper(p) from Producto p ORDER BY p.fechaCreacion ASC")
// Consulta nombrada para ordenar los productos por fechaCreacion de forma descendente
@NamedQuery(name = "Producto.getAllProductosOrderByFechaCreacionDESC", query = "SELECT new com.api.gestion.wrapper.ProductoWrapper(p) from Producto p ORDER BY p.fechaCreacion DESC")
@Entity
@Table(name = "producto")
@DynamicInsert
@DynamicUpdate
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Categoria categoria;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "precio")
    private Integer precio;

    @Column(name = "status")
    private String status;

    @Column(name = "fechaCreacion", nullable = true)
    private Date fechaCreacion;
}
