package com.exercise.reviewsanalyzer.domain;
/*
 * Created by itsik on 8/8/17.
 *
 * Basic pojo for review
 */

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class Review {

    @NotNull
    private Integer id;

    @NotNull
    @Pattern(regexp = "[0-9A-Z]+")
    private String productId;

    @NotNull
    @Pattern(regexp = "(#oc-)?[0-9A-Z]+")
    private String userId;

    private String profileName;

    private String text;
}
