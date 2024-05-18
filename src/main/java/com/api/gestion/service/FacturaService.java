package com.api.gestion.service;

import com.api.gestion.pojo.Factura;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface FacturaService {

    ResponseEntity<String> generateReport(Map<String, Object> requestMap, boolean isCreated);

    ResponseEntity<List<Factura>> getFacturas();

    ResponseEntity<Factura> guardarFactura(Map<String, Object> requestMap);

    Map<String, Object> getMapById(Integer id);

    ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap);

    ResponseEntity<String> deleteFacturaById(Integer id);
}
