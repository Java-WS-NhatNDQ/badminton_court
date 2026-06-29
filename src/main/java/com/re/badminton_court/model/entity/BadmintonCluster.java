package com.re.badminton_court.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "badminton_clusters")
public class BadmintonCluster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(name = "hot_line", length = 20)
    private String hotLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;
}
