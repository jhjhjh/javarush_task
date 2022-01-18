package com.game.repository;

import com.game.entity.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;

public interface PlayerCustomizedRepository {

    List<Player> getPlayerWithCustomQuery(Map<String,String> param);
    Integer getPlayerWithCustomQueryCount(Map<String,String> param);

}
