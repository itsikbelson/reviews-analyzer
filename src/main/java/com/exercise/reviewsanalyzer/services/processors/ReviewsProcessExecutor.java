package com.exercise.reviewsanalyzer.services.processors;

import com.exercise.reviewsanalyzer.domain.Review;

import java.util.Collection;

/**
 * Created by itsik on 8/8/17.
 */
public interface ReviewsProcessExecutor {
    void execute(Collection<Review> reviews);
}
