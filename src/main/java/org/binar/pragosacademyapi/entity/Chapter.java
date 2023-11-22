package org.binar.pragosacademyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    @JoinColumn(name = "cours_code", referencedColumnName = "code")
    private String courseCode;
    private String name;
    private String video;
    @Column(name = "material")
    private String materi;
    @Column(name = "chapter")
    private String capther;

}
