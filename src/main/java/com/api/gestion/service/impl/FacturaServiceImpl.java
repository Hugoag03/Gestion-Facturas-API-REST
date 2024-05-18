package com.api.gestion.service.impl;

import com.api.gestion.constantes.FacturaConstantes;
import com.api.gestion.dao.FacturaDAO;
import com.api.gestion.mapper.MapToEntity;
import com.api.gestion.pojo.Factura;
import com.api.gestion.pojo.User;
import com.api.gestion.security.jwt.JwtFilter;
import com.api.gestion.service.FacturaService;
import com.api.gestion.util.FacturaUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.persistence.Entity;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

@Service
@Slf4j
public class FacturaServiceImpl implements FacturaService {

    @Autowired
    private FacturaDAO facturaDAO;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private MapToEntity mapToEntity;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap, boolean isCreated) {
        log.info("Dentro del método generar reporte");
        try {
            String fileName = null;

            if (validateRequestMap(requestMap)){
                    if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate") && isCreated){
                        fileName = (String) requestMap.get("uuid");
                    }else{
                        fileName = FacturaUtils.getUuid();
                        requestMap.put("uuid", fileName);
                        insertarFactura(requestMap);
                    }


                String data = "Nombre: " + requestMap.get("nombre")
                        + "\nNúmero de contacto: " + requestMap.get("numeroContacto")
                        + "\nEmail: " + requestMap.get("email")
                        + "\nMétodo de pago: " + requestMap.get("metodoPago");

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(FacturaConstantes.STORE_LOCATION + "\\" + fileName + ".pdf"));

                document.open();
                setRectangleInPdf(document);

                Paragraph paragraphHeader = new Paragraph("Gestión de categorías y productos", getFont("Header"));
                paragraphHeader.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraphHeader);

                PdfPTable pdfPTable = new PdfPTable(5);
                pdfPTable.setWidthPercentage(100);
                addTableHeader(pdfPTable);

                JSONArray jsonArray = FacturaUtils.getJsonArrayFromString((String)requestMap.get("productoDetalles"));
                for (int i = 0; i < jsonArray.length(); i++){
                    addRows(pdfPTable, FacturaUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(pdfPTable);

                Paragraph footer = new Paragraph("Total: " + requestMap.get("total") + "\n" +
                        "Gracias por visitarnos, vuelva pronto !!", getFont("Data"));
                document.add(footer);

                document.close();

                return new ResponseEntity<>("{\"uuid\":\"" + fileName +"\"}", HttpStatus.OK);

            }else{
                return FacturaUtils.getResponseEntity("Datos requeridos no encontrados", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Factura>> getFacturas() {
        List<Factura> facturas = new ArrayList<>();
        if (jwtFilter.isAdmin()){
            facturas = facturaDAO.getFacturas();
        }else{
            facturas = facturaDAO.getFacturaByUsername(jwtFilter.getCurrentUser());
        }
        return new ResponseEntity<>(facturas, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Factura> guardarFactura(Map<String, Object> requestMap) {
        try{
            if (validateRequestMap(requestMap)) {
                return new ResponseEntity<>(facturaDAO.save(mapToEntity.mapToEntity(requestMap)), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public Map<String, Object> getMapById(Integer id) {
        Factura factura = facturaDAO.findById(id).orElse(null);
        Map<String, Object> requestMap = new HashMap<>();
        if (factura != null){
            requestMap.put("id", id);
            requestMap.put("nombre", factura.getNombre());
            requestMap.put("email", factura.getEmail());
            requestMap.put("metodoPago", factura.getMetodoPago());
            requestMap.put("numeroContacto", factura.getNumeroContacto());
            requestMap.put("uuid", factura.getUuid());
            requestMap.put("productoDetalles", factura.getProductoDetalles());
            requestMap.put("createdBy", factura.getCreatedBy());
            requestMap.put("total", factura.getTotal());

        }
        return requestMap;
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("Dentro de getPdf : requestMap{}", requestMap);
        try {
            byte [] bytesArray = new byte[0];
            if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap)){
                return new ResponseEntity<>(bytesArray, HttpStatus.BAD_REQUEST);
            }

            String filePath = FacturaConstantes.STORE_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";

            if (FacturaUtils.isFileExist(filePath)){
                bytesArray = getByteArray(filePath);
                return new ResponseEntity<>(bytesArray, HttpStatus.OK);
            }else{
                requestMap.put("isGenerate", false);
                generateReport(requestMap, false);
                bytesArray = getByteArray(filePath);
                return new ResponseEntity<>(bytesArray, HttpStatus.OK);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteFacturaById(Integer id) {
        try{
            Optional <Factura> factura = facturaDAO.findById(id);
            if (factura.isPresent()){
                facturaDAO.deleteById(id);
                return new ResponseEntity<>("Factura eliminada con éxito", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("No existe esa factura", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return FacturaUtils.getResponseEntity(FacturaConstantes.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private byte[] getByteArray(String filepath) throws IOException {
        File initialFile = new File(filepath);
        InputStream inputStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        inputStream.close();;
        return byteArray;
    }

    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Dentro de setRectangleInPdf");
        Rectangle rectangle = new Rectangle(577, 825, 18, 15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorder(1);
        document.add(rectangle);
    }

    private Font getFont(String type){
        log.info("Dentro de getFont");
        switch (type){
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void addRows(PdfPTable pdfPTable, Map<String, Object> data){
        log.info("Dentro de addRows");
        pdfPTable.addCell((String)data.get("nombre"));
        pdfPTable.addCell((String)data.get("categoria"));
        pdfPTable.addCell((String)data.get("cantidad"));
        pdfPTable.addCell(Double.toString((Double) data.get("precio")));
        pdfPTable.addCell(Double.toString((Double) data.get("total")));
    }

    private void addTableHeader(PdfPTable pdfPTable){
        log.info("Dentro del addTableHeader");
        Stream.of("Nombre", "Categoría", "Cantidad", "Precio", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell pdfPCell = new PdfPCell();
                    pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    pdfPCell.setBorderWidth(2);
                    pdfPCell.setPhrase(new Phrase(columnTitle));
                    pdfPCell.setBackgroundColor(BaseColor.YELLOW);
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfPTable.addCell(pdfPCell);

                });
    }

    private void insertarFactura(Map<String, Object> requestMap){
        try{
                Factura factura = new Factura();
                factura.setUuid((String) requestMap.get("uuid"));
                factura.setNombre((String) requestMap.get("nombre"));
                factura.setEmail((String) requestMap.get("email"));
                factura.setNumeroContacto((String) requestMap.get("numeroContacto"));
                factura.setMetodoPago((String) requestMap.get("metodoPago"));
                factura.setTotal(Integer.parseInt(requestMap.get("total").toString()));
                factura.setProductoDetalles((String) requestMap.get("productoDetalles"));
                factura.setCreatedBy(jwtFilter.getCurrentUser());
                facturaDAO.save(factura);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap){
        System.out.println(requestMap.get("nombre"));
        System.out.println(requestMap.get("numeroContacto"));
        System.out.println(requestMap.get("email"));
        System.out.println(requestMap.get("metodoPago"));
        System.out.println(requestMap.get("productoDetalles"));
        System.out.println(requestMap.get("total"));

            return requestMap.containsKey("nombre") &&
                    requestMap.containsKey("numeroContacto") &&
                    requestMap.containsKey("email") &&
                    requestMap.containsKey("metodoPago") &&
                    requestMap.containsKey("productoDetalles") &&
                    requestMap.containsKey("total");
    }

    public static boolean isFileExist(String path){
        log.info("Dentro de isFileExist", path);
        try {
            File file = new File(path);
            return file != null && file.exists() ? Boolean.TRUE : Boolean.FALSE;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
