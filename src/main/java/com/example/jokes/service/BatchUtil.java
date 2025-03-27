package com.example.jokes.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Service
public class BatchUtil {

    @Async
    public <R> CompletableFuture<R> submitForResult(Supplier<R> supplier) {
        return CompletableFuture.completedFuture(supplier.get());
    }

    public <R> void waitForResult(List<CompletableFuture<R>> futures) {
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

}
