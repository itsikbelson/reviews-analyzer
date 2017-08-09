package com.exercise.reviewsanalyzer.services.processors.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import com.exercise.reviewsanalyzer.dto.TranslateRequestDto;
import com.exercise.reviewsanalyzer.dto.TranslateResponseDto;
import com.exercise.reviewsanalyzer.services.processors.ReviewsProcessor;
import lombok.extern.log4j.Log4j2;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Stream;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by itsik on 8/6/17.
 */
//@Component
@Log4j2
public class ReviewsTranslatorImpl implements ReviewsProcessor<Object> {
    //TODO generify with other analyzers
    //TODO inject configurations (configurations should be central)
    //TODO support multiple languages
    private static final int DEFAULT_TIMEOUT_MS = 1000;
    private static final String INPUT_LANGUAGE = "en";
    private static final String OUTPUT_LANGUAGE = "fr";
    private static final String TRANSLATOR_URL = "https://api.google.com/translate";
    private static final int REVIEW_MAX_LEN = 1000;

    @Mock
    private RestTemplate restTemplate = new RestTemplate(createFactory());

    private static final Charset CHARSET = Charset.forName("UTF-8");

    public ReviewsTranslatorImpl() {
        initMock();
    }

    private void initMock() {
        MockitoAnnotations.initMocks(this);
        TranslateResponseDto translateResponse = TranslateResponseDto.builder()
                .text("Salut Jéan, comment vas tu?")
                .build();
        ResponseEntity<TranslateResponseDto> responseEntity = new ResponseEntity<>(translateResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(eq(TRANSLATOR_URL), anyObject(), eq(TranslateResponseDto.class))).thenReturn(responseEntity);
    }

    private HttpComponentsClientHttpRequestFactory createFactory() {
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectTimeout(DEFAULT_TIMEOUT_MS);
        requestFactory.setReadTimeout(DEFAULT_TIMEOUT_MS);
        return requestFactory;
    }

    public void translate(Review review) {

        HttpHeaders headers = createHeaders();
        TranslateRequestDto translateRequest = createTranslateRequest(review);
        HttpEntity<TranslateRequestDto> translateRequestEntity = new HttpEntity<>(translateRequest, headers);
        ResponseEntity<TranslateResponseDto> translateResponseEntity = restTemplate.postForEntity(TRANSLATOR_URL, translateRequestEntity, TranslateResponseDto.class);
        if (!translateResponseEntity.getStatusCode().is2xxSuccessful()) {
            log.error("Could not translate review: {}",review);
        }
    }

    private TranslateRequestDto createTranslateRequest(Review review) {

        String trimmedReviewText = review.getText().substring(0, Math.min(REVIEW_MAX_LEN,review.getText().length()));
        return TranslateRequestDto.builder()
                .inputLanguage(INPUT_LANGUAGE)
                .outputLanguage(OUTPUT_LANGUAGE)
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
    public Object process(Collection<Review> reviews) {
        Stream<Review> reviewStream = reviews.parallelStream();
        new ForkJoinPool().submit(() -> reviewStream.forEach(this::translate));
        return 0;
    }
}
