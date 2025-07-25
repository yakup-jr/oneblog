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
@Table(name = "T_LABEL")
@Entity
public class LabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LABEL_ID", nullable = false, updatable = false, unique = true)
    private Long labelId;

    @Column(name = "NAME", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private LabelName name;

    @ManyToMany(mappedBy = "labelEntities", fetch = FetchType.LAZY)
    private List<ArticleEntity> articleEntities;
}
