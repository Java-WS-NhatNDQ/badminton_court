package com.re.badminton_court.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Data @Builder @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "time_slots")
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String label;
    private Boolean isActive;
    private Double basePrice;
}
