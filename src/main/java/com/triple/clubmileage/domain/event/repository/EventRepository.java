package com.triple.clubmileage.domain.event.repository;

import com.triple.clubmileage.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {

}
