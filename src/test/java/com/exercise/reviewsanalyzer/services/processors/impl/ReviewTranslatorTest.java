package com.exercise.reviewsanalyzer.services.processors.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import org.junit.Test;

/**
 * Created by itsik on 8/7/17.
 */
public class ReviewTranslatorTest {

    private ReviewsTranslatorImpl translator = new ReviewsTranslatorImpl();

    @Test
    public void testTranslation() throws Exception {
        Review review = Review.builder()
                .id(123)
                .productId("PROD123")
                .profileName("USER123")
                .text("Hello John, how are you?")
                .build();
        translator.translate(review);
    }

}