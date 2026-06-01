package com.wholparts.person_service.controller;

import com.wholparts.person_service.service.IbgeImportService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class IbgeController {

    private final IbgeImportService service;

    public IbgeController(IbgeImportService service) {
        this.service = service;
    }

    @PostMapping("/import-ibge")
    public void importIbge() {
        service.importAll();
    }
}
