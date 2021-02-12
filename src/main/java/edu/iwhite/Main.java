package edu.iwhite;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // create iterator for Reviews
        final ObjectReader reviewMapper = new ObjectMapper().readerFor(Review.class);
        MappingIterator<Object> reviewIterator = reviewMapper.readValues(new File("C:\\Users\\ianw1\\Desktop\\Yelp Reviews\\review.json"));

        // create iterator for Businesses
        final ObjectReader businessMapper = new ObjectMapper().readerFor(Business.class);
        MappingIterator<Object> businessIterator = businessMapper.readValues(new File("C:\\Users\\ianw1\\Desktop\\Yelp Reviews\\business.json"));

    }
}
