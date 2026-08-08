package com.github.elja9y.event_booking.service.impl;

import com.github.elja9y.event_booking.dto.booking.BookingResponse;
import com.github.elja9y.event_booking.entity.*;
import com.github.elja9y.event_booking.exception.*;
import com.github.elja9y.event_booking.mapper.BookingMapper;
import com.github.elja9y.event_booking.repository.*;
import com.github.elja9y.event_booking.service.BookingService;
import com.github.elja9y.event_booking.service.impl.EventServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    private BookingRepository bookingRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private BookingMapper bookingMapper;
    private EventServiceImpl eventService;

    @Override
    public BookingResponse bookEvent(Long eventId, String username) {
        Event event = eventService.getEventEntityById(eventId);
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserException::userNotFound);

        // Business rules
        if (event.getEventDate().isBefore(LocalDateTime.now()))
            throw BookingException.eventIsPast();

        if (event.getBookedCount() >= event.getCapacity())
            throw BookingException.eventFull();

        if (bookingRepository.existsByUserIdAndEventIdAndCancelledFalse(user.getId(), eventId))
            throw BookingException.alreadyBooked();

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setEvent(event);
        booking.setBookedAt(LocalDateTime.now());

        // Increment booked count
        event.setBookedCount(event.getBookedCount() + 1);
        eventRepository.save(event);

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    public BookingResponse cancelBooking(Long bookingId, String username) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(BookingException::bookingNotFound);

        if (!booking.getUser().getUsername().equals(username))
            throw BookingException.notYourBooking();

        if (booking.isCancelled())
            throw BookingException.alreadyCancelled();

        booking.setCancelled(true);

        // Free up the spot
        Event event = booking.getEvent();
        event.setBookedCount(event.getBookedCount() - 1);
        eventRepository.save(event);

        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    public List<BookingResponse> getMyBookings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(UserException::userNotFound);
        return bookingRepository.findByUserId(user.getId()).stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(bookingMapper::toResponse)
                .toList();
    }
}