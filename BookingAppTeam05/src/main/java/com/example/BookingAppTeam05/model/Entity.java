/***********************************************************************
 * Module:  Entity.java
 * Author:  cr007
 * Purpose: Defines the Class Entity
 ***********************************************************************/

package com.example.BookingAppTeam05.model;

import java.util.*;

public abstract class Entity {
   private int id;
   private String promoDescription;
   private ArrayList<String> pictures;
   private String address;
   private String name;
   private ArrayList<DateTime> unavailableDates;
   private float entityCancelationRate;
   
   public EntityType entityType;
   public List<Pricelist> pricelists;
   public Place place;
   public List<RuleOfConduct> rulesOfConduct;
   public List<Client> subscribedClients;
   
   public ArrayList<DateTime> getUnavailableDates() {
      return unavailableDates;
   }
   
   /** @param newUnavailableDates */
   public void setUnavailableDates(ArrayList<DateTime> newUnavailableDates) {
      unavailableDates = newUnavailableDates;
   }
   
   public int getId() {
      return id;
   }
   
   /** @param newId */
   public void setId(int newId) {
      id = newId;
   }
   
   public String getName() {
      return name;
   }
   
   /** @param newName */
   public void setName(String newName) {
      name = newName;
   }
   
   public String getAddress() {
      return address;
   }
   
   /** @param newAddress */
   public void setAddress(String newAddress) {
      address = newAddress;
   }
   
   public String getPromoDescription() {
      return promoDescription;
   }
   
   /** @param newPromoDescription */
   public void setPromoDescription(String newPromoDescription) {
      promoDescription = newPromoDescription;
   }
   
   public ArrayList<String> getPictures() {
      return pictures;
   }
   
   /** @param newPictures */
   public void setPictures(ArrayList<String> newPictures) {
      pictures = newPictures;
   }
   
   public float getEntityCancelationRate() {
      return entityCancelationRate;
   }
   
   /** @param newEntityCancelationRate */
   public void setEntityCancelationRate(float newEntityCancelationRate) {
      entityCancelationRate = newEntityCancelationRate;
   }
   
   
   /** @pdGenerated default parent getter */
   public EntityType getEntityType() {
      return entityType;
   }
   
   /** @pdGenerated default parent setter
     * @param newEntityType */
   public void setEntityType(EntityType newEntityType) {
      this.entityType = newEntityType;
   }
   /** @pdGenerated default getter */
   public List<Pricelist> getPricelists() {
      if (pricelists == null)
         pricelists = new ArrayList<Pricelist>();
      return pricelists;
   }
   
   /** @pdGenerated default iterator getter */
   public Iterator getIteratorPricelists() {
      if (pricelists == null)
         pricelists = new ArrayList<Pricelist>();
      return pricelists.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newPricelists */
   public void setPricelists(List<Pricelist> newPricelists) {
      removeAllPricelists();
      for (Iterator iter = newPricelists.iterator(); iter.hasNext();)
         addPricelists((Pricelist)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newPricelist */
   public void addPricelists(Pricelist newPricelist) {
      if (newPricelist == null)
         return;
      if (this.pricelists == null)
         this.pricelists = new ArrayList<Pricelist>();
      if (!this.pricelists.contains(newPricelist))
         this.pricelists.add(newPricelist);
   }
   
   /** @pdGenerated default remove
     * @param oldPricelist */
   public void removePricelists(Pricelist oldPricelist) {
      if (oldPricelist == null)
         return;
      if (this.pricelists != null)
         if (this.pricelists.contains(oldPricelist))
            this.pricelists.remove(oldPricelist);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllPricelists() {
      if (pricelists != null)
         pricelists.clear();
   }
   /** @pdGenerated default parent getter */
   public Place getPlace() {
      return place;
   }
   
   /** @pdGenerated default parent setter
     * @param newPlace */
   public void setPlace(Place newPlace) {
      this.place = newPlace;
   }
   /** @pdGenerated default getter */
   public List<RuleOfConduct> getRulesOfConduct() {
      if (rulesOfConduct == null)
         rulesOfConduct = new ArrayList<RuleOfConduct>();
      return rulesOfConduct;
   }
   
   /** @pdGenerated default iterator getter */
   public Iterator getIteratorRulesOfConduct() {
      if (rulesOfConduct == null)
         rulesOfConduct = new ArrayList<RuleOfConduct>();
      return rulesOfConduct.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newRulesOfConduct */
   public void setRulesOfConduct(List<RuleOfConduct> newRulesOfConduct) {
      removeAllRulesOfConduct();
      for (Iterator iter = newRulesOfConduct.iterator(); iter.hasNext();)
         addRulesOfConduct((RuleOfConduct)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newRuleOfConduct */
   public void addRulesOfConduct(RuleOfConduct newRuleOfConduct) {
      if (newRuleOfConduct == null)
         return;
      if (this.rulesOfConduct == null)
         this.rulesOfConduct = new ArrayList<RuleOfConduct>();
      if (!this.rulesOfConduct.contains(newRuleOfConduct))
         this.rulesOfConduct.add(newRuleOfConduct);
   }
   
   /** @pdGenerated default remove
     * @param oldRuleOfConduct */
   public void removeRulesOfConduct(RuleOfConduct oldRuleOfConduct) {
      if (oldRuleOfConduct == null)
         return;
      if (this.rulesOfConduct != null)
         if (this.rulesOfConduct.contains(oldRuleOfConduct))
            this.rulesOfConduct.remove(oldRuleOfConduct);
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllRulesOfConduct() {
      if (rulesOfConduct != null)
         rulesOfConduct.clear();
   }
   /** @pdGenerated default getter */
   public List<Client> getSubscribedClients() {
      if (subscribedClients == null)
         subscribedClients = new ArrayList<Client>();
      return subscribedClients;
   }
   
   /** @pdGenerated default iterator getter */
   public Iterator getIteratorSubscribedClients() {
      if (subscribedClients == null)
         subscribedClients = new ArrayList<Client>();
      return subscribedClients.iterator();
   }
   
   /** @pdGenerated default setter
     * @param newSubscribedClients */
   public void setSubscribedClients(List<Client> newSubscribedClients) {
      removeAllSubscribedClients();
      for (Iterator iter = newSubscribedClients.iterator(); iter.hasNext();)
         addSubscribedClients((Client)iter.next());
   }
   
   /** @pdGenerated default add
     * @param newClient */
   public void addSubscribedClients(Client newClient) {
      if (newClient == null)
         return;
      if (this.subscribedClients == null)
         this.subscribedClients = new ArrayList<Client>();
      if (!this.subscribedClients.contains(newClient))
      {
         this.subscribedClients.add(newClient);
         newClient.addWatchedEntities(this);      
      }
   }
   
   /** @pdGenerated default remove
     * @param oldClient */
   public void removeSubscribedClients(Client oldClient) {
      if (oldClient == null)
         return;
      if (this.subscribedClients != null)
         if (this.subscribedClients.contains(oldClient))
         {
            this.subscribedClients.remove(oldClient);
            oldClient.removeWatchedEntities(this);
         }
   }
   
   /** @pdGenerated default removeAll */
   public void removeAllSubscribedClients() {
      if (subscribedClients != null)
      {
         Client oldClient;
         for (Iterator iter = getIteratorSubscribedClients(); iter.hasNext();)
         {
            oldClient = (Client)iter.next();
            iter.remove();
            oldClient.removeWatchedEntities(this);
         }
      }
   }

}