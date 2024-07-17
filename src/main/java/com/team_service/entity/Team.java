package com.team_service.entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String captain;
    private String coach;
    private String cricketBoard;
    private Integer firstInternationalMatch;
    private Integer iccTitles;
    @Lob
    private String history;
}
