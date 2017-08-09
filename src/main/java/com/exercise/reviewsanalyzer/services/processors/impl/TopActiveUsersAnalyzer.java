package com.exercise.reviewsanalyzer.services.processors.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by itsik on 8/4/17.
 * <p>
 * Analyzer which calculates top N active users (writing the most reviews)
 */
@Component
public class TopActiveUsersAnalyzer extends BaseTopReviewsAnalyzer {

    @Override
    protected Stream<String> getEntitiesStream(Collection<Review> reviews) {
        return reviews.stream().parallel().map(Review::getProfileName);
    }

}
