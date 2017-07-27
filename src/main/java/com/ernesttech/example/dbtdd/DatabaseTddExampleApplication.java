package com.ernesttech.example.dbtdd;

import com.ernesttech.example.dbtdd.runner.DbTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableAutoConfiguration
public class DatabaseTddExampleApplication implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseTddExampleApplication.class);

    @Autowired
    private DbTestRunner dbTestRunner;


    @Override
    public void run(String... args) {

        LOGGER.info("Starting");

        dbTestRunner.run();

        LOGGER.info("Finished");

    }


    public static void main(String[] args) {
        SpringApplication.run(DatabaseTddExampleApplication.class, args);
    }

}
