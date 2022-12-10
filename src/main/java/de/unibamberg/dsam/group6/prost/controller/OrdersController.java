package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.repository.OrdersRepository;
import de.unibamberg.dsam.group6.prost.util.CartDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersRepository ordersRepo;

    @GetMapping("/orders")
    public String showOrders(Model model) {
        var orders = this.ordersRepo.findAll().stream().map(CartDTO::fromOrder).toList();
        model.addAttribute("orders", orders);
        return "pages/orders";
    }
}
