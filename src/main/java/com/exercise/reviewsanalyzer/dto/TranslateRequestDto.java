package com.exercise.reviewsanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by itsik on 8/7/17.
 * <p>
 * Translate request
 */
@Getter
@Setter
@Builder
@ToString
public class TranslateRequestDto {

    @JsonProperty("input_lang")
    private String inputLanguage;

    @JsonProperty("output_lang")
    private String outputLanguage;

    private String text;
}
