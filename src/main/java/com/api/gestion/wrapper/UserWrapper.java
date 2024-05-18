package com.api.gestion.wrapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWrapper {

    private Integer id;
    private String nombre;
    private String email;
    private String numeroDeContacto;
    private String status;
}
