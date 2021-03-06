package com.midd.core.reportes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.midd.core.modelo.Equipo;

public class ReporteEquipos {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    List<Equipo> equipos;

    public ReporteEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Reporte Equipos");
    }
    /*public ReporteEquipos(){
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet("Reporte Equipos");
    }*/

    private void cabeceraTabla(){
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        Cell cell;
        String[] headers = {
                "Tipo",
                "Nombre de equipo",
                "Líder Banco",
                "Líder Técnico",
                "Descripción",
                "Estado",
                "Fecha de registro"

        };
        for (int i = 0; i < headers.length; i++) {
            cell = row.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(style);
        }

    }
    
    private void escribirDatosTabla(){
        System.out.println("ESCRIBIENDO DATA...");
        int initRow = 1;
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String mensaje = "";

        for (Equipo equipo : equipos) {
            System.out.println("ESCRIBIENDO DATA BUCLE...");
            Row row = sheet.createRow(initRow++);

            if (equipo.isEstado_asi()) {
                mensaje = "Vigente";
            } else {
                mensaje = "No vigente";
            }

            String[] rows = {
                equipo.getTipo_equipo_asi(),
                equipo.getNombre_equipo_asi(),
                equipo.getNombre_lider(),
                equipo.getNombre_tecnico(),
                equipo.getDescripcion_asi(),
                mensaje,
                dateFormat.format(equipo.getFecha_registro())
            };

            Cell cell;
            
            for(int i=0; i < rows.length; i++){
                System.out.println("ESCRIBIENDO CELDAS...1 " + i);
                cell = row.createCell(i);
                System.out.println("ESCRIBIENDO CELDAS...2 "+ i);
                cell.setCellValue(rows[i]);
                System.out.println("ESCRIBIENDO CELDAS...3 "+ i);
               // sheet.autoSizeColumn(i);
                System.out.println("ESCRIBIENDO CELDAS...4 "+ i);
                cell.setCellStyle(cellStyle);
            }
            System.out.println("FIN ESCRIBIENDO CELDAS...");
        }
    }

    public void export(HttpServletResponse response) throws IOException{
        cabeceraTabla();
        escribirDatosTabla();
        System.out.println("DESPUES DE GENERAR EL EXCEL");
        ServletOutputStream outputStream = response.getOutputStream();
        System.out.println("1");
        workbook.write(outputStream);
        System.out.println("2");
        workbook.close();
        System.out.println("3");
        outputStream.close();
        System.out.println("4");
        System.out.println("TERMINO EL EXPORT");
    }
    
}
