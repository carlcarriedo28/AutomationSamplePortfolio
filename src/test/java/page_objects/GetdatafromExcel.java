package page_objects;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GetdatafromExcel {
    private File file;
    private FileInputStream inputStream;
    private String testID;
    private String sheetName;
    private int testIdColumn;
    private int numberOfColumns;
    private XSSFCell cell;
    public  HashMap<String, String> fieldsAndValues;

    public GetdatafromExcel(String testId, String sheetName) {
        file = new File(System.getProperty("user.dir") + "\\src\\testdata.xlsx");
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found at given location: " + e);
        }
        this.testID = testId;
        this.sheetName = sheetName;

        this.readExcelAndCreateHashMapForData();
    }

    public HashMap<String, String> readExcelAndCreateHashMapForData() {
        try {
            fieldsAndValues = new HashMap<String, String>();

            XSSFWorkbook workBook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workBook.getSheet(sheetName);

            /* Get number of rows */
            int lastRow = sheet.getLastRowNum();
            int firstRow = sheet.getFirstRowNum();
            int numberOfRows = lastRow - firstRow;

            /*
             * Get test_Id column number.
             */
            outerloop: for (int row = 0; row < numberOfRows; row++) {
                numberOfColumns = sheet.getRow(row).getLastCellNum();
                for (int cellNumber = 0; cellNumber < numberOfColumns; cellNumber++) {
                    cell = sheet.getRow(row).getCell(cellNumber);
                    cell.setCellType(CellType.STRING);
                    if (sheet.getRow(row).getCell(cellNumber).getStringCellValue().equalsIgnoreCase("test_ID")) {
                        testIdColumn = sheet.getRow(row).getCell(cellNumber).getColumnIndex();
                        break outerloop;
                    }
                }
            }

            /*
             * Search for the test id value.
             */
            outerloop: for (int i = 0; i <= numberOfRows; i++) {

                cell = sheet.getRow(i).getCell(testIdColumn);
                cell.setCellType(CellType.STRING);

                if (testID.equals(sheet.getRow(i).getCell(testIdColumn).getStringCellValue())) {


                    for (int j = 0; j < numberOfColumns; j++) {
                        XSSFCell key = sheet.getRow(testIdColumn).getCell(j);
                        XSSFCell value = sheet.getRow(i).getCell(j);

                        key.setCellType(CellType.STRING);

                        if (value == null) {
                            // Not capturing blank cells.
                        } else if (value.getCellType() == CellType.BLANK) {
                            // Not capturing blank cells.
                        } else {
                            value.setCellType(CellType.STRING);
                            String fieldName = sheet.getRow(testIdColumn).getCell(j).getStringCellValue().trim();
                            String fieldValue = sheet.getRow(i).getCell(j).getStringCellValue().trim();
                            fieldsAndValues.put(fieldName, fieldValue);
                        }
                    }
                    System.out.println("Fields and values: " + Arrays.toString(fieldsAndValues.entrySet().toArray()));
                    break outerloop;
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred at getting the sheet: " + e);
        }
        /* Return the hash map */
        return fieldsAndValues;
    }
}