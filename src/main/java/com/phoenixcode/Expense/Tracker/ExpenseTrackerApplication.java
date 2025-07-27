package com.phoenixcode.Expense.Tracker;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Expense Tracker REST API",
				version = "1.0.0",
				description = "CRUD operation for expenses, categories, and users"
		),
		servers = {
				@Server(url = "http://localhost:8080", description = "Local dev server")
		}
)
@SpringBootApplication
public class ExpenseTrackerApplication {

	public static void main(String[] args) {

		SpringApplication.run(ExpenseTrackerApplication.class, args);
	}

}