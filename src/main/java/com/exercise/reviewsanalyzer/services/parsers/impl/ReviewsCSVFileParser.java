package com.exercise.reviewsanalyzer.services.parsers.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import com.exercise.reviewsanalyzer.services.parsers.ReviewParser;
import com.exercise.reviewsanalyzer.services.parsers.ReviewsFileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by itsik on 8/4/17.
 * <p>
 * Parse reviews from CSV file
 */
@Component
public class ReviewsCSVFileParser implements ReviewsFileParser {

    private final ReviewParser<String> parser;

    @Autowired
    public ReviewsCSVFileParser(ReviewParser<String> parser) {
        this.parser = parser;
    }

    @Override
    public Collection<Review> parseReviews(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            Stream<Review> reviewStream = lines.skip(1).map(parser::parse);
            //Can use set to avoid duplicates, but it won't be perfect
            //I found that we have same review text for same product with different user Ids
            //Probably would be better to do another pass on the list to clear duplicates
            return reviewStream.collect(Collectors.toList());
        }
    }
}
