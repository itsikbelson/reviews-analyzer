package com.exercise.reviewsanalyzer.services.parsers.impl;

import com.exercise.reviewsanalyzer.domain.Review;
import org.junit.Assert;
import org.junit.Test;
import com.exercise.reviewsanalyzer.services.parsers.impl.ReviewCSVParser;

/**
 * Created by itsik on 7/31/17.
 *
 */
public class ReviewParserCSVTest {

    private ReviewCSVParser parser = new ReviewCSVParser();

    @Test(expected = IllegalArgumentException.class)
    public void testMissingId()
    {
        parser.parse(",B00813GRG4,A1D87F6ZCVE5NK,dll pa,0,0,1,1346976000,Not as Advertised,\"Product arrived labeled as Jumbo Salted Peanuts...the peanuts were actually small sized unsalted. Not sure if this was an error or if the vendor intended to represent the product as \"\"Jumbo\"\".\"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNonNumericId()
    {
        parser.parse("ASAS,B00813GRG4,A1D87F6ZCVE5NK,dll pa,0,0,1,1346976000,Not as Advertised,\"Product arrived labeled as Jumbo Salted Peanuts...the peanuts were actually small sized unsalted. Not sure if this was an error or if the vendor intended to represent the product as \"\"Jumbo\"\".\"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingProductId()
    {
        parser.parse("2,,A1D87F6ZCVE5NK,dll pa,0,0,1,1346976000,Not as Advertised,\"Product arrived labeled as Jumbo Salted Peanuts...the peanuts were actually small sized unsalted. Not sure if this was an error or if the vendor intended to represent the product as \"\"Jumbo\"\".\"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidProductId()
    {
        parser.parse("2,$B00813GRG4,A1D87F6ZCVE5NK,dll pa,0,0,1,1346976000,Not as Advertised,\"Product arrived labeled as Jumbo Salted Peanuts...the peanuts were actually small sized unsalted. Not sure if this was an error or if the vendor intended to represent the product as \"\"Jumbo\"\".\"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMissingUserId()
    {
        parser.parse("2,B00813GRG4,,dll pa,0,0,1,1346976000,Not as Advertised,\"Product arrived labeled as Jumbo Salted Peanuts...the peanuts were actually small sized unsalted. Not sure if this was an error or if the vendor intended to represent the product as \"\"Jumbo\"\".\"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidUserId()
    {
        parser.parse("2,B00813GRG4,$A1D87F6ZCVE5NK,dll pa,0,0,1,1346976000,Not as Advertised,\"Product arrived labeled as Jumbo Salted Peanuts...the peanuts were actually small sized unsalted. Not sure if this was an error or if the vendor intended to represent the product as \"\"Jumbo\"\".\"");
    }

    @Test
    public void testValidReview()
    {
        Review review = parser.parse("2,B00813GRG4,A1D87F6ZCVE5NK,dll pa,0,0,1,1346976000,Not as Advertised,\"Product arrived labeled as Jumbo Salted Peanuts...the peanuts were actually small sized unsalted. Not sure if this was an error or if the vendor intended to represent the product as \"\"Jumbo\"\".\"");
        Assert.assertEquals(Long.valueOf(2), review.getId());
        Assert.assertEquals("B00813GRG4", review.getProductId());
        Assert.assertEquals("A1D87F6ZCVE5NK", review.getUserId());
        Assert.assertEquals("dll pa", review.getProfileName());
        //TODO clear enclosing quotes
        Assert.assertEquals("\"Product arrived labeled as Jumbo Salted Peanuts...the peanuts were actually small sized unsalted. Not sure if this was an error or if the vendor intended to represent the product as \"\"Jumbo\"\".\"", review.getText());
    }

    @Test
    public void testValidReviewWithCommaWithinReviewText()
    {
        Review review = parser.parse("26,B001GVISJM,A3FONPR03H3PJS,\"Deborah S. Linzer \"\"Cat Lady\"\"\",0,0,5,1288310400,Twizzlers - Strawberry,\"Product received is as advertised.<br /><br /><a href=\"\"http://www.amazon.com/gp/product/B001GVISJM\"\">Twizzlers, Strawberry, 16-Ounce Bags (Pack of 6)</a>\"");
        Assert.assertEquals(Long.valueOf(26), review.getId());
        Assert.assertEquals("B001GVISJM", review.getProductId());
        Assert.assertEquals("A3FONPR03H3PJS", review.getUserId());
        Assert.assertEquals("\"Deborah S. Linzer \"\"Cat Lady\"\"\"", review.getProfileName());
        //TODO clear enclosing quotes
        Assert.assertEquals("\"Product received is as advertised.<br /><br /><a href=\"\"http://www.amazon.com/gp/product/B001GVISJM\"\">Twizzlers, Strawberry, 16-Ounce Bags (Pack of 6)</a>\"", review.getText());
    }

}