package com.exercise.reviewsanalyzer.services.processors.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import com.exercise.reviewsanalyzer.services.processors.ReviewsProcessor;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by itsik on 8/4/17.
 * <p>
 * This is a base analyzer, which given a reviews collection and a limit (N),
 * returns the top N entities sorted alphabetically
 * Top N is determined by number of occurrences of the entity
 * <p>
 * Each concrete implementation should provide implementation for
 * converting the reviews collection to a stream of entities we want to count
 * <p>
 * The analyzer is implements callable so it can be executed in a thread pool
 */
public abstract class BaseTopReviewsAnalyzer implements ReviewsProcessor<List<String>> {

    //TODO inject
    private int DEFAULT_LIMIT = 1000;

    protected abstract Stream<String> getEntitiesStream(Collection<Review> reviews);

    protected int getLimit() {
        return DEFAULT_LIMIT;
    }

    private List<String> getTopEntities(Stream<String> entitiesStream) {
        Map<String, Long> reviewsCountMap = entitiesStream
                .collect(
                        Collectors.groupingBy(Function.identity(),
                                Collectors.counting()));
        return getSortedTopEntities(reviewsCountMap);
    }

    private List<String> getSortedTopEntities(Map<String, Long> reviewsCountMap) {
        Comparator<Map.Entry<String, Long>> valueComparator = Comparator.comparing(Map.Entry::getValue);
        Comparator<Map.Entry<String, Long>> reversedCountComparator = valueComparator.reversed();
        Comparator<Map.Entry<String, Long>> keyComparatorCaseInsensitive = Comparator.comparing(Map.Entry::getKey, String::compareToIgnoreCase);
        Stream<Map.Entry<String, Long>> topEntitiesStream = reviewsCountMap.entrySet().stream().sorted(reversedCountComparator).limit(getLimit());
        return topEntitiesStream.sorted(keyComparatorCaseInsensitive).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    @Override
    public List<String> process(Collection<Review> reviews) {
        return getTopEntities(getEntitiesStream(reviews));
    }
}
