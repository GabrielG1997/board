package com.gag.board.util.exporter;

import com.gag.board.dto.BlockUnblockReport;
import com.gag.board.dto.MovementReport;
import com.gag.board.service.ConsoleInterface;
import com.gag.board.service.MessageService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExcelExporterTest {
    @InjectMocks
    ExcelExporter exporter;
    @Mock
    MessageService messageService;
    @Mock
    ConsoleInterface consoleInterface;
    @TempDir
    Path tempDir;
    @Test
    void shouldExportMovementToExcelWithCorrectContent() throws Exception {
        // given
        when(messageService.getMessage("export.success")).thenReturn("Export successful");
        when(messageService.getMessage("export.column.id")).thenReturn("ID");
        when(messageService.getMessage("export.column.title")).thenReturn("Title");
        when(messageService.getMessage("export.column.description")).thenReturn("Description");
        when(messageService.getMessage("export.column.type")).thenReturn("Type");
        when(messageService.getMessage("export.column.boardId")).thenReturn("Board ID");
        when(messageService.getMessage("export.column.columnBoardId")).thenReturn("Column ID");
        when(messageService.getMessage("export.column.exitTime")).thenReturn("Exit Time");
        when(messageService.getMessage("export.column.minutesSpent")).thenReturn("Minutes Spent");
        MovementReport report = new MovementReport(
                1L,
                "Test Card",
                "Testing export",
                "TypeA",
                100L,
                200L,
                LocalDateTime.now(),
                45.0
        );
        File tempFile = Files.createTempFile("movement", ".xlsx").toFile();
        tempFile.deleteOnExit();
        // when
        exporter.exportMovementToExcel(List.of(report), tempFile.getAbsolutePath(), "Movements");
        // then
        verify(consoleInterface).printMessage("Export successful");
        try (FileInputStream fis = new FileInputStream(tempFile);
            Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheet("Movements");
            assertNotNull(sheet);
            // check headers
            Row header = sheet.getRow(0);
            assertEquals("ID", header.getCell(0).getStringCellValue());
            assertEquals("Title", header.getCell(1).getStringCellValue());
            assertEquals("Description", header.getCell(2).getStringCellValue());
            assertEquals("Type", header.getCell(3).getStringCellValue());
            assertEquals("Board ID", header.getCell(4).getStringCellValue());
            assertEquals("Column ID", header.getCell(5).getStringCellValue());
            assertEquals("Exit Time", header.getCell(6).getStringCellValue());
            assertEquals("Minutes Spent", header.getCell(7).getStringCellValue());
            // check data
            Row dataRow = sheet.getRow(1);
            assertEquals(1, (int) dataRow.getCell(0).getNumericCellValue());
            assertEquals("Test Card", dataRow.getCell(1).getStringCellValue());
            assertEquals("Testing export", dataRow.getCell(2).getStringCellValue());
            assertEquals("TypeA", dataRow.getCell(3).getStringCellValue());
            assertEquals(100, (int) dataRow.getCell(4).getNumericCellValue());
            assertEquals(200, (int) dataRow.getCell(5).getNumericCellValue());
            assertEquals(45, (int) dataRow.getCell(7).getNumericCellValue());
            assertNotNull(dataRow.getCell(6)); // exitTime (date field)
        }
    }
    @Test
    void exportBlockedUnblockedReportToExcel_createsFileWithExpectedContent() throws IOException {
        //given
        BlockUnblockReport report = new BlockUnblockReport(3L, "Task C", "Desc C", "UNBLOCK", 13L, 23L, false, "No Issue", "Reopened",
                LocalDateTime.of(2024, 4, 3, 13, 0), LocalDateTime.of(2024, 4, 3, 14, 0), 90L);
        List<BlockUnblockReport> reports = List.of(report);
        String filePath = "C:\\temp\\test-blockunblock.xlsx";
        when(messageService.getMessage(anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        //when
        exporter.exportBlockedUnblockedToExcel(reports, filePath, report.title());
        //then
        Path path = Paths.get(filePath);
        System.out.println(path);
        assertTrue(Files.exists(path));
        verify(consoleInterface).printMessage("export.success");
        try (Workbook workbook = new XSSFWorkbook(Files.newInputStream(path))) {
            Sheet sheet = workbook.getSheetAt(0);
            Row dataRow = sheet.getRow(1);
            assertNotNull(dataRow);
            assertEquals("Task C",dataRow.getCell(1).getStringCellValue());
            assertEquals("Desc C",dataRow.getCell(2).getStringCellValue());
            assertEquals("UNBLOCK",dataRow.getCell(3).getStringCellValue());
            assertFalse((dataRow.getCell(6).getNumericCellValue() == 1 ));
            assertEquals("No Issue",dataRow.getCell(7).getStringCellValue());
            assertEquals("Reopened",dataRow.getCell(8).getStringCellValue());
            assertEquals(90,dataRow.getCell(11).getNumericCellValue());
        }
    }

}

