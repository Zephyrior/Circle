package it.epicode.Circle.circles;


import it.epicode.Circle.enums.CircleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CircleRepository extends JpaRepository<Circle, Long> {

    @Query("SELECT c FROM Circle c WHERE c.circleStatus = :status AND (c.requester.id = :id OR c.receiver.id = :id)")
    List<Circle> getCirclesByStatus(@Param("status") CircleStatus status, @Param("id") Long id);

    @Query("SELECT c FROM Circle c WHERE c.circleStatus = 'ACCEPTED' AND (c.requester.id = :id OR c.receiver.id = :id)")
    List<Circle> findAcceptedCirclesByUserId(@Param("id") Long id);

    @Query("SELECT c FROM Circle c WHERE c.circleStatus = 'ACCEPTED' AND c.smallCircle = true AND (c.requester.id = :id OR c.receiver.id = :id)")
    List<Circle> findSmallCircles(@Param("id") Long id);
}