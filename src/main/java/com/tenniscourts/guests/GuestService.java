package com.tenniscourts.guests;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tenniscourts.exceptions.EntityNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GuestService {
	
	private final GuestRepository guestRepository = null;
	private final GuestMapper guestMapper = null;

	// CREATE
	public GuestDTO createGuest(CreateGuestRequestDTO createGuestRequestDTO) {
		GuestDTO guestDTO = GuestDTO.builder().name(createGuestRequestDTO.getName()).build();
		return guestMapper.map(guestRepository.save(guestMapper.map(guestDTO)));
	}
	
	// READ
	public List<GuestDTO> getAllGuests() {
		return guestMapper.map(guestRepository.findAll());
	}
	
	public GuestDTO findGuestById(Long guestId) {
		return guestRepository.findById(guestId).map(guestMapper::map).orElseThrow(() -> {
			throw new EntityNotFoundException("Guest with id " + guestId + "not found.");
		});
	}
	
	public List<GuestDTO> findGuestByName(String guestName) {
		return guestMapper.map(guestRepository.findByGuestName(guestName));
	}
	
	// UPDATE
	public GuestDTO updateGuest(GuestDTO guestDTO) {
		guestRepository.findById(guestDTO.getId()).orElseThrow(() -> {
            throw new EntityNotFoundException("Guest with id " + guestDTO.getId() + "not found.");
        });
        return guestMapper.map(guestRepository.save(guestMapper.map(guestDTO)));
	}
	
	// DELETE
	public void deleteGuest(Long guestId) {
		GuestDTO guest = guestRepository.findById(guestId).map(guestMapper::map).orElseThrow(() -> {
			throw new EntityNotFoundException("Guest with id " + guestId + "not found.");
		});
		guestRepository.delete(guestMapper.map(guest));
	}
}
