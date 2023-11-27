package org.binar.pragosacademyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "course_code", referencedColumnName = "code")
    private Course course;
    @Column(name = "chapter")
    private String capther;
    @OneToMany(mappedBy = "chapter")
    private List<DetailChapter> detailChapters;
}
