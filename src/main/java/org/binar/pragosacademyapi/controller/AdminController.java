package org.binar.pragosacademyapi.controller;

import org.binar.pragosacademyapi.entity.dto.AdministrationDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.service.AdminService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin")
@RestController
public class AdminController {

    private final AdminService adminService;
    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(
            value = "/administration-data",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<AdministrationDto>> getTotalUser(){
        return ResponseEntity.ok(adminService.getDataAdministration());
    }
}
