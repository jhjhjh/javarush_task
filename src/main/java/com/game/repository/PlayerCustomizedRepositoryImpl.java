package com.game.repository;

import com.game.controller.PlayerOrder;
import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("jpaPlayerService")
@Repository
@Transactional
public class PlayerCustomizedRepositoryImpl implements PlayerCustomizedRepository {

    @PersistenceContext
    private EntityManager entityManager;

    private List<Predicate> prepareCriteria(Map <String,String> param, Root<Player> playerRoot, CriteriaBuilder cb){

        List<Predicate> predicateList = new LinkedList<>();
        if (param.getOrDefault("name", null) != null) {
            Predicate namePredicate = cb.like(playerRoot.get("name"), "%" + param.get("name") + "%");
            predicateList.add(namePredicate);
        }

        if (param.getOrDefault("title", null) != null) {
            Predicate titlePredicate = cb.like(playerRoot.get("title"), "%" + param.get("title") + "%");
            predicateList.add(titlePredicate);
        }

        if (param.getOrDefault("race", null) != null) {
            Predicate racePredicate = cb.equal(playerRoot.get("race"), Race.valueOf(param.get("race")));
            predicateList.add(racePredicate);
        }

        if (param.getOrDefault("profession", null) != null) {
            Predicate professionPredicate = cb.equal(playerRoot.get("profession"), Profession.valueOf(param.get("profession")));
            predicateList.add(professionPredicate);
        }

        String afterParam = param.getOrDefault("after", null);
        String beforeParam = param.getOrDefault("before", null);
        if (afterParam != null && beforeParam == null) {
            Predicate afterPredicate = cb.greaterThanOrEqualTo(playerRoot.get("birthday").as(Date.class), new Date(Long.valueOf(param.get("after"))));
            predicateList.add(afterPredicate);
        }else if (afterParam == null && beforeParam != null) {
            Predicate beforePredicate = cb.lessThanOrEqualTo(playerRoot.get("birthday").as(Date.class), new Date(Long.valueOf(param.get("before"))));
            predicateList.add(beforePredicate);
        }else if  (afterParam != null && beforeParam != null) {
            Predicate betweenPredicate = cb.between(playerRoot.get("birthday").as(Date.class), new Date(Long.valueOf(afterParam)), new Date (Long.valueOf(beforeParam))  );
            predicateList.add(betweenPredicate);
        }

        String minLevel = param.getOrDefault("minLevel", null);
        String maxLevel = param.getOrDefault("maxLevel", null);
        if (minLevel != null && maxLevel == null) {
            Predicate minLevelPredicate = cb.greaterThanOrEqualTo(playerRoot.get("level"), Integer.valueOf(minLevel));
            predicateList.add(minLevelPredicate);
        }else if (minLevel == null && maxLevel != null) {
            Predicate maxLevelPredicate = cb.lessThanOrEqualTo(playerRoot.get("level"), Integer.valueOf(maxLevel));
            predicateList.add(maxLevelPredicate);
        }else if  (minLevel != null && maxLevel != null) {
            Predicate betweenLevelPredicate = cb.between(playerRoot.get("level"), Integer.valueOf(minLevel), Integer.valueOf(maxLevel) );
            predicateList.add(betweenLevelPredicate);
        }

        String minExperience = param.getOrDefault("minExperience", null);
        String maxExperience = param.getOrDefault("maxExperience", null);
        if (minExperience != null && maxExperience == null) {
            Predicate minExperiencePredicate = cb.greaterThanOrEqualTo(playerRoot.get("experience"), Integer.valueOf(minExperience));
            predicateList.add(minExperiencePredicate);
        }else if (minExperience == null && maxExperience != null) {
            Predicate maxExperiencePredicate = cb.lessThanOrEqualTo(playerRoot.get("experience"), Integer.valueOf(maxExperience));
            predicateList.add(maxExperiencePredicate);
        }else if  (minExperience != null && maxExperience != null) {
            Predicate betweenExperiencePredicate = cb.between(playerRoot.get("experience"), Integer.valueOf(minExperience), Integer.valueOf(maxExperience) );
            predicateList.add(betweenExperiencePredicate);
        }

        if (param.getOrDefault("banned", null) != null) {
            Predicate bannedPredicate = cb.equal(playerRoot.get("banned"), Boolean.valueOf(param.get("banned")));
            predicateList.add(bannedPredicate);
        }
        return predicateList;
    }

    @Override
    public List<Player> getPlayerWithCustomQuery(Map<String, String> param) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Player> criteriaQuery = cb.createQuery(Player.class);
        Root<Player> playerRoot = criteriaQuery.from(Player.class);
        List<Predicate> predicateList =  prepareCriteria(param, playerRoot,cb);

        String order = param.getOrDefault("order", "ID");
        PlayerOrder playerOrder = PlayerOrder.valueOf(order);

        criteriaQuery.where(predicateList.toArray(new Predicate[0]));
        criteriaQuery.orderBy(cb.asc(playerRoot.get(playerOrder.getFieldName())));

        Query query = entityManager.createQuery(criteriaQuery);

        Integer pageSize = Integer.valueOf(param.getOrDefault("pageSize","3"));
        Integer pageOffset = Integer.valueOf(param.getOrDefault("pageNumber","0"));

        query.setMaxResults(pageSize);
        query.setFirstResult(pageOffset*pageSize);
        List list =  query.getResultList();
        return list;
    }

    @Override
    public Integer getPlayerWithCustomQueryCount(Map<String,String> param){

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Player> playerRoot = criteriaQuery.from(Player.class);
        List<Predicate> predicateList = prepareCriteria(param, playerRoot, cb);

        criteriaQuery.select(cb.count(playerRoot));
        criteriaQuery.where(predicateList.toArray(new Predicate[0]));

        Query query = entityManager.createQuery(criteriaQuery);
        return Math.toIntExact((Long )query.getSingleResult());
    }

}
