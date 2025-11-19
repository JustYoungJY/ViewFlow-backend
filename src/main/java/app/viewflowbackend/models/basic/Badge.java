package app.viewflowbackend.models.basic;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "badge")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Badge {
    @Id
    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;
}
