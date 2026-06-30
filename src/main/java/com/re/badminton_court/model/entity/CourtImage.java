package com.re.badminton_court.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data @Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "court_images")
public class CourtImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", length = 255, nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_id")
    private BadmintonCourt badmintonCourt;

    @Column(name = "is_main")
    private Boolean isMain = false;
}
