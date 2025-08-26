package com.laliga.laliga_crud_app;

import com.laliga.laliga_crud_app.player.Player;
import com.laliga.laliga_crud_app.player.PlayerRepository;
import com.laliga.laliga_crud_app.player.PlayerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class LaligaCrudAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaligaCrudAppApplication.class, args);
	}

}
