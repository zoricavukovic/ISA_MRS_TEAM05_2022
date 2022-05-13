package com.example.BookingAppTeam05.controller;

import com.example.BookingAppTeam05.service.entities.BookingEntityService;
import com.example.BookingAppTeam05.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/pictures")
public class PictureController {

    private PictureService pictureService;
    private BookingEntityService bookingEntityService;

    @Autowired
    public PictureController(PictureService pictureService, BookingEntityService bookingEntityService) {
        this.pictureService = pictureService;
        this.bookingEntityService = bookingEntityService;
    }

    @GetMapping(value="/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getImageByName(@PathVariable String name) {
        byte[] pictureData = pictureService.getPictureDataByName(name);
        if (pictureData == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(pictureData, HttpStatus.OK);
    }

    @GetMapping(value="/entity/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getBase64ImagesForEntityId(@PathVariable Long id) {
        List<String> pictureNames = pictureService.findAllPictureNamesForEntityId(id);
        List<String> retVal = new ArrayList<>();
        for (String s : pictureNames) {
            String converted = pictureService.convertPictureToBase64ByName(s);
            if (converted != null)
                retVal.add(s + ',' + converted);
        }
        return new ResponseEntity<>(retVal, HttpStatus.OK);
    }

}
