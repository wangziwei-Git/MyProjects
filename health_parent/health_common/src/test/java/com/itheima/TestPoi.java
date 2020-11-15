package com.itheima;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author: wzw
 * @Date: 2020/11/13 18:14
 * @version: 1.8
 */
public class TestPoi {

    //第一种:读取Excel(不推荐使用)
    //@Test
    public void readExcel1() throws IOException {
        //1.创建工作簿
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\15710\\Desktop\\read.xlsx");
        //2.获取工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
        //3.遍历工作表获取行对象
        for (Row row : sheet) {
            //4.遍历行对象获取单元格对象
            for (Cell cell : row) {
                //5.获取单元格对象
                String value = cell.getStringCellValue();
                //打印
                System.out.println(value);
            }
        }
        //6.关流
        workbook.close();
    }

    //第二种方式:获取excel文件.获取最后一行[常用]
    //@Test
    public void readExcel2() throws IOException {
        //创建工作簿(文件的路径),有编译时异常
        XSSFWorkbook workbook = new XSSFWorkbook("C:\\Users\\15710\\Desktop\\read.xlsx");
        //获取工作表,既可以根据工作表的顺序获取去,也可以根据工作的名称获取
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        //获取当前工作表最后一行的行号,行号从0开始
        int lastRowNum = sheetAt.getLastRowNum();
        //遍历所有行
        for (int i = 0; i <= lastRowNum; i++) {
            //根据行号获取对象
            XSSFRow row = sheetAt.getRow(i);
            //根据行号获取所有列
            short lastCellNum = row.getLastCellNum();
            //循环单行的所有列()
            for (short j = 0; j < lastCellNum; j++) {
                //获取每行每列对象
                String value = row.getCell(j).getStringCellValue();
                //打印
                System.out.println(value);
            }
            System.out.println("*********************");

        }
        //关流
        workbook.close();
    }


    //写入文件
    //@Test
    public void createExcel() throws IOException {
        //1.在内存种创建一个Excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
        //2.创建工作表,指定工作表名称(打开文件左下脚)
        XSSFSheet sheet = workbook.createSheet("测试文件");

        //3.创建行,0表示1行
        XSSFRow row = sheet.createRow(0);

        //3.1创建单元格,0表示第一个单元格
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");

        //创建行,1表示2行
        XSSFRow row1 = sheet.createRow(1);

        //创建单元格,0表示第一个单元格
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("小王");
        row1.createCell(2).setCellValue("18");

        //创建行,2表示3行
        XSSFRow row2 = sheet.createRow(2);

        //创建单元格,0表示第一个单元格
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("小娘们");
        row2.createCell(2).setCellValue("17");

        //4.通过输出流将workbook对象下载到磁盘(有编译时异常)
        FileOutputStream out = new FileOutputStream("C:\\Users\\15710\\Desktop\\asd.xlsx");
        //5.写出(有编译时异常)
        workbook.write(out);
        //6.刷新
        out.flush();
        //7.关流
        out.close();
        workbook.close();


    }
}
