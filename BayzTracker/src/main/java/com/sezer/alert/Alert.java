package com.sezer.alert;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Data @RequiredArgsConstructor
@Table(
        name="ALERT",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"currency", "target_price"})
)
public class Alert {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "currency", nullable = false)
    private String currency;
    @Column(name = "target_price", nullable = false)
    private float targetPrice;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private AlertStatus status;
    public enum AlertStatus {
        NEW, TRIGGERED, ACKED, CANCELED
    }
}

