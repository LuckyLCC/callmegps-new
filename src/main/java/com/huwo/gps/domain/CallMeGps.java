package com.huwo.gps.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @Description: TODO
 * @Author: liuchang
 * @CreateTime: 2022-12-12  19:33
 */
@Data
@Document(collection = "call_me_gps")
@Builder
public class CallMeGps {

    private Long id;
    private String timeStamp;
    private String vehicleID;
    private List<HwPositionMessage> data;

}
