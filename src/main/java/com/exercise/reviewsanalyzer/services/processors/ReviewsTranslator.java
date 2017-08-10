package com.exercise.reviewsanalyzer.services.processors;

import com.exercise.reviewsanalyzer.domain.Review;

import java.util.Collection;

/**
 * Created by itsik on 8/7/17.
 * <p>
 * Interface for reviews translator. No return arg.
 */
public interface ReviewsTranslator {

    void translate(Collection<Review> reviews);
}
