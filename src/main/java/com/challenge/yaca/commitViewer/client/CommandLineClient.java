package com.challenge.yaca.commitViewer.client;

import com.challenge.yaca.commitViewer.model.Author;
import com.challenge.yaca.commitViewer.model.Commit;
import com.challenge.yaca.commitViewer.model.CommitInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Depending on the server environment, the used commands can be different.
 * Currently, we're only using windows commands, because I do not own any Linux/Unix server to run tests on.
 */
@Component("commandLineClient")
public class CommandLineClient {

    static final Logger logger = LoggerFactory.getLogger(CommandLineClient.class);

    public static class DateParseException extends ParseException {
        public DateParseException(String message, int errorOffset) {
            super(message, errorOffset);
        }
    }

    public static class CommandExecuteException extends IOException {
        public CommandExecuteException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Commands to fetch commits from any repository
     */
    final String COMMAND_LINE = "cmd.exe";
    final String DIR = "/c";

    final String REPOSITORY_URL = "https://github.com/%s/%s.git";
    final String GIT_LOG = "git clone --bare --no-checkout %s tmpdir && cd tmpdir && git log";

    final String RMDIR = "rmdir tmpdir /s /q";

    /**
     * Standard keys used for parsing fields from the console output.
     */
    final String SHA = "commit ";
    final String AUTHOR_NAME = "Author: ";
    final String DATE = "Date:   ";
    final String MESSAGE = "    ";

    /**
     * Simple way of removing the string key, from the string target
     *
     * @param target    complete line from the console log output
     * @param key       key used for string parsing, to extract the desire data
     * @return          a string containing only the information associated with the key.
     */
    private String readFieldAsString(String target, String key) {
        return target.replace(key, "");
    }

    /**
     * Same as readFileAsString, but returns a date.
     *
     * @param target    complete line from the console log output
     */
    private Date readFieldAsDate(String target) throws DateParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
        String dateInString = readFieldAsString(target, DATE);
        try {
            return formatter.parse(dateInString);
        } catch (ParseException e) {
            throw new DateParseException(String.format("Could not parse date: %s", e.getMessage()), e.getErrorOffset());
        }
    }

    /**
     * Returns the name. That's displayed before the email, that starts with <.
     */
    private String getName(String authorLine) {
        return authorLine.replaceAll("<.*", "").trim();
    }

    private Commit commitExtractor(List<Commit> commits, Commit commit, String line) throws DateParseException {
        if (line.startsWith(SHA)) {
            String id = readFieldAsString(line, SHA);
            commit = new Commit();
            commit.setId(id);
        }

        if (line.startsWith(AUTHOR_NAME)) {
            String authorLine = readFieldAsString(line, AUTHOR_NAME);
            Author author = new Author();
            String authorName = getName(authorLine);
            author.setName(authorName);
            CommitInfo commitInfo = new CommitInfo();
            commitInfo.setAuthor(author);
            commit.setCommitInfo(commitInfo);
        }

        if (line.startsWith(DATE)) {
            Date date = readFieldAsDate(line);
            commit.getCommitInfo().getAuthor().setDate(date);
        }

        if (line.startsWith(MESSAGE) && !line.trim().isEmpty()) {
            String message = readFieldAsString(line, MESSAGE);
            commit.getCommitInfo().setMessage(message);

            logger.debug("Adding 1 commit from {}", commit.getCommitInfo().getAuthor());
            logger.debug("\t{}", commit.getCommitInfo().getMessage());
            commits.add(commit);
        }

        return commit;
    }

    /**
     * Use the git CLI to retrieve a commit list
     * In order to help with testing, in case repoUrl is null, it will retrieve the commit list for the current repository
     *
     * @param owner Repository's owner name
     * @param repo  Repository's name
     * @return      commit stream, ordered by date
     * @throws CommandExecuteException  when ProcessBuilder::start() fails
     */
    public List<Commit> getCommitLogs(@NonNull String owner, @NonNull String repo) throws CommandExecuteException {
        logger.info("Using git CLI to get commits, a temporary folder will be created.");
        String command = String.format(
                GIT_LOG,
                String.format(REPOSITORY_URL, owner, repo));

        List<Commit> commits = new ArrayList<>();

        BufferedReader logResult = execute(command);
        Commit[] commit = new Commit[1];

        logResult.lines().forEachOrdered(line -> {
            try {
                commit[0] = commitExtractor(commits, commit[0], line);
            } catch (DateParseException e) {
                logger.error(e.getMessage());
            }
        });

        logger.info("{} commits returned", commits.size());
        Collections.sort(commits);

        removeTmpDir();

        return commits;
    }

    /**
     * Executes the command on windows cmd.exe, the return will need to be parsed into model classes.
     *
     * @param command   command to be executed
     * @throws CommandExecuteException  when ProcessBuilder::start() fails
     */
    BufferedReader execute(String command) throws CommandExecuteException {
        logger.info("Executing {} command line:", COMMAND_LINE);
        logger.info("\t Command: {}", command);

        ProcessBuilder builder = new ProcessBuilder(
                COMMAND_LINE, DIR, command);
        builder.redirectErrorStream(true);

        try {
            Process p = builder.start();
            return new BufferedReader(new InputStreamReader(Objects.requireNonNull(p).getInputStream()));
        } catch (IOException e) {
            throw new CommandExecuteException(e.getMessage(), e.getCause());
        }
    }

    /**
     * I didn't find any other way of getting the list of commits from git cli, without checkout.
     * So, I needed a rmdir after to clear the project folder.
     *
     * @throws CommandExecuteException  when ProcessBuilder::start() fails
     */
    void removeTmpDir() throws CommandExecuteException {
        logger.info("Executing {} command line:", COMMAND_LINE);
        logger.info("\t Command: {}", RMDIR);

        ProcessBuilder builder = new ProcessBuilder(
                COMMAND_LINE, DIR, RMDIR);
        builder.redirectErrorStream(true);

        try {
            builder.start();
        } catch (IOException e) {
            throw new CommandExecuteException(e.getMessage(), e.getCause());
        }
    }
}
