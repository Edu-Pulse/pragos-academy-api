package org.binar.pragosacademyapi.service;

import org.binar.pragosacademyapi.entity.dto.AdministrationDto;
import org.binar.pragosacademyapi.entity.response.Response;

public interface AdminService {
    Response<AdministrationDto> getDataAdministration();
}
