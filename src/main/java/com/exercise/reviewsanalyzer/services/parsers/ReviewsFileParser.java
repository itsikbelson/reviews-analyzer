package com.exercise.reviewsanalyzer.services.parsers;

import com.exercise.reviewsanalyzer.domain.Review;

import java.util.Collection;

/**
 * Created by itsik on 8/4/17.
 * <p>
 * Interface for parsing reviews for a source
 */
public interface ReviewsFileParser {

    Collection<Review> parseReviews(String filePath) throws Exception;
}
