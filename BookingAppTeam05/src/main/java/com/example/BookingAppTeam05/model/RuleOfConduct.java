/***********************************************************************
 * Module:  RuleOfConduct.java
 * Author:  cr007
 * Purpose: Defines the Class RuleOfConduct
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public class RuleOfConduct {
   private int id;
   private String ruleName;
   private boolean allowed;
   
   public int getId() {
      return id;
   }
   
   /** @param newId */
   public void setId(int newId) {
      id = newId;
   }
   
   public String getRuleName() {
      return ruleName;
   }
   
   /** @param newRuleName */
   public void setRuleName(String newRuleName) {
      ruleName = newRuleName;
   }
   
   public boolean getAllowed() {
      return allowed;
   }
   
   /** @param newAllowed */
   public void setAllowed(boolean newAllowed) {
      allowed = newAllowed;
   }

}