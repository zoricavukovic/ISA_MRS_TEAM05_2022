package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewImageDTO;
import com.example.BookingAppTeam05.model.Picture;
import com.example.BookingAppTeam05.model.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Base64;

@Service
public class PictureService {
    private PictureRepository pictureRepository;
    public PictureService() {}

    @Autowired
    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public byte[] getPictureDataByName(String name) {
        try {
            String path = new File("src/main/resources/images/" + name)
                    .getAbsolutePath();
            RandomAccessFile f = new RandomAccessFile(path, "r");
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
            String path = new File("src/main/resources/images/" + imageName)
                    .getAbsolutePath();
            byte[] image = Base64.getDecoder().decode(base64);
            OutputStream out = new FileOutputStream(path);
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
            tryToSaveNewPictureAndAddToOtherPictures(pictures, img);
        }
        return pictures;
    }

    public List<String> findAllPictureNamesForEntityId(Long id) {
        return this.pictureRepository.findAllPictureNamesForEntityId(id);
    }

    public String convertPictureToBase64ByName(String s) {
        byte[] pictureData = getPictureDataByName(s);
        if (pictureData == null)
            return null;
        return Base64.getEncoder().encodeToString(pictureData);
    }


    public boolean tryToSaveNewPictureAndAddToOtherPictures(Set<Picture> otherPictures, NewImageDTO newImage) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
        String newImageName = timeStamp + newImage.getImageName();
        if (tryConvertBase64ToImageAndSave(newImageName, newImage.getDataBase64())) {
            otherPictures.add(new Picture(newImageName));
            return true;
        }
        return false;
    }

    public void deletePictureByName(String picturePath) {
        String path = new File("src/main/resources/images/" + picturePath)
                .getAbsolutePath();
        try {
            pictureRepository.deleteByPicturePath(picturePath);
            File f = new File(path);
            f.delete();
        } catch (Exception ignored) { }
    }
}
