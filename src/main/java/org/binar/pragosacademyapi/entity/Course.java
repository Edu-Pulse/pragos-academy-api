package org.binar.pragosacademyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binar.pragosacademyapi.enumeration.Level;
import org.binar.pragosacademyapi.enumeration.Type;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "courses")
public class Course {
    @Id
    private String code;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private Level level;
    private String category;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Integer price;
    private Integer discount;
}
