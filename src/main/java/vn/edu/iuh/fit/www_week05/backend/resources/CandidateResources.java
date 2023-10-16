package vn.edu.iuh.fit.www_week05.backend.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.iuh.fit.www_week05.backend.services.CandidateServices;


@RestController("/api/vi")
public class CandidateResources {
    @Autowired
    private CandidateServices services;
}
