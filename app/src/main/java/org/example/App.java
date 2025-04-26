package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(App.class, args);

        // try (ServerSocket serverSocket = new ServerSocket(8080)) {
        //     ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        //     System.out.println("Server started on port 8080...");

        //     while (true) {
        //         Socket clientSocket = serverSocket.accept();
        //         executor.submit(() -> handleClient(clientSocket));
        //     }
        // }
    }

    // @Bean
	// public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
	// 	return args -> {

	// 		System.out.println("Let's inspect the beans provided by Spring Boot:");

	// 		String[] beanNames = ctx.getBeanDefinitionNames();
	// 		Arrays.sort(beanNames);
	// 		for (String beanName : beanNames) {
	// 			System.out.println(beanName);
	// 		}

	// 	};
	// }
    
    // private static void handleClient(Socket clientSocket) {
    //     try (clientSocket) {
    //         System.out.println("Handling client " + clientSocket.getRemoteSocketAddress());
    //         Thread.sleep(1000); // Simulate work
    //     } catch (IOException | InterruptedException e) {
    //         e.printStackTrace();
    //     }
    // }
}
