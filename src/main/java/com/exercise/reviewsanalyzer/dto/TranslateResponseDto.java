package com.exercise.reviewsanalyzer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by itsik on 8/7/17.
 * <p>
 * Translate response
 */
@Getter
@Setter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranslateResponseDto {

    //todo Handle charsets
    private String text;
}
