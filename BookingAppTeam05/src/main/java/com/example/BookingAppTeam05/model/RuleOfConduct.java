package com.example.BookingAppTeam05.model;

import javax.persistence.*;

@Entity
@Table(name="rulesOfConduct")
public class RuleOfConduct {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name="ruleName", nullable = false)
   private String ruleName;

   @Column(name="allowed", nullable = false)
   private Boolean allowed;

   public RuleOfConduct() {}

   public RuleOfConduct(String ruleName, Boolean allowed) {
      this.ruleName = ruleName;
      this.allowed = allowed;
   }

   public Long getId() {
      return id;
   }

   public String getRuleName() {
      return ruleName;
   }

   public Boolean isAllowed() {
      return allowed;
   }

   public void setRuleName(String ruleName) {
      this.ruleName = ruleName;
   }

   public void setAllowed(Boolean allowed) {
      this.allowed = allowed;
   }
}