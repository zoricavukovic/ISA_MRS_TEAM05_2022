package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.model.Picture;
import com.example.BookingAppTeam05.repository.PictureRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;

@Service
public class PictureService {
    @Autowired
    private PictureRepository pictureRepository;

    public PictureService() {}

    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public byte[] getPictureDataByName(String name) {
        String picturePath = "images/" + name;
        ClassPathResource imgFile = new ClassPathResource(picturePath);
        try {
            return StreamUtils.copyToByteArray(imgFile.getInputStream());
        } catch (IOException e) {
            return null;
        }
    }
}
