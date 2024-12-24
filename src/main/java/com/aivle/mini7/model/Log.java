package com.aivle.mini7.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String datetime;

    @Column(name = "input_text", nullable = false)
    private String inputText;

    @Column(name = "input_latitude", nullable = false)
    private Double inputLatitude;

    @Column(name = "input_longitude", nullable = false)
    private Double inputLongitude;

    @Column(name = "em_class", nullable = false)
    private Integer emClass;

    @Column(nullable = false)
    private String hospital1 = "Unknown";

    @Column(nullable = false)
    private String addr1 = "Unknown";

    @Column(nullable = false)
    private String tel1 = "Unknown";

    @Column(nullable = false)
    private String hospital2 = "Unknown";

    @Column(nullable = false)
    private String addr2 = "Unknown";

    @Column(nullable = false)
    private String tel2 = "Unknown";

    @Column(nullable = false)
    private String hospital3 = "Unknown";

    @Column(nullable = false)
    private String addr3 = "Unknown";

    @Column(nullable = false)
    private String tel3 = "Unknown";
}