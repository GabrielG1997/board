package com.gag.board.util.exporter;

import com.gag.board.dto.BlockUnblockReport;
import com.gag.board.dto.MovementReport;
import com.gag.board.service.ConsoleInterface;
import com.gag.board.service.MessageService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
/**
 * Service responsible for exporting report data to Excel files.
 * Handles the creation of spreadsheets for both movement and block/unblock reports,
 * including headers, styling, and writing data to the file system.
 */
@Component
public class ExcelExporter {
    private final MessageService messageService;
    private final ConsoleInterface consoleInterface;
    /**
     * Constructs the ExcelExporter with dependencies for messaging and console interaction.
     *
     * @param messageService     service for retrieving localized messages
     * @param consoleInterface   interface for console input/output
     */
    public ExcelExporter(MessageService messageService, ConsoleInterface consoleInterface) {
        this.messageService = messageService;
        this.consoleInterface = consoleInterface;
    }
    /**
     * Exports a list of BlockUnblockReport objects to an Excel file with proper headers and formatting.
     *
     * @param reports  the list of block/unblock reports
     * @param filePath the path to the Excel file to be created
     * @param title    the sheet title
     */
    public void exportBlockedUnblockedToExcel(List<BlockUnblockReport> reports, String filePath, String title) {
        String[] columns = null;
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(title);
            Row headerRow = sheet.createRow(0);
            columns = createBlockUnblockReportHeader();
            CellStyle cellStyle = createDateCellStyle(workbook);
            insertDataRowsBlockUnblockReport(reports, sheet,cellStyle);
            createHeaderStyle(columns, headerRow, workbook);
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            Path path = Paths.get(filePath);
            Files.createDirectories(path.getParent());

            try (FileOutputStream fileOut = new FileOutputStream(path.toFile())) {
                workbook.write(fileOut);
                fileOut.flush();
            }

            consoleInterface.printMessage(messageService.getMessage("export.success"));
        } catch (Exception e) {
            e.printStackTrace();
            consoleInterface.printMessage(messageService.getMessage("export.fail"));
            consoleInterface.printMessage(e.getMessage());
        }
    }
    /**
     * Exports a list of MovementReport objects to an Excel file with proper headers and formatting.
     *
     * @param reports  the list of movement reports
     * @param filePath the path to the Excel file to be created
     * @param title    the sheet title
     */
    public void exportMovementToExcel(List<MovementReport> reports, String filePath, String title) {
        String[] columns = null;
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(title);
            Row headerRow = sheet.createRow(0);
            columns = createMovementReportHeader();
            CellStyle cellStyle = createDateCellStyle(workbook);
            insertDataRowsMovementReport(reports, sheet,cellStyle);
            createHeaderStyle(columns, headerRow, workbook);
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }
            FileOutputStream fileOut = new FileOutputStream(filePath);
            workbook.write(fileOut);
            fileOut.close();
            consoleInterface.printMessage(messageService.getMessage("export.success"));
        } catch (Exception e) {
            consoleInterface.printMessage(messageService.getMessage("export.fail"));
            consoleInterface.printMessage(e.getMessage());
        }
    }
    /**
     * Creates a cell style for date-time values in the Excel sheet.
     *
     * @param workbook the Excel workbook
     * @return a cell style with a date-time format
     */
    private CellStyle createDateCellStyle(Workbook workbook) {
        CellStyle dateCellStyle = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        dateCellStyle.setDataFormat(format.getFormat("[$-en-US]m/d/yy h:mm:ss AM/PM;@"));
        return dateCellStyle;
    }
    /**
     * Inserts movement report data rows into the Excel sheet.
     *
     * @param reports           the list of movement reports
     * @param sheet             the Excel sheet to insert data into
     * @param dateTimeCellStyle the style to apply to date-time cells
     */
    private static void insertDataRowsMovementReport(List<MovementReport> reports, Sheet sheet, CellStyle dateTimeCellStyle) {
        int rowNum = 1;
        for (MovementReport report : reports) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(report.id());
            row.createCell(1).setCellValue(report.title());
            row.createCell(2).setCellValue(report.description());
            row.createCell(3).setCellValue(report.type());
            row.createCell(4).setCellValue(report.boardId());
            row.createCell(5).setCellValue(report.columnBoardId());
            row.createCell(6).setCellStyle(dateTimeCellStyle);
            row.getCell(6).setCellValue(report.exitTime());
            row.createCell(7).setCellValue(report.minutesSpent());
        }
    }
    /**
     * Inserts block/unblock report data rows into the Excel sheet.
     *
     * @param reports           the list of block/unblock reports
     * @param sheet             the Excel sheet to insert data into
     * @param dateTimeCellStyle the style to apply to date-time cells
     */
    private static void insertDataRowsBlockUnblockReport(List<BlockUnblockReport> reports, Sheet sheet, CellStyle dateTimeCellStyle) {
        int rowNum = 1;
        for (BlockUnblockReport report : reports) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(report.id());
            row.createCell(1).setCellValue(report.title());
            row.createCell(2).setCellValue(report.description());
            row.createCell(3).setCellValue(report.type());
            row.createCell(4).setCellValue(report.boardId());
            row.createCell(5).setCellValue(report.columnBoardId());
            row.createCell(6).setCellValue((report.isBlocked() ? 1:0));
            row.createCell(7).setCellValue(report.blockedReason());
            row.createCell(8).setCellValue(report.unblockedReason());
            row.createCell(9).setCellStyle(dateTimeCellStyle);
            row.getCell(9).setCellValue(report.lastBlockedDt());
            row.createCell(10).setCellStyle(dateTimeCellStyle);
            row.getCell(10).setCellValue(report.lastUnblockedDt());
            row.createCell(11).setCellValue(report.secondsSpent() != null ? report.secondsSpent() : 0);
        }
    }
    /**
     * Creates the header row with styled column titles for the Excel sheet.
     *
     * @param columns    the array of column titles
     * @param headerRow  the row object representing the header
     * @param workbook   the Excel workbook
     */
    private static void createHeaderStyle(String[] columns, Row headerRow, Workbook workbook) {
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            CellStyle style = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);
            cell.setCellStyle(style);
        }
    }
    /**
     * Generates the localized column headers for the block/unblock report.
     *
     * @return an array of header titles
     */
    private String[] createBlockUnblockReportHeader() {
        return new String[]{
                messageService.getMessage("export.column.id"),
                messageService.getMessage("export.column.title"),
                messageService.getMessage("export.column.description"),
                messageService.getMessage("export.column.type"),
                messageService.getMessage("export.column.boardId"),
                messageService.getMessage("export.column.columnBoardId"),
                messageService.getMessage("export.column.isBlocked"),
                messageService.getMessage("export.column.blockedReason"),
                messageService.getMessage("export.column.unblockedReason"),
                messageService.getMessage("export.column.lastBlockedDt"),
                messageService.getMessage("export.column.lastUnblockedDt"),
                messageService.getMessage("export.column.secondsSpent")
        };
    }
    /**
     * Generates the localized column headers for the movement report.
     *
     * @return an array of header titles
     */
    private String[] createMovementReportHeader() {
        return new String[]{
                messageService.getMessage("export.column.id"),
                messageService.getMessage("export.column.title"),
                messageService.getMessage("export.column.description"),
                messageService.getMessage("export.column.type"),
                messageService.getMessage("export.column.boardId"),
                messageService.getMessage("export.column.columnBoardId"),
                messageService.getMessage("export.column.exitTime"),
                messageService.getMessage("export.column.minutesSpent")
        };
    }
}
