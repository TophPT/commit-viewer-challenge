package com.challenge.yaca.commitViewer.client;

import com.challenge.yaca.commitViewer.model.Commit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.challenge.yaca.commitViewer.client.CommandLineClient.CommandExecuteException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommandLineClientTest {

    @Mock
    private CommandLineClient commandLineClient;

    @BeforeEach
    void setUp() throws CommandExecuteException {
        assertNotNull(commandLineClient);
        doNothing().when(commandLineClient).removeTmpDir();
        doCallRealMethod().when(commandLineClient).getCommitLogs(anyString(), anyString());
    }

    @Test
    void shouldReturnCommitObject() throws CommandExecuteException, ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy", Locale.ENGLISH);
        String commitId = "some-commit";
        String authorName = "some-author";
        Date date = formatter.parse("Thu Jul 22 11:43:40 2021");
        String message = "some commit message";

        String log = "commit "+ commitId + "\n" +
                "Author: " + authorName + " <some.email@example.com>\n" +
                "Date:   Thu Jul 22 11:43:40 2021\n\n" +
                "    " + message;

        doReturn(new BufferedReader(new StringReader(log)))
                .when(commandLineClient)
                .execute(anyString());

        List<Commit> commitLogs = commandLineClient.getCommitLogs(anyString(), anyString());

        assertEquals(commitLogs.size(), 1);
        assertEquals(commitLogs.get(0).getId(), commitId);
        assertEquals(commitLogs.get(0).getCommitInfo().getAuthor().getName(), authorName);
        assertEquals(commitLogs.get(0).getCommitInfo().getAuthor().getDate(), date);
        assertEquals(commitLogs.get(0).getCommitInfo().getMessage(), message);
    }

}