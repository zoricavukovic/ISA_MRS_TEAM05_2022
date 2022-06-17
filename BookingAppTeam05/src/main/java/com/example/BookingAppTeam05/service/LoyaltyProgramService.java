package com.example.BookingAppTeam05.service;

import com.example.BookingAppTeam05.dto.LoyaltyProgramDTO;
import com.example.BookingAppTeam05.exception.ItemNotFoundException;
import com.example.BookingAppTeam05.exception.database.DatabaseException;
import com.example.BookingAppTeam05.model.LoyaltyProgram;
import com.example.BookingAppTeam05.model.LoyaltyProgramEnum;
import com.example.BookingAppTeam05.repository.LoyaltyProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class LoyaltyProgramService {
    private LoyaltyProgramRepository loyaltyProgramRepository;

    @Autowired
    public LoyaltyProgramService(LoyaltyProgramRepository loyaltyProgramRepository) {
        this.loyaltyProgramRepository = loyaltyProgramRepository;
    }

    public LoyaltyProgramService() {}

    public LoyaltyProgram getCurrentLoyaltyProgram() {
        List<LoyaltyProgram> all = loyaltyProgramRepository.getAllSortedByTime();
        if (all == null || all.size() == 0)
            return null;
        return all.get(0);
    }

    public LoyaltyProgramDTO getCurrentLoyaltyProgramDTO() {
        LoyaltyProgram loyaltyProgram = getCurrentLoyaltyProgram();
        if (loyaltyProgram == null)
            throw new ItemNotFoundException("Can't find loyalty program in system.");
        return new LoyaltyProgramDTO(loyaltyProgram);
    }

    @Transactional
    public LoyaltyProgramDTO createNewLoyaltyProgram(LoyaltyProgramDTO loyaltyProgramDTO) {
        try{
            LoyaltyProgram loyaltyProgram = new LoyaltyProgram(loyaltyProgramDTO);
            LoyaltyProgramDTO retVal = new LoyaltyProgramDTO(loyaltyProgram);
            loyaltyProgramRepository.save(loyaltyProgram);
            return retVal;
        } catch (Exception ex){
            throw new DatabaseException("Can't create new loyalty program at the moment. Try again!");
        }
    }

    public LoyaltyProgramEnum getLoyaltyProgramTypeFromUserPoints(int loyaltyPoints) {
        LoyaltyProgram current = this.getCurrentLoyaltyProgram();
        if (loyaltyPoints < current.getBronzeLimit())
            return LoyaltyProgramEnum.REGULAR;
        if (loyaltyPoints < current.getSilverLimit())
            return LoyaltyProgramEnum.BRONZE;
        if (loyaltyPoints < current.getGoldLimit())
            return LoyaltyProgramEnum.SILVER;
        return LoyaltyProgramEnum.GOLD;
    }
}
