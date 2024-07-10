package team3.boogie.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column
    private String name;

    @NonNull
    @Column(unique = true)
    private String loginId;

    @NonNull
    @Column
    private String password;

    public User(@NonNull String name, @NonNull String loginId, @NonNull String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
    }

    public User() {

    }
}
