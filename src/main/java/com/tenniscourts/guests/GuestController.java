package com.tenniscourts.guests;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/guests")
public class GuestController extends BaseRestController{
	
	private final GuestService guestService;

	// CREATE
	@ApiOperation(value = "Create a guest")
	@ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "An unexpected error occurred")
    })
	@PostMapping
	public ResponseEntity<Void> createGuest(@RequestBody CreateGuestRequestDTO createGuestRequestDTO) {
		return ResponseEntity.created(locationByEntity(guestService.createGuest(createGuestRequestDTO).getId())).build();
	}
	
	// READ
	@ApiOperation(value = "Get a guest by Id")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "An unexpected error occurred")
    })
	@GetMapping(path = "/{guestId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GuestDTO> getGuestById(@PathVariable Long guestId) {
		return ResponseEntity.ok(guestService.findGuestById(guestId));
	}
	
	@ApiOperation(value = "Get guests or a guest by name url param")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "An unexpected error occurred")
    })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<GuestDTO>> getGuestByName(@RequestParam(name = "name", required = false) String name) {
		if(name == null) {
			return ResponseEntity.ok(guestService.getAllGuests());
		} else {
			return ResponseEntity.ok(guestService.findGuestByName(name));
		}
	}
	
	// UPDATE
	@ApiOperation(value = "Update a guest")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 400, message = "An unexpected error occurred")
    })
	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GuestDTO> updateGuest(@RequestBody GuestDTO guestDTO) {
		return ResponseEntity.ok(guestService.updateGuest(guestDTO));
	}
	
	// DELETE
	@ApiOperation(value = "Delete a guest")
	@ApiResponses(value = {
            @ApiResponse(code = 200, message = "No Content"),
            @ApiResponse(code = 400, message = "An unexpected error occurred")
    })
	@DeleteMapping
	public ResponseEntity<Void> deleteGuest(@PathVariable Long guestId) {
		guestService.deleteGuest(guestId);
		return ResponseEntity.noContent().build();
	}
}
