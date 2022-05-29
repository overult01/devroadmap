package dev.load.map.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import dev.load.map.domain.member.Field;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Subjects {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String title;
    private int expectedtime;
    
    @Enumerated(EnumType.STRING)
    private Field field;
    private String detailedsubjects;
}
