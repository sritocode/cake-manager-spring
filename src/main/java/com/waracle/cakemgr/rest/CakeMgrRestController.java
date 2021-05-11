package com.waracle.cakemgr.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cakemgr.exceptions.CakeMgrErrorResponse;
import com.waracle.cakemgr.exceptions.CakeMgrException;
import com.waracle.cakemgr.data.CakeEntity;
import com.waracle.cakemgr.data.CakeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/")
public class CakeMgrRestController {

    private static final String INPUT_JSON = "src/main/resources/cakes-initialLoad.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";
    private static final Logger LOGGER = LoggerFactory.getLogger(CakeMgrRestController.class);


    @Autowired
    private CakeRepository cakeRepository;


    @PostConstruct
    public void loadData() {
        List<CakeEntity> cakeEntities = loadInitialCakes();
        cakeRepository.saveAll(cakeEntities);
        LOGGER.debug("Loaded initial data");
    }

    @GetMapping("/cakes")
    public Iterable<CakeEntity> getAllCakes(@RequestHeader("Content-Type") String contentType) {
        if ((contentType).equals(APPLICATION_JSON_UTF8_VALUE)) {
            return cakeRepository.findAll();
        } else {
            throw new CakeMgrException("Invalid Content-Type -- " + contentType);
        }
    }

    @GetMapping("/cakes/{cakeTitle}")
    public ResponseEntity<Iterable<CakeEntity>> getCakeByIndex(@PathVariable String cakeTitle) {
        LOGGER.debug("Finding cake with title {}", cakeTitle);

        if (cakeTitle == null || cakeTitle.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        List<CakeEntity> byTitle = cakeRepository.findByTitle(cakeTitle);
        return (byTitle.isEmpty()) ? ResponseEntity.notFound().build() : ResponseEntity.ok(byTitle);
    }

    @PostMapping("/cakes")
    public ResponseEntity<CakeEntity> addCake(@RequestBody() CakeEntity newCake) {
        CakeEntity savedCake = cakeRepository.save(newCake);
        LOGGER.info("Saved new cake with title {}", newCake.getTitle());
        return ResponseEntity.ok(savedCake);
    }


    public List<CakeEntity> loadInitialCakes() {
        List<CakeEntity> cakes;
        try (InputStream inputStream = new FileInputStream(INPUT_JSON)) {
            cakes = MAPPER.readValue(inputStream, new TypeReference<List<CakeEntity>>() {
            });
            return cakes;
        } catch (IOException e) {
            throw new CakeMgrException("Encountered an exception while loading cakes into database", e);
        }
    }

}
