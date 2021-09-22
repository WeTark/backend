package com.wetark.main.model.matchedTrade;

import com.wetark.main.model.BaseRepository;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.user.User;
import com.wetark.main.payload.response.MatchedTradeSumResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MatchedTradeRepository extends BaseRepository<MatchedTrade> {

    @Query("Select mTrd from MatchedTrade mTrd JOIN mTrd.yesTrade yesMTrd JOIN mTrd.noTrade noMTrd where (yesMTrd.user = :user and yesMTrd.event = :event) or (noMTrd.user = :user and noMTrd.event = :event) order by mTrd.createdAt desc")
    List<MatchedTrade> findAllTradeByUserAndByEventOrderByCreatedAtDesc(@Param("user") User user, @Param("event") Event event);

    List<MatchedTrade> findAllByEventOrderByCreatedAtDesc(Event event, Pageable pageable);

    @Query("Select mTrd from MatchedTrade mTrd where mTrd.event = :event and mTrd.createdAt >= :date order by mTrd.createdAt desc")
    List<MatchedTrade> findAllByEventWithCreatedAtBefore(@Param("event") Event event, @Param("date") Date date);

    @Query("SELECT SUM(mTrd.yesTrade.price*mTrd.size) as yesSum, SUM(mTrd.noTrade.price*mTrd.size) as noSum FROM MatchedTrade mTrd  where mTrd.event = :event")
    MatchedTradeSumResponse findSumOfAllTradeByEvent(@Param("event") Event event);
}
