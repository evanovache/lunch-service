package com.etz.foodapp.menu;

import java.time.LocalDate;

import com.etz.foodapp.vendor.Vendor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity 
@Table(
    name = "menus", 
    uniqueConstraints = @UniqueConstraint(
        columnNames = {"vendor_id", "valid_from", "valid_to"}
    )
)
public class Menu {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @ManyToOne(optional = false) 
    @JoinColumn(name = "vendor_id") 
    private Vendor vendor; 

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom; 

    @Column(name = "valid_to", nullable = false)
    private LocalDate validTo; 

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false) 
    private MenuStatus status; 

    protected Menu() {}

    public Menu(Vendor vendor, LocalDate validFrom, LocalDate validTo) {
        this.vendor = vendor;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.status = MenuStatus.DRAFT;
    }

    public Long getId() {
        return id;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidTo() {
        return validTo;
    }

    public MenuStatus getStatus() {
        return status;
    }

    public void activate() {
        this.status = MenuStatus.ACTIVE;
    }

    public void close() {
        this.status = MenuStatus.CLOSED;
    }
}
