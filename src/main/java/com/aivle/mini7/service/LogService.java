package com.aivle.mini7.service;

import com.aivle.mini7.client.dto.HospitalResponse;
import com.aivle.mini7.dto.LogDto;
import com.aivle.mini7.model.Log;
import com.aivle.mini7.repository.LogRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LogService {

    private final LogRepository logRepository;

    @Transactional(readOnly = true)
    public Page<LogDto.ResponseList> getLogList(Pageable pageable) {
        Page<Log> logs = logRepository.findAll(pageable);

        return logs.map(LogDto.ResponseList::of);
    }

    /**
     * 원래 이렇게 나쁜 모듈로 구현하면 안된다.
     * 현재 프로젝트 완료를 위해 급급한 소스이다.
     * @param hospitalResponseList
     * @param request
     * @param latitude
     * @param longitude
     */
    public void saveLog(List<HospitalResponse> hospitalResponseList, String request, double latitude, double longitude, int emClass) {
        Log hospitalLog = Log.builder()
                .inputText(request)
                .inputLatitude(latitude)
                .inputLongitude(longitude)
                .emClass(emClass)
                .datetime(String.valueOf(new Date()))
                .build();
        int count = 1;
        for(HospitalResponse hospitalResponse : hospitalResponseList) {
            log.info("hospitalResponse: {}", hospitalResponse);
            switch (count) {
                case 1:
                    hospitalLog.setHospital1(hospitalResponse.getHospitalName());
                    hospitalLog.setAddr1(hospitalResponse.getAddress());
                    hospitalLog.setTel1(hospitalResponse.getPhoneNumber1());
                    break;
                case 2:
                    hospitalLog.setHospital2(hospitalResponse.getHospitalName());
                    hospitalLog.setAddr2(hospitalResponse.getAddress());
                    hospitalLog.setTel2(hospitalResponse.getPhoneNumber1());
                    break;
                case 3:
                    hospitalLog.setHospital3(hospitalResponse.getHospitalName());
                    hospitalLog.setAddr3(hospitalResponse.getAddress());
                    hospitalLog.setTel3(hospitalResponse.getPhoneNumber1());
                    break;
            }
            count++;

        }

        logRepository.save(hospitalLog);
    }

}
