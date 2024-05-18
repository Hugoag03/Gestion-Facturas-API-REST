package com.api.gestion.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@NamedQuery(name = "Categoria.getAllCategorias", query = "select c from Categoria c")
@NamedQuery(name = "Categoria.updateCategoria", query = "update Categoria set nombre=:nombre")
@Data
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@DynamicInsert
@Table(name = "categorias")
public class Categoria {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(name = "nombre")
    private String nombre;


}
