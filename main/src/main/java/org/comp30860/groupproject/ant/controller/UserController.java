package org.comp30860.groupproject.ant.controller;

import org.comp30860.groupproject.ant.persistence.entity.AccountModel;
import org.comp30860.groupproject.ant.persistence.entity.CartItemModel;
import org.comp30860.groupproject.ant.persistence.entity.OrderModel;
import org.comp30860.groupproject.ant.persistence.entity.ProductModel;
import org.comp30860.groupproject.ant.persistence.entity.ReviewModel;
import org.comp30860.groupproject.ant.persistence.repository.AccountRepository;
import org.comp30860.groupproject.ant.persistence.repository.CartItemRepository;
import org.comp30860.groupproject.ant.persistence.repository.OrderRepository;
import org.comp30860.groupproject.ant.persistence.repository.ProductRepository;
import org.comp30860.groupproject.ant.persistence.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UserController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/user")
    public String getProfile(Model model, Authentication authentication) {
        AccountModel account = accountRepository.findByUsername(authentication.getName());
        model.addAttribute("user", account);
        return "user/profile.html";
    }

    @GetMapping("/user/orders")
    public String getOrders(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<OrderModel> orders = orderRepository.findMyOrders(PageRequest.of(page, 5, Sort.Direction.DESC, "id"));
        model.addAttribute("orders", orders);

        int totalPages = orders.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "user/orders.html";
    }

    @GetMapping("/user/order/{id}")
    public String getOrder(@PathVariable long id, Model model, Authentication authentication) {
        AccountModel account = accountRepository.findByUsername(authentication.getName());
        Optional<OrderModel> optOrder = orderRepository.findById(id);
        OrderModel order = optOrder
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));

        if (order.getAccount().getId() != account.getId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
        }
        model.addAttribute("order", order);
        return "user/order.html";
    }

    @DeleteMapping("/user/order/{id}")
    public String cancelOrder(@PathVariable long id, Model model, Authentication authentication) {
        AccountModel account = accountRepository.findByUsername(authentication.getName());
        Optional<OrderModel> optOrder = orderRepository.findById(id);
        OrderModel order = optOrder
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));

        if (order.getAccount().getId() != account.getId()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found.");
        }
        if (order.getStatus() == OrderModel.Status.SHIPPED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot update order status as order has already been shipped");
        }

        order.setStatus(OrderModel.Status.CANCELLED);
        orderRepository.save(order);
        return "redirect:/user/orders";
    }

    @GetMapping("/user/reviews")
    public String getReviews(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<ReviewModel> reviews = reviewRepository.findMyReviews(PageRequest.of(page, 5, Sort.Direction.DESC, "id"));
        model.addAttribute("reviews", reviews);

        int totalPages = reviews.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "user/reviews.html";
    }

    @PostMapping("/user/update")
    public String updateUser(Model model, Authentication authentication,
            @RequestParam(required = false) String username, @RequestParam(required = false) String password) {
        AccountModel account = accountRepository.findByUsername(authentication.getName());
        if (username != null) {
            account.setUsername(username);
        }
        if (password != null) {
            account.setPassword(password);
        }
        model.addAttribute("user", account);
        accountRepository.save(account);
        return "user/profile.html";
    }

    @PostMapping("/cart")
    public String addToCart(@RequestParam(required = true) long productId,
            @RequestParam(required = true) boolean isTrained, @RequestParam(defaultValue = "1") int amount,
            Authentication authentication) {
        AccountModel account = accountRepository.findByUsername(authentication.getName());
        Optional<ProductModel> optProduct = productRepository.findById(productId);
        ProductModel product = optProduct
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));

        List<CartItemModel> sameItem = account.getCart().stream()
                .filter(c -> c.getProduct().getId() == productId && c.isTrained() == isTrained).toList();

        CartItemModel cartItem;
        if (sameItem.isEmpty()) {
            cartItem = new CartItemModel(account, product, isTrained, amount);
        } else {
            cartItem = sameItem.get(0);
            cartItem.setAmount(amount + cartItem.getAmount());
        }

        cartItemRepository.save(cartItem);
        return "redirect:/cart";
    }

    @PatchMapping("/cart")
    public String updateCartItemAmount(@RequestParam(required = true) long id,
            @RequestParam(defaultValue = "1") int amount) {
        Optional<CartItemModel> optCartItem = cartItemRepository.findById(id);
        CartItemModel cartItem = optCartItem
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found."));
        
        cartItem.setAmount(amount);
        cartItemRepository.save(cartItem);
        return "redirect:/cart";
    }

    @DeleteMapping("/cart")
    public String removeFromCart(@RequestParam(required = true) long productId,
            @RequestParam(required = true) boolean isTrained, Authentication authentication) {
        AccountModel account = accountRepository.findByUsername(authentication.getName());

        List<CartItemModel> sameItem = account.getCart().stream()
                .filter(c -> c.getProduct().getId() == productId && c.isTrained() == isTrained).toList();

        if (sameItem.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found.");
        }

        cartItemRepository.deleteById(sameItem.get(0).getId());
        return "redirect:/cart";
    }

}
