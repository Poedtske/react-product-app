package com.example.backend.service.impl;

import com.example.backend.dto.TicketDto;
import com.example.backend.exceptions.AppException;
import com.example.backend.model.*;
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
        return null;
    }

    @Override
    public Iterable<TicketDto> findAll() {
        return null;
    }

    @Override
    public TicketDto findById(Long id) {


        Ticket t= ticketRepository.findById(id).orElseThrow(()->new AppException("Unknown user",HttpStatus.NOT_FOUND));
        TicketDto newTicketDto=new TicketDto(t.getId(),t.getTable().getId(),t.getInvoice().getUser(),t.getPrice(),t.getEvent().getId());
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
            Invoice invoice =user.getActiveInvoice();

            Event event= eventService.findById(ticketDto.getEvent());
            Tafel table= tableService.findById(ticketDto.getTable());
            if(table.checkAvailableSeats(ticketDto.getAmount())){
                for (int i=0;i<ticketDto.getAmount();i++){
                    Ticket t=Ticket.builder()
                            .price(event.getTicketPrice())
                            .table(table)
                            .invoice(invoice)
                            .event(event)
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
