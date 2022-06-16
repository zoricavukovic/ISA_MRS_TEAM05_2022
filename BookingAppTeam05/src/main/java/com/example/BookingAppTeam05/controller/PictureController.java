package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
//@CrossOrigin
@RequestMapping("/pictures")
public class PictureController {

    private PictureService pictureService;

    @Autowired
    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    public PictureController(){}

    @GetMapping(value="/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageByName(@PathVariable String name) {
        byte[] pictureData = pictureService.getPictureDataByName(name);
        return new ResponseEntity<>(pictureData, HttpStatus.OK);
    }

    @GetMapping(value="/entity/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getBase64ImagesForEntityId(@PathVariable Long id) {
        List<String> retVal = pictureService.getBase64ImagesForEntityId(id);
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }
}
