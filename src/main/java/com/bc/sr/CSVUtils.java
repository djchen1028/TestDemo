package com.bc.sr;

import com.csvreader.CsvWriter;
import org.apache.commons.beanutils.BeanUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class CSVUtils {
    private static String[] HEADERS={"id","keyword","URL"};

    public static void main(String[] args){
        List<String[]> dataLists = new ArrayList<String[]>();
        String[] shanghai = {"1","shanghai","aaaaaa"};
        String[] beijing = {"2","beijing","bbbbbb"};
        String[] suzhou = {"3","suzhou","cccccc"};
        dataLists.add(shanghai);
        dataLists.add(beijing);
        dataLists.add(suzhou);
        CSVUtils.write("city",dataLists);
    }

    public static void write(String fileName, List<String[]> dataLists){
        ResourceBundle resource = ResourceBundle.getBundle("DBproperties");
        String FILEPATH = resource.getString("FILEPATH");
        String filePath = FILEPATH+fileName+".csv";
//        String filePath = "/Users/dj_chen/IdeaProjects/WebFiles/"+fileName+".csv";
//        String filePath = "F:/Documents/IdeaProjects/WebFiles/"+fileName+".csv";

        CsvWriter csvWrite = new CsvWriter(filePath,',', Charset.forName("GBK"));
        try{
            csvWrite.writeRecord(HEADERS);
            for(String[] datalist:dataLists){
                csvWrite.writeRecord(datalist);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            csvWrite.close();
        }
    }


    /*手动创建CSV文件*/
    public static File createCSVFile(List exportData, LinkedHashMap map, String outPutPath, String fileName){
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            // 定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv", new File(outPutPath));
            System.out.println("csvFile：" + csvFile);
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"),1024);
            System.out.println("csvFileOutputStream：" + csvFileOutputStream);
            // 写入文件头部
            for (Iterator propertyIterator = map.entrySet().iterator();
                 propertyIterator.hasNext();) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
                csvFileOutputStream.write("" + (String) propertyEntry.getValue() != null ? (String) propertyEntry.getValue() : "" + "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(",");
                }
            }
            csvFileOutputStream.newLine();
            // 写入文件内容
            for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
                Object row = (Object) iterator.next();
                for (Iterator propertyIterator = map.entrySet().iterator();propertyIterator.hasNext();) {
                    java.util.Map.Entry propertyEntry =(java.util.Map.Entry) propertyIterator.next();
                    csvFileOutputStream.write((String) BeanUtils.getProperty( row, (String) propertyEntry.getKey()));
                    if (propertyIterator.hasNext()) {
                        csvFileOutputStream.write(",");
                    }
                }
                if (iterator.hasNext()) {
                    csvFileOutputStream.newLine();
                }
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile;
    }

}
