package com.aivle.mini7.controller;

import com.aivle.mini7.client.api.FastApiClient;
import com.aivle.mini7.client.dto.HospitalResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final FastApiClient fastApiClient;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/recommend_hospital")
    public ModelAndView recommend_hospital(@RequestParam("request") String request, @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {


//        FastApiClient 를 호출한다.
        HospitalResponse hospital = fastApiClient.getHospital(request, latitude, longitude);
        log.info("hospital: {}", hospital);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("recommend_hospital");
        mv.addObject("hospital", hospital);

        return mv;
    }
}


