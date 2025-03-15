package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.service.dashbard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "https://krorya-dashbaord-git-newdeploy-dear0001s-projects.vercel.app/"})
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/counts")
    public Map<String, Long> getCounts() {
        return dashboardService.getCounts();
    }
}