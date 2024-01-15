package org.comp30860.groupproject.ant.controller;

import jakarta.servlet.http.HttpServletResponse;

import org.comp30860.groupproject.ant.persistence.entity.AccountModel;
import org.comp30860.groupproject.ant.persistence.entity.CartItemModel;
import org.comp30860.groupproject.ant.persistence.entity.ProductModel;
import org.comp30860.groupproject.ant.persistence.repository.AccountRepository;
import org.comp30860.groupproject.ant.persistence.repository.CartItemRepository;
import org.comp30860.groupproject.ant.persistence.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Controller
public class CartController {
    @Autowired
    CartItemRepository cartItemRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    AccountRepository accountRepository;

    @PostMapping(value = "/carttrained")
    public void addTrained(CartItemModel cartItemModel, Model model, @RequestParam("id") Long id,
            HttpServletResponse httpServletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser") || !authentication.isAuthenticated()) {
            try {
                httpServletResponse.sendRedirect("/login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Optional<ProductModel> productModel = productRepository.findById(id);
            if (productModel.isPresent()) {
                ProductModel product = productModel.get();
                model.addAttribute("product", product);
                AccountModel account = accountRepository.findByUsername(authentication.getName());
                CartItemModel cartItem = new CartItemModel(account, product, true, cartItemModel.getAmount());
                cartItemRepository.save(cartItem);
            }
            try {
                httpServletResponse.sendRedirect("/cart");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping(value = "/cartuntrained")
    public void addunTrained(CartItemModel cartItemModel, Model model, @RequestParam("id") Long id,
            HttpServletResponse httpServletResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser") || !authentication.isAuthenticated()) {
            try {
                httpServletResponse.sendRedirect("/login");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Optional<ProductModel> productModel = productRepository.findById(id);
            if (productModel.isPresent()) {
                ProductModel product = productModel.get();
                model.addAttribute("product", product);
                AccountModel account = accountRepository.findByUsername(authentication.getName());
                CartItemModel cartItem = new CartItemModel(account, product, false, cartItemModel.getAmount());
                cartItemRepository.save(cartItem);
            }
            try {
                httpServletResponse.sendRedirect("/cart");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping(value = "/cart")
    public String viewCart(Model model, Authentication authentication) {
        AccountModel account = accountRepository.findByUsername(authentication.getName());
        Set<CartItemModel> cart = account.getCart();
        double totalPrice = account.getCartPrice();
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("cart", cart);
        return "cart.html";
    }

    @PostMapping(value = "/changeTrained")
    public void changeTrained(@RequestParam("id") long id, Model model, HttpServletResponse response) {
        Optional<CartItemModel> cart = cartItemRepository.findById(id);
        CartItemModel cartItemModel = cart
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart item not found."));
        if (cartItemModel.isTrained()) {
            cartItemModel.setTrained(false);
            cartItemModel.setPrice(cartItemModel.getProduct().getUntrainedPrice() * cartItemModel.getAmount());

        } else {
            cartItemModel.setTrained(true);
            cartItemModel.setPrice(cartItemModel.getProduct().getTrainedPrice() * cartItemModel.getAmount());
        }
        cartItemRepository.save(cartItemModel);
        try {
            response.sendRedirect("/cart");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
