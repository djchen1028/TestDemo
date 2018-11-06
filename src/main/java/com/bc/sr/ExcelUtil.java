package com.bc.sr;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileOutputStream;
import java.util.List;
import java.util.ResourceBundle;

public class ExcelUtil {
    private static String[] HEADERS={"id","keyword","URL"};

    public static void writeExcel(String fileName, List<String[]> dataLists) {
        ResourceBundle resource = ResourceBundle.getBundle("DBproperties");
        String FILEPATH = resource.getString("FILEPATH");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(fileName);
        HSSFRow headRow = sheet.createRow(0);
        for(int i=0;i<3;i++){
            headRow.createCell(i).setCellValue(HEADERS[i]);
        }
        int rowId=1;
        for(String[] data:dataLists){
            HSSFRow rowConeten = sheet.createRow(rowId);
            for(int i = 0 ;i<data.length;i++){
                rowConeten.createCell(i).setCellValue(data[i]);
            }
            rowId++;
        }
        FileOutputStream output=null;
        try {
            output=new FileOutputStream(FILEPATH+fileName+".xls");
            wb.write(output);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                output.close();
                wb.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }
}
