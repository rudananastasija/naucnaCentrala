package com.example.naucna.utils;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.example.naucna.model.FormSubmissionDto;
import com.example.naucna.security.TokenUtils;

public class Utils {

    public static String getUsernameFromRequest(HttpServletRequest request, TokenUtils tokenUtils) {
        String authToken = tokenUtils.getToken(request);
        if (authToken == null) {
            return null;
        }

        String username = tokenUtils.getUsernameFromToken(authToken);
        return username;
    }

    public static HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list) {

        HashMap<String, Object> map = new HashMap<String, Object>();
        for(FormSubmissionDto temp : list){
            map.put(temp.getFieldId(), temp.getFieldValue());
        }

        return map;
    }
}