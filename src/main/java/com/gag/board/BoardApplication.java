package com.gag.board;

import com.gag.board.entity.Board;
import com.gag.board.entity.BoardColumn;
import com.gag.board.entity.Card;
import com.gag.board.exception.ExitApplicationException;
import com.gag.board.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.OffsetDateTime;
import java.util.*;

@SpringBootApplication
public class BoardApplication {
	private final ConsoleService consoleService;

	public BoardApplication(ConsoleService consoleService) {
        this.consoleService = consoleService;
    }
    public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}
	@Bean
	CommandLineRunner runner(ApplicationContext context) {
		return args -> {
			try {
				consoleService.mainMenu();
			}catch(ExitApplicationException e){
				SpringApplication.exit(context);
			}
		};
	}
}
