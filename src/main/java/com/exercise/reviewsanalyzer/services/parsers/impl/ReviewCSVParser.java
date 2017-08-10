package com.exercise.reviewsanalyzer.services.parsers.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import com.exercise.reviewsanalyzer.services.parsers.ReviewParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Created by itsik on 7/31/17.
 * <p>
 * Parsing review from a CSV line
 */
@Component
public class ReviewCSVParser implements ReviewParser<String> {


    private static final int COLUMN_ID = 0;
    private static final int COLUMN_PRODUCT_ID = 1;
    private static final int COLUMN_USER_ID = 2;
    private static final int COLUMN_PROFILE_NAME = 3;
    private static final int COLUMN_TEXT = 9;

    private Validator validator;

    public ReviewCSVParser() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public Review parse(String reviewInput) throws IllegalArgumentException {
        //TODO did not handle commas within quotes
        String[] columns = StringUtils.commaDelimitedListToStringArray(reviewInput);
        Integer id = getId(columns[COLUMN_ID]);
        Review review = Review.builder()
                .id(id)
                .productId(columns[COLUMN_PRODUCT_ID])
                .userId(columns[COLUMN_USER_ID])
                .profileName(columns[COLUMN_PROFILE_NAME])
                .text(columns[COLUMN_TEXT])
                .build();
        //TODO validation is exhaustive so skipped
//        validate(review);
        return review;
    }

    private Integer getId(String column) throws IllegalArgumentException {
        try {
            return Integer.valueOf(column);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    private void validate(Review review) throws IllegalArgumentException {
        Set<ConstraintViolation<Review>> violations = validator.validate(review);
        if (violations.size() > 0) {
            throw new IllegalArgumentException("Review invalid: " + review + "Violations: " + violations.toString());
        }

    }
}
