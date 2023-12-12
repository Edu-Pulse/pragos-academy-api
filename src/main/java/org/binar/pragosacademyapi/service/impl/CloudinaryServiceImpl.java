package org.binar.pragosacademyapi.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.binar.pragosacademyapi.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;
    @Override
    public String uploadImage(MultipartFile image) {
        try{
            Map<?,?> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(image.getName(), image.getName()));
            return uploadResult.get("secure_url").toString();
        }catch (Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
}
