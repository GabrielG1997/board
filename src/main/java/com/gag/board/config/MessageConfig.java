package com.gag.board.config;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
/**
 * Configuration class that defines the message source for internationalization (i18n).
 *<p>
 * This class registers a {@link MessageSource} bean used to resolve messages from
 * resource bundles, enabling support for multiple languages in the application.
 */
@Configuration
public class MessageConfig {
    /**
     * Creates and configures the {@link MessageSource} bean used for resolving messages
     * from property files (e.g., messages.properties, messages_en.properties).
     * <p>
     * The resource bundle is reloaded automatically and UTF-8 encoding is used to support
     * international characters. If a message code is not found, the code itself is returned
     * as the default message.
     *
     * @return the configured {@link MessageSource} bean
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }
}
