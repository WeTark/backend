package com.wetark.main.model.trade;

import com.wetark.main.model.BaseRepository;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.user.User;
import com.wetark.main.payload.response.PendingTradeResponse;
import com.wetark.main.payload.response.userPortfolio.UserPortfolio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends BaseRepository<Trade> {

    @Query("Select ord from Trade ord where ord.event = :event and ord.tradeType = :tradeType and ord.isActive = true order by ord.price desc, ord.createdAt asc")
    List<Trade> getAllTradesSortedByPriceAndActive(@Param("tradeType") TradeType tradeType, @Param("event") Event event);


    @Query("Select trd from Trade trd where trd.event = :event and trd.tradeType = :tradeType and trd.isActive = true order by trd.price desc")
    List<PendingTradeResponse> getTopPendingTrade(@Param("tradeType") TradeType tradeType, @Param("event") Event event, Pageable pageable);

    List<Trade> findByUserAndEventAndIsActiveOrderByCreatedAtDesc(User user,Event event, Boolean isActive, Pageable pageable);

    List<Trade> findByIsActive(Boolean isActive);

    @Query("Select trd.event.id as id, trd.event.title as title, trd.tradeType as tradeType, MAX(trd.event.createdAt) as createdAt, max(trd.event.expireAt) as expireAt, SUM(trd.price*(trd.initialSize-trd.size)) as totalAmount, SUM(trd.price*trd.size) as totalPendingAmount from Trade trd group by trd.event.id, trd.tradeType, trd.event.title")
    List<UserPortfolio> findUserPortfolioByUser(@Param("user") User user);
}
