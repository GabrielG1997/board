package com.gag.board.service;

import com.gag.board.entity.Board;
import com.gag.board.entity.BoardColumn;
import com.gag.board.entity.Card;
import com.gag.board.exception.ExitApplicationException;
import com.gag.board.util.exporter.ExcelExporter;
import com.gag.board.util.exporter.PDFExporter;
import org.hibernate.LazyInitializationException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * Service responsible for managing the application's flow through the console interface.
 * Handles the main menu and operations related to boards, columns, and cards.
 */
@Service
public class ConsoleService {
    private final BoardService boardService;
    private final BoardColumnService boardColumnService;
    private final CardService cardService;
    private final MessageService messageService;
    private final ConsoleInterface consoleInterface;
    private final ExcelExporter excelExporter;
    private final PDFExporter pdfExporter;

    /**
     * Constructs a new ConsoleService with the required dependencies.
     *
     * @param boardService service for board operations
     * @param boardColumnService service for board column operations
     * @param cardService service for card operations
     * @param messageService service for retrieving localized messages
     * @param consoleInterface interface for console interactions
     * @param excelExporter utility for exporting reports to Excel
     * @param pdfExporter utility for exporting reports to PDF
     */
    public ConsoleService(BoardService boardService, BoardColumnService boardColumnService, CardService cardService, MessageService messageService, ConsoleInterface consoleInterface, ExcelExporter excelExporter, PDFExporter pdfExporter) {
        this.boardService = boardService;
        this.boardColumnService = boardColumnService;
        this.cardService = cardService;
        this.messageService = messageService;
        this.consoleInterface = consoleInterface;
        this.excelExporter = excelExporter;
        this.pdfExporter = pdfExporter;
    }
    /**
     * Displays the main menu and handles user input to perform actions.
     */
    public void mainMenu() {
        while (true) {
            try{
                String option = consoleInterface.prompt(messageService.getMessage("type.choose.option"));
                switch (option){
                    case "0":
                        throw new ExitApplicationException(); // Interrompe a execução
                    case "1":
                        createNewBoard();
                        break;
                    case "2":
                        editBoard();
                        break;
                    case "3":
                        deleteBoard();
                        break;
                    case "4":
                        printMovementReport();
                        break;
                    case "5":
                        printBlockedReport();
                        break;
                    default:
                        consoleInterface.printMessage(messageService.getMessage("msg.error.chosen.option.not.allowed"));
                        break;
                }
            }catch (LazyInitializationException e) {
                consoleInterface.printMessage(messageService.getMessage("msg.error.lazyInitializationException"));
                mainMenu();
            } catch (NumberFormatException e){
                consoleInterface.printMessage(messageService.getMessage("msg.error.NumberFormatException"));
            }
        }
    }
    /**
     * Creates a new board along with its columns and cards.
     */
    void createNewBoard() {
        Map<String,String> titlesAndDescriptions = new HashMap<>();
        String boardName;
        String title;
        String description;
        int columnQty;
        int cardQty;
        try{
            boardName = consoleInterface.prompt(messageService.getMessage("type.board.name"));
            columnQty = Integer.parseInt(consoleInterface.prompt(messageService.getMessage("type.columns.quantity")));
            cardQty = Integer.parseInt(consoleInterface.prompt(messageService.getMessage("type.cards.quantity")));
        } catch (NumberFormatException e) {
            consoleInterface.printMessage(messageService.getMessage("msg.error.NumberFormatException"));
            return;
        }
        if(columnQty < 3){
            consoleInterface.printMessage(messageService.getMessage("msg.error.columns.quantity.minimum.required"));
            return;
        }

        for (int i = 0; i < cardQty; i++) {
            title = consoleInterface.prompt(messageService.getMessage("type.card.title"));
            description = consoleInterface.prompt(messageService.getMessage("type.card.description"));

            titlesAndDescriptions.put(title,description);
        }
        Board board = this.boardService.addBoard(boardName);
        List<BoardColumn> boardColumnList = this.boardColumnService.generateBoardColumns(columnQty, board);
        boardColumnList.getFirst().setCardList(cardService.generateCards(titlesAndDescriptions, boardColumnList.getFirst()));
        board.setBoardColumns(boardColumnList);
        this.boardService.save(board);
    }
    /**
     * Creates a new card and assigns it to the first column of the board.
     *
     * @param boardColumnList the list of columns in the board
     */
    private void createNewCard(List<BoardColumn> boardColumnList) {

        String title = consoleInterface.prompt(messageService.getMessage("type.card.title"));
        String description = consoleInterface.prompt(messageService.getMessage("type.card.description"));
        cardService.save(new Card(title,description, OffsetDateTime.now(),null,false, boardColumnList.getFirst()));
        consoleInterface.printMessage(messageService.getMessage("msg.card.added"));
    }
    /**
     * Allows editing an existing board, including card updates and movements.
     */
    private void editBoard() {
        Optional<Board> board = getBoard();
        if(board == null || !board.isPresent()){
            consoleInterface.printMessage(messageService.getMessage("msg.error.board.not.exist"));
        }
        else{
            List<Card> cardList = cardService.findByBoardColumn_Board_Id(board.get().getId());
            List<BoardColumn> boardColumnList = boardColumnService.findByBoard_Id(board.get().getId());
            int operation = Integer.parseInt(consoleInterface.prompt(messageService.getMessage("type.choose.option.inside.board")));
            while(true){
                switch (operation){
                    case 1,2,4,5:
                        updateCard(cardList, boardColumnList, operation);
                        return;
                    case 3:
                        createNewCard(boardColumnList);
                        return;
                    case 6:
                        return;
                    default:
                        consoleInterface.printMessage(messageService.getMessage("msg.error.invalid.option"));
                        return;
                }
            }
        }
    }
    /**
     * Deletes an existing board by its name or ID.
     */
    void deleteBoard() {
        Optional<Board> board = getBoard();
        if (board == null) return;
        board.ifPresent(boardService::delete);
    }
    /**
     * Generates and exports a movement report for the selected board in Excel or PDF format.
     */
    void printMovementReport() {
        Optional<Board> board = getBoard();
        if (board == null) return;
        if(consoleInterface.prompt(messageService.getMessage("type.report.type")).equals("1")) {
            excelExporter.exportMovementToExcel(cardService.getMovementReport(board.get().getId()), "C:\\Users\\GabrielAlves\\Desktop\\MovementReport.xlsx", "MovementReport");
        }else{
            pdfExporter.exportMovementReportToPDF(cardService.getMovementReport(board.get().getId()), "C:\\Users\\GabrielAlves\\Desktop\\MovementReport.pdf", "MovementReport");

        }
    }
    /**
     * Generates and exports a blocked/unblocked cards report for the selected board in Excel or PDF format.
     */
    void printBlockedReport() {
        Optional<Board> board = getBoard();
        if (board == null) return;
        if(consoleInterface.prompt(messageService.getMessage("type.report.type")).equals("1")) {
            excelExporter.exportBlockedUnblockedToExcel(cardService.getBlockedReport(board.get().getId()), "C:\\Users\\GabrielAlves\\Desktop\\blockReport.xlsx", "blockReport");
        }else{
            pdfExporter.exportBlockedUnblockedReportToPDF(cardService.getBlockedReport(board.get().getId()), "C:\\Users\\GabrielAlves\\Desktop\\blockReport.pdf", "blockReport");
        }
    }
    /**
     * Prompts the user to enter a board name or ID and retrieves the corresponding board.
     *
     * @return an Optional containing the Board if found
     */
    Optional<Board> getBoard() {
        String userInput = consoleInterface.prompt(messageService.getMessage("type.board.id.or.name"));
        String boardName = "";
        long boardId = -1;
        try {
            boardId = Long.parseLong(userInput);
            if(boardId == -1){
                return null;
            }
        } catch (NumberFormatException e) {
            boardName = userInput;
        }
        if(!boardService.existsByIdOrNameIgnoreCase(boardId,boardName)){
            consoleInterface.printMessage(messageService.getMessage("msg.error.board.not.exist"));
            return null;
        }
        return boardService.findByIdOrNameIgnoreCase(boardId,boardName);
    }
    /**
     * Updates the selected card according to the chosen operation (move, cancel, block, or unblock).
     *
     * @param cardList list of cards in the board
     * @param boardColumnList list of columns in the board
     * @param operation the operation to perform (by numeric code)
     */
    void updateCard(List<Card> cardList, List<BoardColumn> boardColumnList, int operation) {
        String userInput = consoleInterface.prompt(messageService.getMessage("type.card.id"));;
        long cardId = 0;
        Card selectedCard;
        while(true){
            try{
                cardId = Long.parseLong(userInput);
            }catch (NumberFormatException e){
                consoleInterface.printMessage(messageService.getMessage("msg.error.NumberFormatException"));
                break;
            }
            try{
                long finalCardId = cardId;
                selectedCard = cardList.stream().filter(card -> card.getId().equals(finalCardId)).toList().getFirst();
                switch (operation){
                    case 1:
                        moveCardToNextColumn(boardColumnList, selectedCard);
                        return;
                    case 2:
                        cancelCard(boardColumnList, selectedCard);
                        return;
                    case 4:
                        blockCard(selectedCard);
                        return;
                    case 5:
                        unblockCard(selectedCard);
                        return;
                    default:
                        consoleInterface.printMessage(messageService.getMessage("msg.error.invalid.option"));
                        return;
                }
            }catch (NoSuchElementException exception){
                consoleInterface.printMessage(messageService.getMessage("msg.error.card.id.not.found"));
                break;
            }
        }
    }
    /**
     * Moves the selected card to the next column, if allowed.
     *
     * @param boardColumnList list of columns in the board
     * @param selectedCard the card to move
     */
    void moveCardToNextColumn(List<BoardColumn> boardColumnList, Card selectedCard) {
        long currentBoardColumn = selectedCard.getBoardColumn().getId();

        if(selectedCard.getIsBlocked()){
            consoleInterface.printMessage(messageService.getMessage("msg.error.card.cant.be.moved"));
            editBoard();
        }
        if(currentBoardColumn+1 > (Integer.parseInt(String.valueOf(boardColumnList.getLast().getId()))-1)){
            consoleInterface.printMessage(messageService.getMessage("msg.error.card.is.on.board.last.active.column"));
            return;
        }
        selectedCard.setBoardColumn(boardColumnList.stream().filter(boardColumn -> boardColumn.getId().equals(currentBoardColumn+1)).toList().getFirst());
        selectedCard.setLastMovementDt(OffsetDateTime.now());
        cardService.save(selectedCard);
        consoleInterface.printMessage(messageService.getMessage("msg.card.moved.to.next.column"));
    }
    /**
     * Cancels the selected card by moving it to the last column (Cancelled).
     *
     * @param boardColumnList list of columns in the board
     * @param selectedCard the card to cancel
     */
    void cancelCard(List<BoardColumn> boardColumnList, Card selectedCard) {
        if (selectedCard.getBoardColumn().getName().equalsIgnoreCase("Cancelled")){
            consoleInterface.printMessage(messageService.getMessage("msg.error.card.already.cancelled"));
            return;
        }
        selectedCard.setBoardColumn(boardColumnList.getLast());
        if(selectedCard.getIsBlocked()){
            selectedCard.setLastUnblockedDt(OffsetDateTime.now());
            selectedCard.setUnblockedReason(null);
            selectedCard.setBlockedReason(null);
            selectedCard.setIsBlocked(false);
        }
        selectedCard.setLastMovementDt(OffsetDateTime.now());
        cardService.save(selectedCard);
        consoleInterface.printMessage(messageService.getMessage("msg.card.cancelled"));
    }
    /**
     * Unblocks the selected card and sets the unblock reason and date.
     *
     * @param selectedCard the card to unblock
     */
    void unblockCard(Card selectedCard) {
        if(!selectedCard.getIsBlocked()){
            consoleInterface.printMessage(messageService.getMessage("msg.error.impossible.do.unblock"));
            return;
        }
        String unblockReason = consoleInterface.prompt(messageService.getMessage("type.unblock.reason"));
        selectedCard.setIsBlocked(false);
        selectedCard.setBlockedReason(null);
        selectedCard.setLastBlockedDt(null);
        selectedCard.setLastUnblockedDt(OffsetDateTime.now());
        selectedCard.setUpdateDt(OffsetDateTime.now());
        selectedCard.setUnblockedReason(unblockReason);
        cardService.save(selectedCard);
    }
    /**
     * Blocks the selected card and sets the block reason and date.
     *
     * @param selectedCard the card to block
     */
    void blockCard(Card selectedCard) {
        if(selectedCard.getIsBlocked()){
            consoleInterface.printMessage(messageService.getMessage("msg.error.card.already.blocked"));
            return;
        }
        String blockReason = consoleInterface.prompt(messageService.getMessage("type.block.reason"));
        selectedCard.setLastBlockedDt(OffsetDateTime.now());
        selectedCard.setIsBlocked(true);
        selectedCard.setBlockedReason(blockReason);
        selectedCard.setUpdateDt(OffsetDateTime.now());
        selectedCard.setUnblockedReason(null);
        selectedCard.setLastUnblockedDt(null);
        cardService.save(selectedCard);
    }
}
