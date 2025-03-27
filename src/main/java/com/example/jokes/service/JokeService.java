package com.example.jokes.service;

import com.example.jokes.model.Joke;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class JokeService {

    public static final String JOKES_URL = "https://official-joke-api.appspot.com/random_joke ";
    public static final int DEFAULT_BATCH_SIZE = 10;

    private final RestTemplate restTemplate;
    private final BatchUtil batchUtil;

    public List<Joke> retrieveJokes(Integer count) {
        if (count > 1) {
            return getBatches(count);
        } else {
            return List.of(getJokeFromProvider());
        }
    }

    private List<Joke> getBatches(Integer count) {
        List<Joke> result = Lists.newArrayList();
        List<List<Integer>> batches = Lists.partition(
                IntStream.range(0, count)
                        .boxed()
                        .toList(), DEFAULT_BATCH_SIZE
        );

        for (List<Integer> batch : batches) {
            List<CompletableFuture<Joke>> jokesFutures = IntStream.range(0, batch.size())
                    .mapToObj(i -> batchUtil.submitForResult(this::getJokeFromProvider))
                    .toList();

            batchUtil.waitForResult(jokesFutures);

            result.addAll(jokesFutures.stream()
                    .map(CompletableFuture::join)
                    .toList());
        }

        return result;
    }

    private Joke getJokeFromProvider() {
        log.info("Retrieving joke");
        return restTemplate.getForObject(JOKES_URL, Joke.class);
    }

}
