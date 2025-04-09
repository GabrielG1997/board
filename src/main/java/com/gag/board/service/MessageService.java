package com.gag.board.service;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.util.Locale;
/**
 * Service responsible for managing the application's messages translating then based on the user default Locale.
 */
@Service
public class MessageService {
    private final MessageSource messageSource;

    public MessageService(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    /**
     * Service responsible for managing the application's flow through the console interface.
     *
     * @param key key id on the messageSource.
     * @param args additional optional arguments to be used on the string
     *
     * @return String to be used as the message
     */
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, Locale.getDefault());
    }
}

