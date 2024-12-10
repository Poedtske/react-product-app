package com.example.backend.service.impl;

import com.example.backend.dto.TicketDto;
import com.example.backend.exceptions.AppException;
import com.example.backend.mapper.TicketMapper;
import com.example.backend.model.Event;
import com.example.backend.model.Tafel;
import com.example.backend.model.Ticket;
import com.example.backend.model.User;
import com.example.backend.repository.TicketRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    private final TableServiceImpl tableService;
    private final EventServiceImpl eventService;
    private final UserRepository userRepository;


    @Override
    public Ticket save(Ticket ticket) {
        ticketRepository.save(ticket);
        return ticket;
    }

    @Override
    public TicketDto updateById(Long id, TicketDto ticketDto) {
        // Fetch the existing Ticket entity
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));

        // Map TicketDto to Ticket entity to update fields
        Ticket updatedTicket = ticketMapper.toTicket(ticketDto);

        // Update fields from DTO while keeping relationships intact
        existingTicket.setPrice(updatedTicket.getPrice());
        existingTicket.setPaid(updatedTicket.getPaid());
        existingTicket.setTable(updatedTicket.getTable());
        existingTicket.setOwner(updatedTicket.getOwner());
        existingTicket.setEvent(updatedTicket.getEvent());

        // Save and map back to DTO
        Ticket savedTicket = ticketRepository.save(existingTicket);

        return new TicketDto(savedTicket.getId(),savedTicket.getTable().getId(),savedTicket.getOwner(),savedTicket.getPrice(),savedTicket.getEvent().getId(),savedTicket.getPaid());
    }

    @Override
    public Iterable<TicketDto> findAll() {
        return null;
    }

    @Override
    public TicketDto findById(Long id) {


        Ticket t= ticketRepository.findById(id).orElseThrow(()->new AppException("Unknown user",HttpStatus.NOT_FOUND));
        TicketDto newTicketDto=new TicketDto(t.getId(),t.getTable().getId(),t.getOwner(),t.getPrice(),t.getEvent().getId(),t.getPaid());
        return newTicketDto;
    }


    public Ticket findTicketById(Long id) {


        return ticketRepository.findById(id).orElseThrow(()->new AppException("Unknown user",HttpStatus.NOT_FOUND));

    }

    @Override
    public void deleteById(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public ResponseEntity createTicket(String email, TicketDto ticketDto) {
        try{
            User user= userRepository.findByEmail(email).orElseThrow();
            Event event= eventService.findById(ticketDto.getEvent());
            Tafel table= tableService.findById(ticketDto.getTable());
            if(table.checkAvailableSeats(ticketDto.getAmount())){
                for (int i=0;i<ticketDto.getAmount();i++){
                    Ticket t=Ticket.builder()
                            .price(event.getTicketPrice())
                            .table(table)
                            .owner(user)
                            .event(event)
                            .paid(false)
                            .build();
                    ticketRepository.save(t);
                }

                //TicketDto newTicketDto=new TicketDto(t.getId(),t.getTable().getId(),t.getOwner(),t.getPrice(),t.getEvent().getId(),t.getPaid(), ticketDto.getAmount());
                return ResponseEntity.ok().build();
            }else{
                throw new AppException("No more available seats",HttpStatus.BAD_REQUEST);
            }

        }catch(AppException a){
            return ResponseEntity.badRequest().body(a);
        }catch(Exception e){
            return ResponseEntity.internalServerError().body(e);
        }
    }

}
