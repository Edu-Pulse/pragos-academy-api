package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.Chapter;
import org.binar.pragosacademyapi.entity.Course;
import org.binar.pragosacademyapi.entity.DetailChapter;
import org.binar.pragosacademyapi.entity.request.ChapterRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.repository.ChapterRepository;
import org.binar.pragosacademyapi.repository.CourseRepository;
import org.binar.pragosacademyapi.service.ChapterService;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class ChapterServiceImpl implements ChapterService {
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public ChapterServiceImpl(ChapterRepository chapterRepository, CourseRepository courseRepository){
        this.chapterRepository = chapterRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Response<String> addChapter(String courseCode, ChapterRequest request) {
        Response<String> response = new Response<>();
        try {
            Course course = courseRepository.findById(courseCode).orElse(null);
            if (course != null){
                DetailChapter detailChapter = new DetailChapter();
                detailChapter.setName(request.getTitle());
                detailChapter.setVideo(request.getVideo());
                detailChapter.setMaterial(request.getMaterial());

                Chapter chapter = chapterRepository.chapterByCourseAndName(course.getCode(), request.getChapterName()).orElse(null);
                if (chapter != null){
                    detailChapter.setChapter(chapter);
                    if (chapter.getDetailChapters().isEmpty()){
                        chapter.setDetailChapters(new ArrayList<>());
                        chapter.getDetailChapters().add(detailChapter);
                    }else {
                        chapter.getDetailChapters().add(detailChapter);
                    }
                    chapterRepository.save(chapter);
                }else {
                    Chapter newChapter = new Chapter();
                    newChapter.setCourse(course);
                    newChapter.setCapther(request.getChapterName());
                    detailChapter.setChapter(newChapter);
                    newChapter.setDetailChapters(new ArrayList<>());
                    newChapter.getDetailChapters().add(detailChapter);
                    course.getChapters().add(newChapter);
                    courseRepository.save(course);
                }
                response.setError(false);
                response.setMessage("Success add chapter");
            }else {
                response.setError(true);
                response.setMessage("Course not found");
            }
        }catch (Exception e){
            response.setError(false);
            response.setMessage(ResponseUtils.MESSAGE_FAILED);
        }
        return response;
    }
}
