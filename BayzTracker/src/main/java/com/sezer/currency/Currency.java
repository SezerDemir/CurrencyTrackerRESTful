package com.sezer.currency;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDate;

@Data @Entity @Builder @AllArgsConstructor @NoArgsConstructor
@Table(name = "currency")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "name", nullable = false, length = 50)
    private String name;
    @Column(name = "symbol", nullable = false, length = 5)
    private String symbol;
    @Column(name = "current_price")
    private float currentPrice;
    @Column(name = "created_time")
    @CreationTimestamp
    private LocalDate createdTime;

}
