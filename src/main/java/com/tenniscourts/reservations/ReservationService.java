package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.transaction.Transactional;

import static com.tenniscourts.reservations.ReservationStatus.*;

@Service
@AllArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;
    
    private final ScheduleRepository scheduleRepository;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        throw new UnsupportedOperationException();
    }

    public ReservationDTO findReservation(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        return reservationMapper.map(this.cancel(reservationId));
    }

    private Reservation cancel(Long reservationId) {
        return reservationRepository.findById(reservationId).map(reservation -> {

            this.validateCancellation(reservation);

            BigDecimal refundValue = getRefundValue(reservation);
            return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

        }).orElseThrow(() -> {
            throw new EntityNotFoundException("Reservation not found.");
        });
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        reservation.setReservationStatus(status);
        reservation.setValue(reservation.getValue().subtract(refundValue));
        reservation.setRefundValue(refundValue);

        return reservationRepository.save(reservation);
    }

    private void validateCancellation(Reservation reservation) {
        if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
            throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
        }

        if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

        if (hours >= 24) {
            return reservation.getValue();
        }

        return BigDecimal.ZERO;
    }

    /*TODO: This method actually not fully working, find a way to fix the issue when it's throwing the error:
            "Cannot reschedule to the same slot.*/
    @Transactional
    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
    	
    	// Check if reservation exists.
    	Reservation previousReservation = reservationRepository.findById(previousReservationId).orElseThrow(()->{
            throw new EntityNotFoundException("Reservation not found.");
        });
    	
    	// Check if scheduleId is in database
        Schedule newSchedule = scheduleRepository.findById(scheduleId).orElseThrow(()->{
        	throw new EntityNotFoundException("New Schedule not found.");
        });
    	
    	// Check if it is in READY_TO_PLAY status
    	if(!READY_TO_PLAY.equals(previousReservation.getReservationStatus())){
            throw new IllegalArgumentException("Reservations with status READY_TO_PLAY can only be rescheduled");
        }
    	
        //  Check if the user is trying to schedule to the same timeslot in same court
        if (newSchedule.getStartDateTime().equals(previousReservation.getSchedule().getStartDateTime())
                && newSchedule.getTennisCourt().getId().equals(previousReservation.getSchedule().getTennisCourt().getId())) {
            throw new IllegalArgumentException("Cannot reschedule to the same slot.");
        }
        
        // Check if the reservation still can be modified
        validateCancellation(previousReservation);

        // Calculate refund value
        BigDecimal refund = getRefundValue(previousReservation);
        
        // Update the reservation as Rescheduled
        previousReservation = updateReservation(previousReservation, refund, ReservationStatus.RESCHEDULED);

        // Book a new reservation
        ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                .guestId(previousReservation.getGuest().getId())
                .scheduleId(scheduleId)
                .build());
        
        // Record the old reservation
        newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
        
        return newReservation;
    }
}
