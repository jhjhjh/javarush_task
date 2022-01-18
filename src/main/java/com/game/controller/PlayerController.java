package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.repository.PlayerRepository;
import com.game.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.jaxb.SpringDataJaxb;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PlayerController {
    //https://spring.io/guides/tutorials/rest/
    @Autowired
    private PlayerService playerService;
    @Autowired
    private PlayerRepository playerRepository;

    @RequestMapping(value="/rest/players", method = RequestMethod.GET)
    public List<Player> mainTable(@RequestParam Map<String,String> param)
            /*(@RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "title", required = false) String title,
                                  @RequestParam(value = "race", required = false) Race race,
                                  @RequestParam(value = "profession", required = false) Profession profession,
                                  @RequestParam(value = "after", required = false) Long after,
                                  @RequestParam(value = "before", required = false) Long before,
                                  @RequestParam(value = "banned", required = false) Boolean banned,
                                  @RequestParam(value = "minExperience", required = false) Integer minExperience,
                                  @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                                  @RequestParam(value = "minLevel", required = false) Integer minLevel,
                                  @RequestParam(value = "maxLevel", required = false) Integer maxLevel,
                                  @RequestParam(value = "order", required = false, defaultValue = "ID") PlayerOrder order,
                                  @RequestParam(value = "pageNumber", required = false, defaultValue = "0") Integer pageNumber,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "3") Integer pageSize)*/

    {
        return playerRepository.getPlayerWithCustomQuery(param);
        //return playerService.getAllPlayers(param);
    }

    @RequestMapping("/rest/players/count")
    public Integer getCount(@RequestParam Map<String,String> param)
            /*(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "title", required = false) String title,
                            @RequestParam(value = "race", required = false) Race race,
                            @RequestParam(value = "profession", required = false) Profession profession,
                            @RequestParam(value = "after", required = false) Long after,
                            @RequestParam(value = "before", required = false) Long before,
                            @RequestParam(value = "banned", required = false) Boolean banned,
                            @RequestParam(value = "minExperience", required = false) Integer minExperience,
                            @RequestParam(value = "maxExperience", required = false) Integer maxExperience,
                            @RequestParam(value = "minLevel", required = false) Integer minLevel,
                            @RequestParam(value = "maxLevel", required = false) Integer maxLevel)*/
    {
        playerRepository.getPlayerWithCustomQuery(param);
        return playerRepository.getPlayerWithCustomQueryCount(param);
        //return  playerService.getPayersCount(param);
    }

    @RequestMapping("/rest/players/{id}")
    public Player findPlayerById(@PathVariable String id){
        Long idLong = null;
        try {
            idLong = Long.valueOf(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (idLong < 1 )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Optional<Player> player = playerRepository.findById(idLong);
        if (player.isPresent()) {
            return player.get();
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }


    @RequestMapping(value ="/rest/players", method = RequestMethod.POST)
    public Player createPlayer( @RequestBody Player newPlayer) {
        if (!newPlayer.isCorrect()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }else {
            try {
                Player player = newPlayer.prepareToPersist();
                return playerRepository.save(newPlayer);
            }catch (Exception e){
                throw new  ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @RequestMapping(value ="/rest/players/{id}", method = RequestMethod.POST)
    public Player updatePlayer(@RequestBody Player playerUpdate, @PathVariable Long id){
        Long idLong = null;
        try {
            idLong = Long.valueOf(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (idLong < 1 )
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        Optional<Player> playerOptional = playerRepository.findById(idLong);
        if ( !playerOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }else{
            if (playerOptional.get().checkAndUpdate(playerUpdate)){
                return playerRepository.save(playerOptional.get());
            }else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }

    }

    @RequestMapping(value ="/rest/players/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deletePlayer(@PathVariable String id){
        Long idLong = null;
        try {
            idLong = Long.valueOf(id);
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (idLong < 1 )
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (playerRepository.existsById(idLong)){
            playerRepository.deleteById(idLong);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
