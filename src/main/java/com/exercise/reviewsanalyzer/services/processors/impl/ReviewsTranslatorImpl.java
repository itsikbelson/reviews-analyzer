package com.exercise.reviewsanalyzer.services.processors.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import com.exercise.reviewsanalyzer.dto.TranslateRequestDto;
import com.exercise.reviewsanalyzer.dto.TranslateResponseDto;
import com.exercise.reviewsanalyzer.services.processors.ReviewsTranslator;
import lombok.extern.log4j.Log4j2;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;

/**
 * Created by itsik on 8/6/17.
 * <p>
 * Mock implementation for translating reviews using Google API
 * <p>
 * //TODO inject mock based on profile
 */
@Component
@Log4j2
public class ReviewsTranslatorImpl implements ReviewsTranslator {

    private final TranslatorConfiguration configuration;

    private RestTemplate restTemplate;

    private static final Charset CHARSET = Charset.forName("UTF-8");

    static final String DUMMY_TRANSLATED_TEXT = "Salut Jéan, comment vas tu?";

    @Autowired
    public ReviewsTranslatorImpl(TranslatorConfiguration configuration) {
        this.configuration = configuration;
        initMock();
    }

    private void initMock() {
        restTemplate = Mockito.mock(RestTemplate.class);
        TranslateResponseDto translateResponse = TranslateResponseDto.builder()
                .text(DUMMY_TRANSLATED_TEXT)
                .build();
        ResponseEntity<TranslateResponseDto> responseEntity = new ResponseEntity<>(translateResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), anyObject(), eq(TranslateResponseDto.class))).thenReturn(responseEntity);
    }

    private HttpComponentsClientHttpRequestFactory createFactory() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(configuration.getApiTimeoutMs());
        requestFactory.setReadTimeout(configuration.getApiTimeoutMs());
        return requestFactory;
    }

    public String translate(Review review) {

        HttpHeaders headers = createHeaders();
        TranslateRequestDto translateRequest = createTranslateRequest(review);
        HttpEntity<TranslateRequestDto> translateRequestEntity = new HttpEntity<>(translateRequest, headers);
        ResponseEntity<TranslateResponseDto> translateResponseEntity = restTemplate.postForEntity(configuration.getApiUrl(), translateRequestEntity, TranslateResponseDto.class);
        if (translateResponseEntity.getStatusCode().is2xxSuccessful()) {
            log.trace("Translated review - ID: {}", review.getId());
            return translateResponseEntity.getBody().getText();
        } else {
            log.error("Could not translate review: {}", review);
            return null;
        }
    }

    private TranslateRequestDto createTranslateRequest(Review review) {

        String trimmedReviewText = review.getText().substring(0, Math.min(configuration.getApiMaxLength(), review.getText().length()));
        return TranslateRequestDto.builder()
                .inputLanguage(configuration.getInputLanguage())
                .outputLanguage(configuration.getOutputLanguage())
                .text(trimmedReviewText)
                .build();
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAcceptCharset(Collections.singletonList(CHARSET));
        return headers;
    }

    @Override
    public void translate(Collection<Review> reviews) {
        Stream<Review> reviewStream = reviews.parallelStream();
        new ForkJoinPool(configuration.getParallelism()).submit(() -> reviewStream.forEach(this::translate));
    }
}
