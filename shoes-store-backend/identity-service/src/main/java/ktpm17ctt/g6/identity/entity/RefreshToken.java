package ktpm17ctt.g6.identity.entity;

import jakarta.persistence.*;
import ktpm17ctt.g6.identity.entity.enums.TokenStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class RefreshToken {
    @Id
    String id;
    @ManyToOne
    Account account;
    @Column(length = 512)
    String token;
    Date expirationDate;
    @Enumerated(EnumType.STRING)
    TokenStatus status;
}
