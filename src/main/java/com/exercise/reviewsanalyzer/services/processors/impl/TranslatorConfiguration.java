package com.exercise.reviewsanalyzer.services.processors.impl;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by itsik on 8/9/17.
 * <p>
 * Holds translator configurations - API details, input/output language and parallelism
 */
@Component
@Getter
public class TranslatorConfiguration {

    @Value("${translator.api.url}")
    private String apiUrl;

    @Value("${translator.api.timeout.ms}")
    private int apiTimeoutMs;

    @Value("${translator.api.length.max}")
    private int apiMaxLength;

    @Value("${translator.language.input}")
    private String inputLanguage;

    @Value("${translator.language.output}")
    private String outputLanguage;

    @Value("${translator.executor.parallelism}")
    private int parallelism;
}
