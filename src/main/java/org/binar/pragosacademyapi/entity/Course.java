package org.binar.pragosacademyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binar.pragosacademyapi.enumeration.Level;
import org.binar.pragosacademyapi.enumeration.Type;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    @Column(name = "intended_for")
    private String intended;
    private String lecturer;
    @Enumerated(EnumType.STRING)
    private Level level;
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;
    @Enumerated(EnumType.STRING)
    private Type type;
    private Integer price;
    private Integer discount;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    List<Chapter> chapters;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Payment> payments;
}
