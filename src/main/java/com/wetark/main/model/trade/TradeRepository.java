package com.wetark.main.model.trade;

import com.wetark.main.model.BaseRepository;
import com.wetark.main.model.event.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends BaseRepository<Trade> {

    @Query("Select ord from Trade ord where ord.event = :event and ord.tradeType = :tradeType and ord.isActive = true order by ord.price asc")
    List<Trade> getAllSellOrdersSortedByPriceAndActive(@Param("tradeType") TradeType tradeType, @Param("event") Event event);

    @Query("Select ord from Trade ord where ord.event = :event and ord.tradeType = :tradeType and ord.isActive = true order by ord.price desc")
    List<Trade> getAllBuyOrdersSortedByPriceAndActive(@Param("tradeType") TradeType tradeType, @Param("event") Event event);

    List<Trade> findByIsActive(Boolean isActive);
}
