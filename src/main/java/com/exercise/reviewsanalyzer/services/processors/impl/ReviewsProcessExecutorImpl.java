package com.exercise.reviewsanalyzer.services.processors.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import com.exercise.reviewsanalyzer.services.processors.ReviewsAnalyzer;
import com.exercise.reviewsanalyzer.services.processors.ReviewsTranslator;
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
 * Executor to run analyzers and translators concurrently
 */
@Component
@Log4j2
public class ReviewsProcessExecutorImpl implements com.exercise.reviewsanalyzer.services.processors.ReviewsProcessExecutor {

    private final List<ReviewsAnalyzer<?>> analyzers;

    private final List<ReviewsTranslator> translators;

    @Autowired
    public ReviewsProcessExecutorImpl(List<ReviewsAnalyzer<?>> analyzers, List<ReviewsTranslator> translators) {
        this.analyzers = analyzers;
        this.translators = translators;
    }

    @Override
    public void execute(Collection<Review> reviews, boolean translate) {

        executeAnalyzers(reviews);

        if (translate) {
            executeTranslators(reviews);
        }
    }

    private void executeAnalyzers(Collection<Review> reviews) {
        log.debug("Start executing analyzers");
        ForkJoinPool pool = new ForkJoinPool();
        Collection<Future<?>> tasks = submitAnalyzersTasks(pool, reviews);
        collectAnalyzersResults(tasks);
        pool.shutdown();
        log.debug("Done executing analyzers");
    }

    private Collection<Future<?>> submitAnalyzersTasks(ForkJoinPool pool, Collection<Review> reviews) {
        Collection<Future<?>> tasks = new ArrayList<>();
        analyzers.forEach(
                reviewsAnalyzer -> {
                    Callable<?> task = () -> reviewsAnalyzer.analyze(reviews);
                    tasks.add(pool.submit(task));
                    log.debug("Submitted task {}", reviewsAnalyzer.getClass());
                }
        );
        return tasks;
    }

    private void collectAnalyzersResults(Collection<Future<?>> tasks) {
        tasks.forEach(future -> {
            try {
                Object results = future.get();
                System.out.println(results);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void executeTranslators(Collection<Review> reviews) {
        log.debug("Start executing translators");
        ForkJoinPool pool = new ForkJoinPool();
        translators.forEach(reviewsTranslator -> {
            Runnable task = () -> reviewsTranslator.translate(reviews);
            pool.execute(task);
        });
        pool.shutdown();
        log.debug("Done executing translators");
    }


}
