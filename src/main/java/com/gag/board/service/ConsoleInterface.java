package com.gag.board.service;

import org.springframework.stereotype.Component;

import java.util.Scanner;
/**
 * ConsoleInterface is responsible for handling user interaction via the console.
 * It provides methods to prompt user input and print messages using localized messages
 * from the {@link MessageService}.
 */
@Component
public class ConsoleInterface {
    private final Scanner scanner = new Scanner(System.in);
    private final MessageService messageService;
    /**
     * Constructs a ConsoleInterface with the specified MessageService.
     *
     * @param messageService the service used to retrieve localized messages
     */
    public ConsoleInterface(MessageService messageService) {
        this.messageService = messageService;
    }
    /**
     * Prompts the user with a message retrieved by key and returns the user's input.
     *
     * @param messageKey the key for the localized message to display
     * @return the user input as a String
     */
    public String prompt(String messageKey) {
        System.out.println(messageService.getMessage(messageKey));
        return scanner.nextLine();
    }
    /**
     * Prints a message to the console retrieved by the specified key.
     *
     * @param messageKey the key for the localized message to display
     */
    public void printMessage(String messageKey) {
        System.out.println(messageService.getMessage(messageKey));
    }
}

