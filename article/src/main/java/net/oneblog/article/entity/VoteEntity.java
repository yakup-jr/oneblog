package net.oneblog.article.entity;

import jakarta.persistence.*;
import lombok.*;
import net.oneblog.api.interfaces.VoteType;
import net.oneblog.user.entity.UserEntity;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_vote",
    uniqueConstraints = @UniqueConstraint(
        name = "uk_vote_user_article",
        columnNames = {"user_id", "article_id"}
    )
)
public class VoteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vote_seq")
    @SequenceGenerator(name = "vote_seq", initialValue = 10, allocationSize = 10, sequenceName =
        "t_vote_seq")
    @Column(nullable = false)
    private Long voteId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "article_id", foreignKey = @ForeignKey(name = "fk_article_vote"),
        nullable = false)
    private ArticleEntity article;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_vote"), nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "vote_type", nullable = false)
    private VoteType voteType;
}
