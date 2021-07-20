package com.challenge.yaca.commitViewer.utils;

import com.challenge.yaca.commitViewer.model.Commit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Depending on the server environment, the used commands can be different.
 * Currently, we're only using windows commands, because I do not own any Linux/Unix server to run tests on.
 *
 * Stateless utility class
 */
public final class Commands {

    static Logger logger = LoggerFactory.getLogger(Commands.class);

    /**
     * Private constructor to prevent instantiation
     */
    private Commands() { }

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
    public static final String COMMAND_LINE = "cmd.exe";
    public static final String DIR = "/c";

    public static final String GIT_LOG = "git log";
    public static final String GIT_LOG_REMOTE = "git clone --bare --no-checkout %s tmpdir && cd tmpdir && git log";

    public static  final String RMDIR = "rmdir tmpdir /s /q";

    /**
     * Date format to be used to get the dates from the console output.
     */
    public static final SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);

    /**
     * Standard keys used for parsing fields from the console output.
     * TODO a more detailed output could be created to improve parsing code
     */
    private static final String SHA = "commit ";
    private static final String AUTHOR = "Author: ";
    private static final String DATE = "Date:   ";
    private static final String MESSAGE = "    ";

    /**
     * TODO this might be handy if any format is done to the output itself, currently a simple replace works.
     *
     * @param target    complete line from the console log output
     * @param key       key used for string parsing, to extract the desire data
     * @return          a string containing only the information associated with the key.
     */
    private static String readFieldAsString(String target, String key) {
        return target.replace(key, "");
    }

    /**
     * Same as readFileAsString, but returns a date.
     * @param target    complete line from the console log output
     */
    private static Date readFieldAsDate(String target) throws DateParseException {
        String dateInString = readFieldAsString(target, DATE);
        try {
            return formatter.parse(dateInString);
        } catch (ParseException e) {
            throw new DateParseException(String.format("Could not parse date: %s", e.getMessage()), e.getErrorOffset());
        }
    }

    /**
     * TODO this process could be improved, but a more complex map on the output needed to be performed.
     */
    private static Commit commitExtractor(List<Commit> commits, Commit commit, String line) throws DateParseException {
        if (line.startsWith(SHA)) {
            String id = readFieldAsString(line, SHA);
            commit = new Commit(id);
        }

        if (line.startsWith(AUTHOR)) {
            String author = readFieldAsString(line, AUTHOR);
            commit.setAuthor(author);
        }

        if (line.startsWith(DATE)) {
            Date date = readFieldAsDate(line);
            commit.setDate(date);
        }

        if (line.startsWith(MESSAGE) && !line.trim().isEmpty()) {
            String message = line.replace(MESSAGE, "");
            commit.setMessage(message);
            commits.add(commit);
            logger.debug("Adding 1 commit from {}", commit.getAuthor());
            logger.debug("\t{}", commit.getMessage());
        }

        return commit;
    }

    /**
     * Use the git CLI to retrieve a commit list
     * In order to help with testing, in case repoUrl is null, it will retrieve the commit list for the current repository
     * @param repoUrl   GitHub url
     * @return          commit list, ordered by date
     */
    public static List<Commit> readCommitsFromCommandLine(String repoUrl) throws CommandExecuteException {
        String command = repoUrl != null ? String.format(GIT_LOG_REMOTE, repoUrl) : GIT_LOG;

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
     * @throws CommandExecuteException  when something goes wrong with ProcessBuilder::start
     */
    private static BufferedReader execute(String command) throws CommandExecuteException {
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

    private static void removeTmpDir() throws CommandExecuteException {
        logger.info("Executing {} command line:", COMMAND_LINE);
        logger.info("\t Command: {}", RMDIR);

        ProcessBuilder builder = new ProcessBuilder(
                COMMAND_LINE, DIR, RMDIR);
        builder.redirectErrorStream(true);

        try {
            Process p = builder.start();
        } catch (IOException e) {
            throw new CommandExecuteException(e.getMessage(), e.getCause());
        }
    }
}
