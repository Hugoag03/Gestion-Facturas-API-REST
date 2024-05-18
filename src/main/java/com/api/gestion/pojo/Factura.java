package com.api.gestion.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@NamedQuery(name = "Factura.getFacturas", query = "select f from Factura f order by f.id desc")
@NamedQuery(name = "Factura.getFacturaByUsername", query = "select f from Factura f where f.createdBy=:username order by f.id desc")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "facturas")
public class Factura {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "uuid")
    private String uuid;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "email")
    private String email;

    @Column(name = "numeroContacto")
    private String numeroContacto;

    @Column(name = "metodoPago")
    private String metodoPago;

    @Column(name = "total")
    private Integer total;

    @Column(name = "productoDetalles", columnDefinition = "json")
    private String productoDetalles;

    @Column(name = "createdBy")
    private String createdBy;
}
