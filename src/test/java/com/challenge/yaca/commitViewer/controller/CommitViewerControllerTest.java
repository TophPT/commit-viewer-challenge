package com.challenge.yaca.commitViewer.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.servlet.http.HttpServletRequest;

import static com.challenge.yaca.commitViewer.utils.Commands.CommandExecuteException;
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
        Mockito.when(httpServletRequest.getRequestURI()).thenReturn("/");
        commitViewerController.getCommitsByRepositoryUrl(httpServletRequest, null);
        assertTrue(true);
    }
}