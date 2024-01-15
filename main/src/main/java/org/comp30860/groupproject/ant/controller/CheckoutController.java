package org.comp30860.groupproject.ant.controller;

import org.comp30860.groupproject.ant.persistence.entity.AccountModel;
import org.comp30860.groupproject.ant.persistence.entity.CartItemModel;
import org.comp30860.groupproject.ant.persistence.entity.OrderModel;
import org.comp30860.groupproject.ant.persistence.repository.AccountRepository;
import org.comp30860.groupproject.ant.persistence.repository.CartItemRepository;
import org.comp30860.groupproject.ant.persistence.repository.OrderItemRepository;
import org.comp30860.groupproject.ant.persistence.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

@Controller
public class CheckoutController {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/checkout")
    public String getOrders(Model model, Authentication authentication) {
        AccountModel account = accountRepository.findByUsername(authentication.getName());
        Set<CartItemModel> cart = account.getCart();
        if (cart.isEmpty()) {
            return "redirect:/products";
        }
        OrderModel order = new OrderModel(account, account.getCart());
        order.setStatus(OrderModel.Status.PENDING);
        model.addAttribute("order", order);
        orderRepository.save(order);
        orderItemRepository.saveAll(order.getOrderItems());
        return "checkout.html";
    }

    @PostMapping("/purchase")
    public String getOrder(@RequestParam("id") long id, Authentication authentication, Model model) {
        AccountModel account = accountRepository.findByUsername(authentication.getName());
        Optional<OrderModel> optOrder = orderRepository.findById(id);
        OrderModel order = optOrder
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));

        if (order.getAccount().getId() != account.getId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
        }
        model.addAttribute("order", order);
        order.setStatus(OrderModel.Status.CONFIRMED);
        orderRepository.save(order);
        cartItemRepository.deleteAll(account.getCart());
        return "ordersuccess.html";
    }

}
