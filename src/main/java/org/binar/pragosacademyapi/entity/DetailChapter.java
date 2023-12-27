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
@Table(name = "detail_chapters")
public class DetailChapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "chapter_id", referencedColumnName = "id")
    private Chapter chapter;
    private String name;
    private String video;
    private String material;
    @OneToMany(mappedBy = "detailChapter", cascade = CascadeType.ALL)
    private List<UserDetailChapter> userDetailChapters;
}
