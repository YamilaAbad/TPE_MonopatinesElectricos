package com.monopatin.monopatinservice.Controller;

import com.monopatin.monopatinservice.Model.Monopatin;
import com.monopatin.monopatinservice.Repository.MonopatinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monopatin")
public class MonopatinController {
    @Autowired
    private MonopatinRepository monopatinRepository;
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Monopatin> getAllMonopatines(){
        return monopatinRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void crearMonopatin(@RequestBody Monopatin monopatin){
        monopatinRepository.save(monopatin);
    }
}
