package com.exercise.reviewsanalyzer.services.processors;

import com.exercise.reviewsanalyzer.domain.Review;

import java.util.Collection;

/**
 * Created by itsik on 8/7/17.
 * <p>
 * Interface for reviews analyzer which returns a result
 */
public interface ReviewsAnalyzer<T> {

    T analyze(Collection<Review> reviews);
}
