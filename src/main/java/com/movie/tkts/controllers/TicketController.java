package com.movie.tkts.controllers;

import com.movie.tkts.dto.TicketDto;
import com.movie.tkts.entities.Ticket;
import com.movie.tkts.services.TicketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tkts/tickets")
@CrossOrigin(origins = "http://localhost:4200")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/order")
    public ResponseEntity<TicketDto> orderTicket(@RequestBody TicketDto ticketDTO) {
        TicketDto orderedTicket = ticketService.orderTicket(ticketDTO);
        return ResponseEntity.ok(orderedTicket);
    }

//
//    private final TicketService ticketService;
//
//    public TicketController(TicketService ticketService) {
//        this.ticketService = ticketService;
//    }
//
//    @GetMapping
//    public List<Ticket> getAllTickets() {
//        return ticketService.getAllTickets();
//    }
//
//    @GetMapping("/{id}")
//    public Optional<Ticket> getTicketById(@PathVariable Long id) {
//        return ticketService.getTicketById(id);
//    }
//
//    @PostMapping
//    public Ticket createTicket(@RequestBody Ticket ticket) {
//        return ticketService.createTicket(ticket);
//    }
//
////    @PutMapping("/{id}")
////    public Ticket updateTicket(@PathVariable Long id, @RequestBody Ticket ticketDetails) {
////        return ticketService.updateTicket(id, ticketDetails);
////    }
//
//    @DeleteMapping("/{id}")
//    public void deleteTicket(@PathVariable Long id) {
//        ticketService.deleteTicket(id);
//    }
}
