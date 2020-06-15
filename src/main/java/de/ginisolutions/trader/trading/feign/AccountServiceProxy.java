package de.ginisolutions.trader.trading.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "AccountService") // TODO check if that the right id
public interface AccountServiceProxy {

//    @RequestMapping
//    public void Account
}
