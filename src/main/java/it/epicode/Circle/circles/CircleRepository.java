package it.epicode.Circle.circles;


import it.epicode.Circle.enums.CircleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface CircleRepository extends JpaRepository<Circle, Long> {

    @Query("SELECT c FROM Circle c WHERE c.circleStatus = :status AND (c.requester.id = :id OR c.receiver.id = :id)")
    List<Circle> getCirclesByStatus(@Param("status") CircleStatus status, @Param("id") Long id);

    @Query("SELECT c FROM Circle c WHERE c.circleStatus = 'ACCEPTED' AND (c.requester.id = :id OR c.receiver.id = :id)")
    List<Circle> findAcceptedCirclesByUserId(@Param("id") Long id);

    @Query("SELECT c FROM Circle c WHERE c.circleStatus = 'ACCEPTED' AND c.smallCircle = true AND (c.requester.id = :id OR c.receiver.id = :id)")
    List<Circle> findSmallCircles(@Param("id") Long id);

    @Query("SELECT c FROM Circle c WHERE (c.requester.id = :id1 AND  c.receiver.id = :id2) OR (c.requester.id = :id2 AND  c.receiver.id = :id1)")
    Optional<Circle> findExistingCircleRequest(@Param("id1") Long id1, @Param("id2") Long id2);
}