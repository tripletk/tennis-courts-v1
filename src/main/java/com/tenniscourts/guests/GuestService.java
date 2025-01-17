package com.tenniscourts.guests;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tenniscourts.exceptions.EntityNotFoundException;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GuestService {
	
	private final GuestRepository guestRepository;
	
	private final GuestMapper guestMapper;

	// CREATE
	public GuestDTO createGuest(CreateGuestRequestDTO createGuestRequestDTO) {
        GuestDTO guestDTO = GuestDTO.builder().name(createGuestRequestDTO.getName()).build();
        return guestMapper.map(guestRepository.save(guestMapper.map(guestDTO)));
    }
	
	// READ
	public List<GuestDTO> getAllGuests() {
		try {
			return guestMapper.map(guestRepository.findAll());
		} catch(NullPointerException e) {
			throw new EntityNotFoundException("No guests in repository!");
		}
		
	}
	
	public GuestDTO findGuestById(Long guestId) {
		return guestRepository.findById(guestId).map(guestMapper::map).orElseThrow(() -> {
			throw new EntityNotFoundException("No guests in repository!");
		});
	}
	
	public List<GuestDTO> findGuestByName(String guestName) {
		return guestMapper.map(guestRepository.findByName(guestName));
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
