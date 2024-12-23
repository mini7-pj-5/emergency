package com.aivle.mini7.controller;

import com.aivle.mini7.client.api.FastApiClient;
import com.aivle.mini7.client.dto.HospitalResponse;
import com.aivle.mini7.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class IndexController {

    private final FastApiClient fastApiClient;
    private final LogService logService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/recommend_hospital")
    public ModelAndView recommend_hospital(@RequestParam("request") String request, @RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude) {


//        FastApiClient 를 호출한다.
        List<HospitalResponse> hospitalList = fastApiClient.getHospital(request, latitude, longitude);
        log.info("hospital: {}", hospitalList);

//        emclass는 AI의 api를 고치기 힘들어서 일단 하드코딩으로 마무리한다.
        if(hospitalList !=null){
            logService.saveLog(hospitalList, request, latitude, longitude,4);
        }


        ModelAndView mv = new ModelAndView();
        mv.setViewName("recommend_hospital");
        mv.addObject("hospitalList", hospitalList);

        return mv;
    }
}


