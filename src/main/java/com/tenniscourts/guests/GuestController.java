package com.tenniscourts.guests;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/guests")
public class GuestController {
	
	private final GuestService guestService;

}
