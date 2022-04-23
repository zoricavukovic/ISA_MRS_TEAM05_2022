package com.example.BookingAppTeam05.dto;
import com.example.BookingAppTeam05.model.users.Client;

public class ClientDTO extends UserDTO{
    private int penalties;

    public ClientDTO(){ }


    public ClientDTO(Client client){
        super(client);
        this.penalties = client.getPenalties();
    }

    public int getPenalties() {
        return penalties;
    }

    public void setPenalties(int penalties) {
        this.penalties = penalties;
    }

}
