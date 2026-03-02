package com.thaoan.taskmanager;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Habilita o suporte a auditoria (createdAt, updatedAt)
public class TaskmanagerApplication {

    public static void main(String[] args) {
        // 1. Tenta carregar o arquivo .env
        Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing() 
            .load();

        // 2. Injeta as variáveis e imprime uma confirmação estilizada
        System.out.println(" Checking .env file variables...");
        
        dotenv.entries().forEach(entry -> {
            System.setProperty(entry.getKey(), entry.getValue());
            // Imprime apenas a chave e o valor para conferência (Cuidado com senhas em prod!)
            if (entry.getKey().equals("DB_URL")) {
                System.out.println("--- Found DB_URL: " + entry.getValue());
            }
        });

        System.out.println(" Environment variables loaded successfully! Starting Spring Boot...");
        System.out.println("------------------------------------------------------------------");

        // 3. Inicia a aplicação
        SpringApplication.run(TaskmanagerApplication.class, args);
    }
}