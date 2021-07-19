package com.challenge.yaca.commitViewer.service;

import com.challenge.yaca.commitViewer.model.Commit;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class GitCLIServiceImpl implements GitCLIService {

    private final String COMMAND_LINE = "cmd.exe";
    private final String DIR = "/c";
    private final String GIT_LOG = "git log";

    private final String SHA = "commit";
    private final String AUTHOR = "Author:";
    private final String DATE = "Date:";
    private final String MESSAGE = "    ";

    private BufferedReader executeGitLog(String repoUrl) {
        logger.info("executeGitLog for: {}", repoUrl);
        return execute(COMMAND_LINE, DIR, GIT_LOG);
    }

    @Override
    public List<Commit> getCommits(String repoUrl) {
        logger.info("Executing {} command line:", COMMAND_LINE);
        logger.info("\t Path: {}", DIR);
        logger.info("\t Command: {}", GIT_LOG);
        logger.info("\t\t Repository: {}", repoUrl);
        try {
            BufferedReader logResult = execute(COMMAND_LINE, DIR, GIT_LOG);
            String line;
            while (true) {
                line = logResult.readLine();
                if (line == null) { break; }
                System.out.println(line);
            }
            return emptyList();
        } catch (IOException e) {
            return emptyList();
        }
    }



}
