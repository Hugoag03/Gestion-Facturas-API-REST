package com.api.gestion.dao;

import com.api.gestion.pojo.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface FacturaDAO extends JpaRepository<Factura, Integer> {

    List<Factura> getFacturas();

    List<Factura> getFacturaByUsername(@Param("username") String username);

}
