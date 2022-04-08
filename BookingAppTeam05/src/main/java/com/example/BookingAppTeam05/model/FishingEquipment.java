package com.example.BookingAppTeam05.model;

import javax.persistence.*;

@Entity
@Table(name="fishingEquipments")
public class FishingEquipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="equipmentName", nullable = false, unique = true)
    private String equipmentName;

    public FishingEquipment() {}

    public FishingEquipment(String name) {
        this.equipmentName = name;
    }

    public Long getId() {
        return id;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String name) {
        this.equipmentName = name;
    }
}
