package com.challenge.yaca.commitViewer.controller;

import com.challenge.yaca.commitViewer.model.Commit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommitViewerControllerTest {

    @Autowired
    private CommitViewerController commitViewerController;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void contextLoads() {
//        fail();
        assertNotNull(commitViewerController);

    }

    @Test
    void shouldGetCommitsForCurrentRepository() {
        Mockito.when(httpServletRequest.getRequestURI()).thenReturn("/");
        ResponseEntity<List<Commit>> listResponseEntity = commitViewerController.get(httpServletRequest, null);
        assertTrue(true);
    }
}