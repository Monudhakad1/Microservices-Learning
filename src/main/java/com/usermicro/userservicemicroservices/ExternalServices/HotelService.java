package com.usermicro.userservicemicroservices.ExternalServices;

import com.usermicro.userservicemicroservices.entity.Dto.HotelDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="HOTELDETAILS")
public interface HotelService {

    @GetMapping("/api/hotels/{hotelId}")
    HotelDto  getHotel(@PathVariable("hotelId") String hotelId);
}
