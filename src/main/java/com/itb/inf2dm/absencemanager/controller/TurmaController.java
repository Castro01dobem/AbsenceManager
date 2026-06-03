package com.itb.inf2dm.absencemanager.controller;

import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.*;
import com.itb.inf2dm.absencemanager.services.TurmaService;
import com.itb.inf2dm.absencemanager.model.entity.Turma;

@RestController
@RequestMapping("/api/turmas")
public class TurmaController {

    @Autowired
    TurmaService s;

    @GetMapping
    public List<Turma> all() {
        return s.findAll();
    }

    @PostMapping
    public Turma create(@RequestBody Turma t) {
        return s.create(t);
    }
}
