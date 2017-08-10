package com.exercise.reviewsanalyzer.services.processors.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

/**
 * Created by itsik on 8/7/17.
 *
 * Translator test
 */
public class ReviewTranslatorTest {

    private ReviewsTranslatorImpl translator = new ReviewsTranslatorImpl(new TranslatorConfiguration());

    @Test
    public void testTranslation() throws Exception {
        Review review = Review.builder()
                .id(123)
                .productId("PROD123")
                .profileName("USER123")
                .text("Hello John, how are you?")
                .build();
        String translate = translator.translate(review);
        Assert.assertEquals(ReviewsTranslatorImpl.DUMMY_TRANSLATED_TEXT,translate);
    }

}