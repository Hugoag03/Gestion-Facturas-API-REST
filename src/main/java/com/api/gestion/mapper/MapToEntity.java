package com.api.gestion.mapper;

import com.api.gestion.pojo.Factura;
import com.api.gestion.security.jwt.JwtFilter;
import com.api.gestion.util.FacturaUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MapToEntity {

    @Autowired
    private JwtFilter jwtFilter;

    public Factura mapToEntity(Map<String, Object> requestMap) {
        Factura factura = new Factura();
        factura.setUuid(FacturaUtils.getUuid());
        factura.setNombre((String) requestMap.get("nombre"));
        factura.setEmail((String) requestMap.get("email"));
        factura.setNumeroContacto((String) requestMap.get("numeroContacto"));
        factura.setMetodoPago((String) requestMap.get("metodoPago"));

        // Verificar si el valor asociado con la clave "total" es numérico antes de convertirlo
        Object totalObject = requestMap.get("total");
        if (totalObject instanceof String && ((String) totalObject).matches("\\d+")) {
            factura.setTotal(Integer.parseInt((String) totalObject));
        } else {
            // Si no es numérico, puedes manejar el error de alguna otra manera
            throw new IllegalArgumentException("El valor de 'total' no es un número válido");
        }

        factura.setProductoDetalles((String) requestMap.get("productoDetalles"));
        factura.setCreatedBy(jwtFilter.getCurrentUser());

        return factura;
    }
}
