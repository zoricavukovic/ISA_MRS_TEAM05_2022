package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewImageDTO;
import com.example.BookingAppTeam05.model.Picture;
import com.example.BookingAppTeam05.repository.PictureRepository;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Base64;

@Service
public class PictureService {
    @Autowired
    private PictureRepository pictureRepository;

    public PictureService() {}

    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

//    public byte[] getPictureDataByName(String name) {
//        String picturePath = "images/" + name;
//        ClassPathResource imgFile = new ClassPathResource(picturePath);
//        try {
//            return StreamUtils.copyToByteArray(imgFile.getInputStream());
//        } catch (IOException e) {
//            return null;
//        }
//    }

    public byte[] getPictureDataByName(String name) {
        try {
            String fullPath = "D:\\BOOKING_APP\\images\\" + name;
            RandomAccessFile f = new RandomAccessFile(fullPath, "r");
            byte[] bytes = new byte[(int) f.length()];
            f.read(bytes);
            f.close();
            return bytes;
        } catch (Exception e) {
            return null;
        }
    }


    private boolean tryConvertBase64ToImageAndSave(String imageName, String base64) {
        try{
            String fullPath = "D:\\BOOKING_APP\\images\\" + imageName;
            System.out.println("Image full path............");
            System.out.println(fullPath);
            System.out.println("Image full path............");
            byte[] image = Base64.getDecoder().decode(base64);
            OutputStream out = new FileOutputStream(fullPath);
            out.write(image);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Set<Picture> createPicturesFromDTO(List<NewImageDTO> images) {
        Set<Picture> pictures = new HashSet<>();
        for (NewImageDTO img : images) {
            String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
            String newImageName = timeStamp + img.getImageName();
            if (tryConvertBase64ToImageAndSave(newImageName, img.getDataBase64())) {
                pictures.add(new Picture(newImageName));
            }
        }
        return pictures;
    }
}
