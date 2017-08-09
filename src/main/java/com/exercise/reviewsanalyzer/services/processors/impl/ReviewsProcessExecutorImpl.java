package com.exercise.reviewsanalyzer.services.processors.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import com.exercise.reviewsanalyzer.services.processors.ReviewsProcessor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * Created by itsik on 8/6/17.
 *
 *
 */
@Component
@Log4j2
public class ReviewsProcessExecutorImpl implements com.exercise.reviewsanalyzer.services.processors.ReviewsProcessExecutor {

    @Autowired
    private List<ReviewsProcessor<?>> processors;

    @Override
    public void execute(Collection<Review> reviews) {
        //TODO inject
        ForkJoinPool pool = new ForkJoinPool();
        Collection<Future<?>> tasks = submitTasks(pool, reviews);
        collectTaskResults(tasks);

        pool.shutdown();
    }

    private void collectTaskResults(Collection<Future<?>> tasks) {
        tasks.forEach(future -> {
            try {
                Object results = future.get();
                System.out.println(results);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Collection<Future<?>> submitTasks(ForkJoinPool pool, Collection<Review> reviews) {
        Collection<Future<?>> tasks = new ArrayList<>();
        processors.forEach(
                reviewsProcessor -> {
                    Callable<?> task = () -> reviewsProcessor.process(reviews);
                    tasks.add(pool.submit(task));
                    log.debug("Submitted task {}", reviewsProcessor.getClass());
                }
        );
        return tasks;
    }

}
