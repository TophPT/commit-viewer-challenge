package com.challenge.yaca.commitViewer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class CommitViewerApplication {

    private static final Logger logger = LoggerFactory.getLogger(CommitViewerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CommitViewerApplication.class, args);
        logger.info("CommitViewerApplication is now listenning on: http://localhost:8080/api/commits");
    }

}


