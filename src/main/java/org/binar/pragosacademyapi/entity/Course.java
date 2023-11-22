package org.binar.pragosacademyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binar.pragosacademyapi.enumeration.Level;
import org.binar.pragosacademyapi.enumeration.Type;

import javax.persistence.*;

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
