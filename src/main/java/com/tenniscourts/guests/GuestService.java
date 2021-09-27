package com.tenniscourts.guests;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GuestService {
	
	private final GuestRepository guestRepository = null;
	private final GuestMapper guestMapper = null;

}
