package dev.road.map.domain;

import javax.persistence.EntityManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

public interface HistoryRepository extends JpaRepository<History, Long>{

}
