package com.invoiceapi.controllers;

import com.invoiceapi.models.response.ClientStatisticsResponse;
import com.invoiceapi.models.response.DashboardStatisticsResponse;
import com.invoiceapi.services.interfaces.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/statistics")
    public ResponseEntity<DashboardStatisticsResponse> getDashboardStatistics(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        String email = userDetails.getUsername();
        DashboardStatisticsResponse statistics = dashboardService.getDashboardStatistics(email);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }

    @GetMapping("/client-statistics")
    public ResponseEntity<ClientStatisticsResponse> getClientStatistics(
            @RequestParam Long clientId,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String email = userDetails.getUsername();
        ClientStatisticsResponse statistics = dashboardService.getClientStatistics(clientId, email);
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}
