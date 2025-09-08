package net.oneblog.article.entity;

import jakarta.persistence.*;
import lombok.*;
import net.oneblog.user.entity.UserEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Article.
 */
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_article")
@Entity
public class ArticleEntity {

    @Id
    @SequenceGenerator(name = "article_seq", sequenceName = "article_sequence", initialValue = 10,
        allocationSize = 10)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "article_seq")
    @Column(name = "article_id", nullable = false, updatable = false, unique = true)
    private Long articleId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "preview_body", nullable = false)
    private String previewBody;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "body", nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "t_article_label",
        joinColumns = @JoinColumn(name = "article_id",
            foreignKey = @ForeignKey(name = "fk_article_label"),
            referencedColumnName = "article_id"),
        inverseJoinColumns = @JoinColumn(name = "label_id",
            foreignKey = @ForeignKey(name = "fk_label_article"),
            referencedColumnName = "label_id"))
    private List<LabelEntity> labelEntities;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_article_user"),
        nullable = false)
    private UserEntity userEntity;
}
