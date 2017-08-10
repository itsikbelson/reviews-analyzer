package com.exercise.reviewsanalyzer.services.processors.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by itsik on 8/4/17.
 * <p>
 * Analyzer which calculates top N words in reviews text
 */
@Component
public class TopWordsAnalyzer extends BaseTopReviewsAnalyzer {

    @Override
    protected Stream<String> getEntitiesStream(Collection<Review> reviews) {
        return reviews.stream().parallel().flatMap(review -> Arrays.stream(review.getText().split(" "))).filter(word -> !word.isEmpty());
    }

}
