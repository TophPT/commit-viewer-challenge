package com.challenge.yaca.commitViewer.controller;

import com.challenge.yaca.commitViewer.client.CommandLineClient.CommandExecuteException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CommitViewerControllerTest {

    @Autowired
    private CommitViewerController commitViewerController;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void contextLoads() {
        assertNotNull(commitViewerController);
        assertNotNull(httpServletRequest);
    }

    @Test
    void shouldGetCommitsForCurrentRepository() throws CommandExecuteException {
        commitViewerController.getCommitsByRepositoryUrl("stayawayinesctec","stayaway-app");
        assertTrue(true);
    }
}