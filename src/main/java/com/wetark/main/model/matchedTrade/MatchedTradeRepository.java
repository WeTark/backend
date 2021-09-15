package com.wetark.main.model.matchedTrade;

import com.wetark.main.model.BaseRepository;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.trade.TradeType;
import com.wetark.main.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchedTradeRepository extends BaseRepository<MatchedTrade> {

    @Query("Select mTrd from MatchedTrade mTrd JOIN mTrd.yesTrade yesMTrd JOIN mTrd.noTrade noMTrd where (yesMTrd.user = :user and yesMTrd.event = :event) or (noMTrd.user = :user and noMTrd.event = :event) order by mTrd.createdAt desc")
    List<MatchedTrade> findAllTradeByUserAndByEventOrderByCreatedAtDesc(@Param("user") User user, @Param("event") Event event);

    List<MatchedTrade> findAllByEventOrderByCreatedAtDesc(Event event, Pageable pageable);
}
