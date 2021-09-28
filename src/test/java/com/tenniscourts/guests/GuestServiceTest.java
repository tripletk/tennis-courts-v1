package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;

import lombok.var;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {GuestService.class,GuestRepository.class})
public class GuestServiceTest {
	
	@InjectMocks
    private GuestService guestService;
    @Mock
    private GuestRepository guestRepository;
    @Mock
    private GuestMapper guestMapper;
    
    @Before
    public void setUp() {
        
    	when(guestRepository.findById(anyLong())).thenReturn(Optional.of(mockGuest()));
    	when(guestService.findGuestById(anyLong())).thenReturn(mockGuestDTO());
    }
    
    @Test
    public void shouldAddNewGuest() {
        when(guestService.createGuest(mockCreateGuestRequestDTO())).thenReturn(mockGuestDTO());

        GuestDTO guestDTO = guestService.createGuest(mockCreateGuestRequestDTO());

        Assert.assertTrue(guestDTO.getId().equals(mockGuestDTO().getId()));
        Assert.assertTrue(guestDTO.getName().equals(mockGuestDTO().getName()));
    }
    
    @Test
    public void shouldFindAllGuests() {
    	when(guestRepository.findAll()).thenReturn(mockGuestList());
    	List<GuestDTO> guestList = guestService.getAllGuests();
        Assert.assertTrue(guestList.size() > 0);
    }
    
    @Test
    public void shouldFindGuestById() {
    	when(guestRepository.findById(mockGuestDTO().getId())).thenReturn(Optional.of(mockGuest()));
        when(guestService.findGuestById(mockGuestDTO().getId())).thenReturn(mockGuestDTO());

        GuestDTO guestDTO = guestService.findGuestById(mockGuestDTO().getId());
        Assert.assertEquals(mockGuestDTO().getId(), guestDTO.getId());
    }
    
    @Test
    public void shouldFindGuestByName() {
    	List<Guest> mockGuest = new ArrayList<Guest>();
    	mockGuest.add(mockGuest());
    	List<GuestDTO> mockGuestDTO = new ArrayList<GuestDTO>();
    	mockGuestDTO.add(mockGuestDTO());

        when(guestRepository.findByName(mockGuest().getName())).thenReturn(mockGuest);
        when(guestService.findGuestByName(mockGuest().getName())).thenReturn(mockGuestDTO);

        Guest guest = guestMapper.map(guestService.findGuestByName(mockGuest().getName()).get(0));
        Assert.assertEquals(mockGuest().getName(), guest.getName());
    }
    
    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowEntityNotFoundExceptionWhenFindingGuestById() {
        when(guestRepository.findById(anyLong())).thenReturn(Optional.empty());
        guestService.findGuestById(anyLong());
    }
    
    private Guest mockGuest() {
        Guest guest = new Guest("Kaung");
        return guest;
    }
    
    private List<Guest> mockGuestList() {
    	List<Guest> mockGuestList = new ArrayList<Guest>();
    	mockGuestList.add(mockGuest());
    	mockGuestList.add(mockGuest());
    	mockGuestList.add(mockGuest());
    	mockGuestList.add(mockGuest());
    	mockGuestList.add(mockGuest());
        return mockGuestList;
    }
    
    private GuestDTO mockGuestDTO() {
        return GuestDTO.builder().id(0L).name("Kaung Kyaw").build();
    }
    
    private CreateGuestRequestDTO mockCreateGuestRequestDTO() {
    	return CreateGuestRequestDTO.builder().name("Kaung Kyaw").build();
    }
}
