package com.exercise.reviewsanalyzer.services.processors;

import com.exercise.reviewsanalyzer.domain.Review;

import java.util.Collection;

/**
 * Created by itsik on 8/7/17.
 */
public interface ReviewsProcessor<T> {

    T process(Collection<Review> reviews);
}
