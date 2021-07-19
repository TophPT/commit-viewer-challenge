package com.challenge.yaca.commitViewer.service;

import com.challenge.yaca.commitViewer.model.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public interface GitCLIService {

    Logger logger = LoggerFactory.getLogger(GitCLIService.class);

    default BufferedReader execute(String cmdLine, String dir, String command) {
        ProcessBuilder builder = new ProcessBuilder(
                cmdLine, dir, command);
        builder.redirectErrorStream(true);
        Process p = null;
        try {
            p = builder.start();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return new BufferedReader(new InputStreamReader(Objects.requireNonNull(p).getInputStream()));
    }

    List<Commit> getCommits(String repoUrl);
}
