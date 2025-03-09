package ktpm17ctt.g6.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "feedbacks")
public class FeedBack implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String comment;
    private Float rating;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "feedback_list_detail_images",
            joinColumns = {
                    @JoinColumn(name = "feedback_id")
            }
    )
    private List<String> listDetailImages;
    private Date createdAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
