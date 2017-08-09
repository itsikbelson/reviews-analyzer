package com.exercise.reviewsanalyzer.services.parsers;

import com.exercise.reviewsanalyzer.domain.Review;

/**
 * Created by itsik on 7/31/17.
 * <p>
 * Interface for parsing review from input
 */
public interface ReviewParser<T> {

    Review parse(T reviewInput);
}
