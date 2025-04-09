package com.gag.board.util.exporter;

import com.gag.board.dto.BlockUnblockReport;
import com.gag.board.dto.MovementReport;
import com.gag.board.service.ConsoleInterface;
import com.gag.board.service.MessageService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
/**
 * Component responsible for exporting different types of reports into PDF format.
 * Uses Apache PDFBox to generate the documents.
 */
@Component
public class PDFExporter {

    private static final float Y_START = 740;
    private static final float X_START = 50;
    private static final float Y_MARGIN = 50;
    private static final float ROW_HEIGHT = 20;

    private final MessageService messageService;
    private final ConsoleInterface consoleInterface;
    /**
     * Constructs a PDFExporter with the given message service and console interface.
     *
     * @param messageService the service used to retrieve localized messages
     * @param consoleInterface the interface for displaying messages to the user
     */
    public PDFExporter(MessageService messageService, ConsoleInterface consoleInterface) {
        this.messageService = messageService;
        this.consoleInterface = consoleInterface;
    }
    /**
     * Exports a list of MovementReport objects to a PDF file.
     *
     * @param reports the list of movement reports
     * @param filePath the path where the PDF will be saved
     * @param title the title to be shown in the PDF
     */
    public void exportMovementReportToPDF(List<MovementReport> reports, String filePath, String title) {
        exportToPDF(reports, filePath, title, this::insertDataRowsMovementReport);
    }
    /**
     * Exports a list of BlockUnblockReport objects to a PDF file.
     *
     * @param reports the list of block/unblock reports
     * @param filePath the path where the PDF will be saved
     * @param title the title to be shown in the PDF
     */
    public void exportBlockedUnblockedReportToPDF(List<BlockUnblockReport> reports, String filePath, String title) {
        exportToPDF(reports, filePath, title, this::insertDataRowsBlockUnblockReport);
    }
    /**
     * Generic method that exports a list of reports to a PDF file using a custom formatter.
     *
     * @param reports the list of reports to export
     * @param filePath the path where the PDF will be saved
     * @param title the title to be shown in the PDF
     * @param rowFormatter function to format each report as a string of lines separated by semicolons
     * @param <T> the type of report
     */
    private <T> void exportToPDF(List<T> reports, String filePath, String title, Function<T, String> rowFormatter) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);

            try {
                float yPosition = Y_START;

                setDocumentTitle(contentStream, title);
                yPosition -= ROW_HEIGHT;

                PDType1Font textFont = new PDType1Font(Standard14Fonts.FontName.COURIER);

                for (T report : reports) {
                    yPosition -= ROW_HEIGHT;
                    drawTableLines(contentStream, X_START - 10, yPosition + 10, 0);

                    List<String> results = List.of(rowFormatter.apply(report).split(";"));

                    for (String line : results) {
                        yPosition -= ROW_HEIGHT;

                        if (yPosition < Y_MARGIN) {
                            contentStream.close();
                            contentStream = createNewPageWithStream(document);
                            yPosition = Y_START;
                        }

                        contentStream.beginText();
                        contentStream.setFont(textFont, 10);
                        contentStream.newLineAtOffset(X_START, yPosition);
                        contentStream.showText(line);
                        contentStream.endText();
                    }

                    if (yPosition < Y_MARGIN) {
                        contentStream.close();
                        contentStream = createNewPageWithStream(document);
                        yPosition = Y_START;
                    }

                    drawTableLines(contentStream, X_START - 10, yPosition - 10, 0);
                }

                contentStream.close();
                document.save(new File(filePath));
                consoleInterface.printMessage(messageService.getMessage("export.success"));

            } finally {
                contentStream.close();
            }

        } catch (IOException e) {
            consoleInterface.printMessage(messageService.getMessage("export.fail"));
            consoleInterface.printMessage(e.getMessage());
        }
    }
    /**
     * Creates and returns a new PDPageContentStream for a new page.
     *
     * @param document the PDF document
     * @return the content stream of the new page
     * @throws IOException if the content stream cannot be created
     */
    private PDPageContentStream createNewPageWithStream(PDDocument document) throws IOException {
        PDPage newPage = new PDPage();
        document.addPage(newPage);
        return new PDPageContentStream(document, newPage, PDPageContentStream.AppendMode.APPEND, true);
    }
    /**
     * Draws the border of a table row.
     *
     * @param contentStream the content stream to draw on
     * @param x the x-coordinate to start drawing
     * @param y the y-coordinate to start drawing
     * @param height the height of the rectangle
     * @throws IOException if drawing fails
     */
    private void drawTableLines(PDPageContentStream contentStream, float x, float y, float height) throws IOException {
        contentStream.setStrokingColor(0, 0, 0);
        contentStream.setLineWidth(1f);
        contentStream.moveTo(x, y);
        contentStream.lineTo(x + 540, y);
        contentStream.lineTo(x + 540, y - height);
        contentStream.lineTo(x, y - height);
        contentStream.lineTo(x, y);
        contentStream.stroke();
    }
    /**
     * Writes the title of the PDF at the top of the page.
     *
     * @param contentStream the content stream to write on
     * @param title the title text
     * @throws IOException if writing fails
     */
    private void setDocumentTitle(PDPageContentStream contentStream, String title) throws IOException {
        drawTableLines(contentStream, 30, 780, 50);
        PDType1Font titleFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
        contentStream.setFont(titleFont, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(280-title.length(), 750);
        contentStream.showText(title);
        contentStream.endText();
    }
    /**
     * Formats a MovementReport into a semicolon-separated string.
     *
     * @param report the movement report
     * @return the formatted string
     */
    private String insertDataRowsMovementReport(MovementReport report) {
        String[] header = createMovementReportHeader();
        return header[0] + ": " + report.id() + ";" +
                header[1] + ": " + report.title() + ";" +
                header[2] + ": " + report.description() + ";" +
                header[3] + ": " + report.type() + ";" +
                header[4] + ": " + report.boardId() + ";" +
                header[5] + ": " + report.columnBoardId() + ";" +
                header[6] + ": " + (report.exitTime() !=null ? formatDateTime(report.exitTime()):"") + ";" +
                header[7] + ": " + report.minutesSpent() + ";";
    }
    /**
     * Formats a BlockUnblockReport into a semicolon-separated string.
     *
     * @param report the block/unblock report
     * @return the formatted string
     */
    private String insertDataRowsBlockUnblockReport(BlockUnblockReport report) {
        String[] header = createBlockUnblockReportHeader();
        return header[0] + ": " + report.id() + ";" +
                header[1] + ": " + report.title() + ";" +
                header[2] + ": " + report.description() + ";" +
                header[3] + ": " + report.type() + ";" +
                header[4] + ": " + report.boardId() + ";" +
                header[5] + ": " + report.columnBoardId() + ";" +
                header[6] + ": " + report.isBlocked() + ";" +
                header[7] + ": " + report.blockedReason() + ";" +
                header[8] + ": " + report.unblockedReason() + ";" +
                header[9] + ": " + formatDateTime(report.lastBlockedDt()) + ";" +
                header[10] + ": " + formatDateTime(report.lastUnblockedDt()) + ";" +
                header[11] + ": " + report.secondsSpent() + ";";
    }
    /**
     * Creates an array of header labels for MovementReport fields.
     *
     * @return an array of localized header labels
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
    /**
     * Creates an array of header labels for BlockUnblockReport fields.
     *
     * @return an array of localized header labels
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
     * formats date fields to default locale format.
     *
     * @return a String representing the data provided formatted to the default locale
     */
    private String formatDateTime(LocalDateTime dateTime) {
        Locale locale = Locale.getDefault(); // supondo que você tenha isso
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(locale);
        return dateTime.format(formatter).replace('\u202F', ' ');
    }

}
