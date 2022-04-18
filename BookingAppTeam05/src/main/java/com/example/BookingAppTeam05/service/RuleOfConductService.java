package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.NewRuleOfConductDTO;
import com.example.BookingAppTeam05.model.RuleOfConduct;
import com.example.BookingAppTeam05.repository.RuleOfConductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RuleOfConductService {
    @Autowired
    private RuleOfConductRepository ruleOfConductRepository;

    public Set<RuleOfConduct> createRulesFromDTOArray(List<NewRuleOfConductDTO> rulesOfConduct) {
        Set<RuleOfConduct> retVal = new HashSet<>();
        for (NewRuleOfConductDTO r : rulesOfConduct) {
            retVal.add(new RuleOfConduct(r.getRuleName(), r.getAllowed()));
        }
        return retVal;
    }
}
