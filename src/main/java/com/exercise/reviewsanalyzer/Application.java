package com.exercise.reviewsanalyzer;

import com.exercise.reviewsanalyzer.domain.Review;
import com.exercise.reviewsanalyzer.services.parsers.ReviewsFileParser;
import com.exercise.reviewsanalyzer.services.processors.ReviewsProcessExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Created by itsik on 7/30/17.
 * <p>
 * Application runner
 */
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@ComponentScan
@Log4j2
public class Application implements ApplicationRunner {

    private static final String TRANSLATE_OPT = "translate";

    private final ReviewsProcessExecutor executor;

    private final ReviewsFileParser parser;

    @Autowired
    public Application(ReviewsProcessExecutor executor, ReviewsFileParser parser) {
        this.executor = executor;
        this.parser = parser;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean translate = shouldTranslate(args);
        if (args.getNonOptionArgs().size() > 0) {
            String fileName = args.getNonOptionArgs().get(0);
            run(fileName, translate);
        } else {
            System.out.println("Please provide a file path to analyze.");
        }
    }

    private boolean shouldTranslate(ApplicationArguments args) {
        return args.containsOption(TRANSLATE_OPT) && Boolean.parseBoolean(args.getOptionValues(TRANSLATE_OPT).get(0));
    }

    private void run(String fileName, boolean translate) throws Exception {
        log.debug("Starting application. Will analyze reviews from file {}.", fileName);

        Collection<Review> reviews = parseReviews(fileName);
        executeProcessor(reviews, translate);

        log.debug("Processing complete. Total memory used: {}MB", Runtime.getRuntime().totalMemory() / (1024 * 1024));

    }

    private void executeProcessor(Collection<Review> reviews, boolean translate) {
        log.debug("Starting to execute analyzers on reviews. Number of reviews: {}. Available cores: {}. Translate {}", reviews.size(), Runtime.getRuntime().availableProcessors(), translate);
        executor.execute(reviews, translate);
    }

    private Collection<Review> parseReviews(String fileName) throws Exception {
        log.debug("Start parsing reviews from file {}", fileName);
        Collection<Review> reviews = parser.parseReviews(fileName);
        log.debug("Reviews are parsed from file {}", fileName);
        return reviews;
    }

}
