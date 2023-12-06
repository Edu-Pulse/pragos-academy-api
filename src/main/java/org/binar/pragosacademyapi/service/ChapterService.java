package org.binar.pragosacademyapi.service;

import org.binar.pragosacademyapi.entity.request.ChapterRequest;
import org.binar.pragosacademyapi.entity.response.Response;

public interface ChapterService {
    Response<String> addChapter(String courseCode, ChapterRequest request);
}
