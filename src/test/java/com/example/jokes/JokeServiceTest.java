package com.example.jokes;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.jokes.model.Joke;
import com.example.jokes.service.BatchUtil;
import com.example.jokes.service.JokeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
public class JokeServiceTest {

    @Mock
    private BatchUtil batchUtil;
    @InjectMocks
    private JokeService jokeService;

    @Test
    void retrieveJokes_shouldReturnJokes_withoutCallingBatchService() {
        jokeService.retrieveJokes(1);

        verify(batchUtil, times(0)).submitForResult(any());
    }

    @Test
    void retrieveJokes_shouldReturnJokes_sucessfully() {
        when(batchUtil.submitForResult(any()))
                .thenReturn(CompletableFuture.completedFuture(new Joke("id", "type", "setup", "pipeline")));

        List<Joke> result = jokeService.retrieveJokes(10);

        verify(batchUtil, times(10)).submitForResult(any());
        verify(batchUtil, times(1)).waitForResult(any());
        assertThat(result.size()).isEqualTo(10);
        assertThat(result.get(0).id()).isEqualTo("id");
    }

}
