package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAllPlayers(Map<String, String> param) {
        return playerRepository.getPlayerWithCustomQuery(param);
    }

    public Integer getPayersCount(Map<String, String> param) {
        return playerRepository.getPlayerWithCustomQueryCount(param);
    }

    public Player findPlayerById(Long id){
        Optional<Player> optPlayer = playerRepository.findById(id);
        Player player = null;
        if(optPlayer.isPresent()){
            player = optPlayer.get();
        }
        return player;
    }

    public boolean deleteById(Long id) {
        if (playerRepository.existsById(id)){
            playerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
