package com.gag.board.util.exporter;

import com.gag.board.dto.BlockUnblockReport;
import com.gag.board.dto.MovementReport;
import com.gag.board.service.CardService;
import com.gag.board.service.ConsoleInterface;
import com.gag.board.service.MessageService;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PDFExporterTest {

    @Mock
    MessageService messageService;
    @Mock
    ConsoleInterface consoleInterface;

    @InjectMocks
    private PDFExporter exporter;

    @TempDir
    Path tempDir;

    @Test
    void exportMovementReportToPDF_createsFileWithExpectedContent() throws IOException {
        //given
        MovementReport report = new MovementReport(1L, "Task A", "Desc", "MOVE", 10L, 20L, LocalDateTime.of(2024, 4, 1, 10, 0), 30.0);
        List<MovementReport> reports = List.of(report);
        String filePath = tempDir.resolve("movement.pdf").toString();
        Mockito.when(messageService.getMessage(Mockito.anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        //when
        exporter.exportMovementReportToPDF(reports, filePath, "Movement Report");
        //then
        assertTrue(Files.exists(Paths.get(filePath)));
        try(PDDocument document = Loader.loadPDF(new File(filePath))){
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            assertTrue(text.contains("Task A"));
            assertTrue(text.contains("Desc"));
            assertTrue(text.contains("MOVE"));
            assertTrue(text.contains("30"));
        }
    }
    @Test
    void exportBlockedUnblockedReportToPDF_createsFileWithExpectedContent() throws IOException {
        //given
        BlockUnblockReport report = new BlockUnblockReport(2L, "Task B", "Desc B", "BLOCK", 11L, 22L, true, "Issue", "Solved",
                LocalDateTime.of(2024, 4, 2, 11, 0), LocalDateTime.of(2024, 4, 2, 12, 0), 120L);
        List<BlockUnblockReport> reports = List.of(report);
        String filePath = tempDir.resolve("blockunblock.pdf").toString();
        Mockito.when(messageService.getMessage(Mockito.anyString())).thenAnswer(invocation -> invocation.getArgument(0));
        //when
        exporter.exportBlockedUnblockedReportToPDF(reports, filePath, "BlockUnblock Report");
        //then
        assertTrue(Files.exists(Paths.get(filePath)));
        try (PDDocument document = Loader.loadPDF(new File(filePath))){
             PDFTextStripper stripper = new PDFTextStripper();
             String text = stripper.getText(document);
             assertTrue(text.contains("Task B"));
             assertTrue(text.contains("Desc B"));
             assertTrue(text.contains("BLOCK"));
             assertTrue(text.contains("true"));
             assertTrue(text.contains("Issue"));
             assertTrue(text.contains("Solved"));
             assertTrue(text.contains("120"));
        }
    }
}
