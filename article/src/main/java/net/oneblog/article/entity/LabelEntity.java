package net.oneblog.article.entity;

import jakarta.persistence.*;
import lombok.*;
import net.oneblog.api.interfaces.LabelName;

import java.util.List;

/**
 * The type Label.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_label")
@Entity
public class LabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "label_seq")
    @SequenceGenerator(name = "label_seq", sequenceName = "t_label_seq", allocationSize = 10,
        initialValue = 10)
    @Column(name = "label_id", nullable = false, updatable = false, unique = true)
    private Long labelId;

    @Column(name = "name", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private LabelName name;

    @ManyToMany(mappedBy = "labelEntities", fetch = FetchType.LAZY)
    private List<ArticleEntity> articleEntities;
}
