package com.challenge.yaca.commitViewer.service;

import com.challenge.yaca.commitViewer.model.Commit;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GitCLIServiceImpl implements GitCLIService {

    private final String COMMAND_LINE = "cmd.exe";
    private final String DIR = "/c";
    private final String GIT_LOG = "git log";

    // rmdir /s tmpdir && y &&
    private final String GIT_LOG_REMOTE = "git clone --bare --no-checkout %s tmpdir && cd tmpdir && git log";

    private final String SHA = "commit ";
    private final String AUTHOR = "Author: ";
    private final String DATE = "Date:   ";
    private final String MESSAGE = "    ";

    private final SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);

    private Commit commitExtractor(List<Commit> commits, Commit commit, String line) {
        if (line.startsWith(SHA)) {
            String id = line.replace(SHA, "");
            commit = new Commit(id);
        }

        if (line.startsWith(AUTHOR)) {
            String author = line.replace(AUTHOR, "");
            commit.setAuthor(author);
        }

        if (line.startsWith(DATE)) {
            String dateInString = line.replace(DATE, "");
            try {
                Date date = formatter.parse(dateInString);
                commit.setDate(date);
            } catch (ParseException e) {
                logger.error("Could not parse date: {}", e.getMessage());
            }
        }

        if (line.startsWith(MESSAGE) && !line.trim().isEmpty()) {
            String message = line.replace(MESSAGE, "");
            commit.setMessage(message);
            commits.add(commit);
            logger.info("Adding 1 commit from {}", commit.getAuthor());
            logger.info("\t{}", commit.getMessage());
        }

        return commit;
    }

    @Override
    public List<Commit> getCommits(String repoUrl) {
        String command = repoUrl != null ? String.format(GIT_LOG_REMOTE, repoUrl) : GIT_LOG;
        logger.info("Executing {} command line:", COMMAND_LINE);
        logger.info("\t Path: {}", DIR);
        logger.info("\t Command: {}", command);
        logger.info("\t\t Repository: {}", repoUrl);

        List<Commit> commits = new ArrayList<>();

        try (BufferedReader logResult = execute(COMMAND_LINE, DIR, command)) {
            Commit[] commit = new Commit[1];
            logResult.lines().forEachOrdered(line -> commit[0] = commitExtractor(commits, commit[0], line));

            logger.info("{} commits returned", commits.size());
            Collections.sort(commits);
            return commits;
        } catch (IOException e) {
            return commits;
        }
    }



}
