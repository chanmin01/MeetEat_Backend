package com.zb.meeteat.domain.matching.repository;

import com.zb.meeteat.domain.matching.entity.MatchingHistory;
import com.zb.meeteat.domain.matching.entity.MatchingStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchingHistoryRepository extends JpaRepository<MatchingHistory, Long> {

  boolean existsByUserIdAndStatus(Long userId, MatchingStatus status);

  Page<MatchingHistory> findAllByUserId(Long userId, Pageable pageable);

  List<MatchingHistory> findAllByMatchingId(long id);

  MatchingHistory findByMatchingIdAndUserId(long matching_id, Long userId);

  MatchingHistory findByUserIdAndMatchingStatusAndCreatedAtAfter(Long userId,
      com.zb.meeteat.type.MatchingStatus matchingStatus, LocalDateTime localDateTime);

  List<MatchingHistory> findAllByUserIdAndCreatedAtIsAfter(Long id, LocalDateTime localDateTime);
}
