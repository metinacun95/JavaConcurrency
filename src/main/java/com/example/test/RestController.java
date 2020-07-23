package com.example.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.*;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/test")
public class RestController {

    @GetMapping("/future")
    public void futureTest() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future future = executor.submit(() -> {

            Thread.sleep(10000);
            return "future received";
        });

        Future future1 = executor.submit(() -> {
            System.out.println("geliyorrrrr");
        });
        System.out.println("future previous");
        try {
            System.out.println(future.get(7500, TimeUnit.MILLISECONDS));
            System.out.println("future next");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException | TimeoutException e) {
            System.out.println("Timeout");
        }
    }

    @GetMapping("/completablefuture")
    public String completableFutureTest() throws ExecutionException, InterruptedException {
        //ForkJoinPool.commonPool() thread poolunu kullaniyor
        Executor executor = Executors.newCachedThreadPool();
        CompletableFuture<String> completableFuture2=CompletableFuture.supplyAsync(()->{return "test";}, executor);
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "runAsync started Thread Name => " + Thread.currentThread().getName();
        }).thenApply(result -> {
            return result + " then accept "+ Thread.currentThread().getName();
        }).thenApplyAsync(result -> {
            return result + " test " + Thread.currentThread().getName();
        });
        /*
            CompletableFuture.runAsync(() -> {islemler....})  returnsuz
            CompletableFuture.supplyAsync(() -> {islemler....})  return edilmek istenildiginde
            thenAccept return etmeden islem yapmak icin result ->  {}
            thenAppyl return etmek istenildiginde result ->  {}
            thenRun completablefuture ın sonucuna erisemez () -> {}

            CompletableFuture<String> completableFuture2=CompletableFuture.supplyAsync(()->{islemler....}, executor); kendi thread poolumuzu verebiliriz

         */
        System.out.println("completableFuture ended Thread Name => " + Thread.currentThread().getName());
        //new CompletableFuture() Aynı threadi kullaniyor http-nio-8080-exec-2
        CompletableFuture<String> completableFuture1 = new CompletableFuture();
        completableFuture1.complete("test");
        System.out.println("new CompletableFuture " + completableFuture1.get() + " Thread Name -> " + Thread.currentThread().getName());
        return completableFuture.get()+ " "+ Thread.currentThread().getName();
    }
}
