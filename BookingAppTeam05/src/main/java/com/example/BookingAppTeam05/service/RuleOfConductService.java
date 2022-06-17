package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewRuleOfConductDTO;
import com.example.BookingAppTeam05.model.RuleOfConduct;
import com.example.BookingAppTeam05.repository.RuleOfConductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RuleOfConductService {

    private RuleOfConductRepository ruleOfConductRepository;

    @Autowired
    public RuleOfConductService(RuleOfConductRepository ruleOfConductRepository){
        this.ruleOfConductRepository = ruleOfConductRepository;
    }

    public RuleOfConductService(){}

    public RuleOfConduct getRuleOfConductById(Long id){return this.ruleOfConductRepository.getById(id);}

    @Transactional(readOnly = false)
    public void deleteRuleById(Long id){ this.ruleOfConductRepository.deleteById(id);}

    @Transactional(readOnly = false)
    public void updateAllowedRuleById(Long id, boolean allowed) { this.ruleOfConductRepository.updateAllowedById(id, allowed);}

    public Set<RuleOfConduct> createRulesFromDTOArray(List<NewRuleOfConductDTO> rulesOfConduct) {
        Set<RuleOfConduct> retVal = new HashSet<>();
        for (NewRuleOfConductDTO r : rulesOfConduct) {
            retVal.add(new RuleOfConduct(r.getRuleName(), r.getAllowed()));
        }
        return retVal;
    }


}
