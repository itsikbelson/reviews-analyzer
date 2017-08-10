package com.exercise.reviewsanalyzer.services.processors;

import com.exercise.reviewsanalyzer.domain.Review;

import java.util.Collection;

/**
 * Created by itsik on 8/8/17.
 * <p>
 * Interface for execution of analyzers and translators on revies
 */
public interface ReviewsProcessExecutor {
    void execute(Collection<Review> reviews, boolean translate);
}
