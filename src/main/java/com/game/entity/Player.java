package com.game.entity;


import org.springframework.data.jpa.repository.Temporal;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long  id;
    private  String name;
    private  String title;
    @Enumerated(EnumType.STRING)
    private Race race;
    @Enumerated(EnumType.STRING)
    private Profession profession;
    private Date birthday;
    private Boolean banned;
    private Integer experience;
    private Integer level;
    private Integer untilNextLevel;

    public Player() {

    }

    public boolean isCorrect(){
        if ( name == null || name.isEmpty() || name.length() > 12)
            return false;
        if( title == null || title.length() > 30)
            return false;
        if (race == null)
            return false;
        if (profession == null)
            return false;
        Calendar calendar = new GregorianCalendar(2000, Calendar.JANUARY , 1);
        Date beginDate = calendar.getTime();
        calendar.set(3000, Calendar.DECEMBER, 31);
        Date endDate = calendar.getTime();
        if (birthday == null || birthday.before(beginDate) || birthday.after(endDate))
            return false;
        if(experience == null || experience < 0 || experience > 10000000)
            return false;
        return true;
    }

    public boolean checkAndUpdate(Player newPlayer) {
       String name = newPlayer.getName();
        if ( name != null ){
            if( !name.isEmpty() && ! (name.length() > 12)) {
                this.name = name;
            }else{
                return false;
            }
        }

        String title = newPlayer.getTitle();
        if( title != null){
            if( ! (title.length() > 30)) {
                this.title = title;
            }else{
                return false;
            }
        }

        if ( newPlayer.getRace() != null){
            this.race = newPlayer.getRace();
        }

        if (newPlayer.getProfession() != null){
            this.profession = newPlayer.getProfession();
        }

        Calendar calendar = new GregorianCalendar(2000, Calendar.JANUARY , 1);
        Date beginDate = calendar.getTime();
        calendar.set(3000, Calendar.DECEMBER, 31);
        Date endDate = calendar.getTime();
        Date newPlayerBirthday = newPlayer.getBirthday();
        if (newPlayerBirthday != null){
            if (!newPlayerBirthday.before(beginDate) && !newPlayerBirthday.after(endDate)){
                this.birthday = newPlayerBirthday;
            }else{
                return false;
            }
        }

        Integer experience = newPlayer.getExperience();
        if (experience != null){
            if ( experience >= 0 && experience <= 10000000){
                this.experience = experience;
            }else{
                return false;
            }
        }

        if(newPlayer.getBanned() != null){
            if(this.banned != newPlayer.getBanned()){
                this.banned = newPlayer.getBanned();
            }
        }
        prepareToPersist();
        return true;
    }


    public Player prepareToPersist(){
        level = (int)(Math.sqrt(2500+200*experience)-50)/100;
        untilNextLevel = 50*(level+1)*(level+2)-experience;
        return this;
    }

    public Player( String name, String title, Race race, Profession profession, Date birthday, Boolean banned, Integer experience, Integer level, Integer untilNextLevel) {
        this.name = name;
        this.title = title;
        this.race = race;
        this.profession = profession;
        this.birthday = birthday;
        this.banned = banned;
        this.experience = experience;
        this.level = level;
        this.untilNextLevel = untilNextLevel;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", race=" + race +
                ", profession=" + profession +
                ", birthday=" + birthday +
                ", banned=" + banned +
                ", experience=" + experience +
                ", level=" + level +
                ", untilNextLevel=" + untilNextLevel +
                '}';
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Profession getProfession() {
        return profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Date getBirthday() {
        return birthday;
    }



    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(Boolean banned) {
        this.banned = banned;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }


}
