package com.wetark.main.model.matchedTrade;

import com.wetark.main.helper.PageHelper;
import com.wetark.main.model.event.Event;
import com.wetark.main.model.user.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchedTradeService {
    private final MatchedTradeRepository matchedTradeRepository;

    public MatchedTradeService(MatchedTradeRepository matchedTradeRepository) {
        this.matchedTradeRepository = matchedTradeRepository;
    }
    public List<MatchedTrade> allMatchedTradeForEvent(Event event, String page, String size){
        return matchedTradeRepository.findAllByEventOrderByCreatedAtDesc(event, PageHelper.pageable(page,size));
    }
    public List<MatchedTrade> allMatchedTradeForEventOfUser(User user, Event event, String page, String size){
        return matchedTradeRepository.findAllTradeByUserAndByEventOrderByCreatedAtDesc(user, event);
    }

}
