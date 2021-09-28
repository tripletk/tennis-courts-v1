package com.tenniscourts.guests;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tenniscourts.config.BaseRestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/guests")
public class GuestController extends BaseRestController{
	
	private final GuestService guestService;

	// CREATE
	@PostMapping
	public ResponseEntity<Guest> createGuest(@RequestBody CreateGuestRequestDTO createGuestRequestDTO) {
		return ResponseEntity.created(locationByEntity(guestService.createGuest(createGuestRequestDTO).getId())).build();
	}
	
	// READ
	@GetMapping(path = "/{guestId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GuestDTO> getGuestById(@PathVariable Long guestId) {
		return ResponseEntity.ok(guestService.findGuestById(guestId));
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<GuestDTO>> getGuestByName(@RequestParam(name = "name", required = false) String name) {
		if(name == null) {
			return ResponseEntity.ok(guestService.getAllGuests());
		} else {
			return ResponseEntity.ok(guestService.findGuestByName(name));
		}
	}
	
	// UPDATE
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GuestDTO> updateGuest(@RequestBody GuestDTO guestDTO) {
		return ResponseEntity.ok(guestService.updateGuest(guestDTO));
	}
	
	// DELETE
	@DeleteMapping
	public ResponseEntity<Void> deleteGUest(@PathVariable Long guestId) {
		guestService.deleteGuest(guestId);
		return ResponseEntity.noContent().build();
	}
}
