package com.example.naucna.utils;

import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        return String.join(SPLIT_CHAR, strings);
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return Arrays.asList(s.split(SPLIT_CHAR));
    }
}

