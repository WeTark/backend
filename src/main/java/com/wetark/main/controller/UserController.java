package com.wetark.main.controller;

import com.wetark.main.model.trade.TradeService;
import com.wetark.main.model.user.User;
import com.wetark.main.model.user.balance.Balance;
import com.wetark.main.payload.response.userPortfolio.UserPortfolio;
import com.wetark.main.payload.response.userPortfolio.UserPortfolioResponse;
import com.wetark.main.security.services.UserDetailsImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/user")
public class UserController {
    private final TradeService tradeService;

    public UserController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @GetMapping("/portfolio")
    @PreAuthorize("hasRole('USER')")
    public List<UserPortfolioResponse> getUserPortfolio(){
        User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return tradeService.userPortfolio(user);
    }

    @GetMapping("/balance")
    @PreAuthorize("hasRole('USER')")
    public Balance getUserBalance(){
        User user = ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        return user.getBalance();
    }
}
