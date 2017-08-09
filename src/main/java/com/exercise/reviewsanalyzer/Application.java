package com.exercise.reviewsanalyzer;

import com.exercise.reviewsanalyzer.domain.Review;
import com.exercise.reviewsanalyzer.services.parsers.ReviewsFileParser;
import com.exercise.reviewsanalyzer.services.processors.ReviewsProcessExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;

/**
 * Created by itsik on 7/30/17.
 *
 * Application runner
 *
 */
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties
@SpringBootApplication
@ComponentScan(basePackages = "com.exercise")
@Log4j2
public class Application implements ApplicationRunner {

    @Autowired
    private ReviewsProcessExecutor executor;

    @Autowired
    private ReviewsFileParser parser;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //TODO read translated arg
        //TODO read all args
        if (args.getNonOptionArgs().size() > 0)
        {
            run(args.getNonOptionArgs().get(0));
        }
        else
        {
            System.out.println("Please provide a file path to analyze.");
        }

    }

    public void run(String fileName) throws Exception {
        log.debug("Starting application. Will analyze reviews from file {}.",fileName);

        Collection<Review> reviews = parseReviews(fileName);
        executeProcessor(reviews);

        log.debug("Processing complete. Total memory used: {}MB",Runtime.getRuntime().totalMemory() / (1024*1024));

    }

    private void executeProcessor(Collection<Review> reviews) {
        log.debug("Starting to execute analyzers on reviews. Number of reviews: {}. Available cores: {}",reviews.size(),Runtime.getRuntime().availableProcessors());
        executor.execute(reviews);
    }

    private Collection<Review> parseReviews(String fileName) throws Exception {
        Collection<Review> reviews = parser.parseReviews(fileName);
        log.debug("Reviews are parsed from file {}",fileName);
        return reviews;
    }

}
