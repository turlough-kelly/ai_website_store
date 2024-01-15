package org.comp30860.groupproject.ant.controller;

import org.comp30860.groupproject.ant.persistence.entity.AccountModel;
import org.comp30860.groupproject.ant.persistence.entity.OrderModel;
import org.comp30860.groupproject.ant.persistence.entity.ProductModel;
import org.comp30860.groupproject.ant.persistence.entity.ReviewModel;
import org.comp30860.groupproject.ant.persistence.repository.AccountRepository;
import org.comp30860.groupproject.ant.persistence.repository.OrderRepository;
import org.comp30860.groupproject.ant.persistence.repository.ProductRepository;
import org.comp30860.groupproject.ant.persistence.repository.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class AdministratorController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/admin")
    public String getDashboard() {
        return "admin/dashboard.html";
    }

    @GetMapping("/admin/products")
    public String getProducts(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<ProductModel> products = productRepository.findAll(PageRequest.of(page, 5, Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);

        int totalPages = products.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/products.html";
    }

    @GetMapping("/admin/product/{id}")
    public String getProduct(@PathVariable long id, Model model) {
        Optional<ProductModel> optProduct = productRepository.findById(id);
        ProductModel product = optProduct
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));

        model.addAttribute("product", product);
        return "admin/product.html";
    }

    @PostMapping("/admin/product")
    public String addProduct(@RequestParam String name, @ModelAttribute ProductModel product) {
        ProductModel productModel = new ProductModel(name, product.getDescription(), product.getTrainedPrice(),
                product.getUntrainedPrice());
        productRepository.save(productModel);
        return "redirect:/admin/product/" + productModel.getId();
    }

    @PatchMapping("/admin/product/{id}")
    public String updateProduct(@PathVariable long id, @ModelAttribute ProductModel updatedProduct) {
        Optional<ProductModel> optProduct = productRepository.findById(id);
        ProductModel product = optProduct
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));

        product.update(updatedProduct);
        productRepository.save(product);
        return "redirect:/admin/product/" + id;
    }

    @GetMapping("/admin/product/hide/{id}")
    public String hideProduct(@PathVariable long id) {
        Optional<ProductModel> optProduct = productRepository.findById(id);
        ProductModel product = optProduct
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));

        product.hide();
        productRepository.save(product);
        return "redirect:/admin/product/" + id;
    }

    @GetMapping("/admin/product/show/{id}")
    public String showProduct(@PathVariable long id) {
        Optional<ProductModel> optProduct = productRepository.findById(id);
        ProductModel product = optProduct
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));

        product.show();
        productRepository.save(product);
        return "redirect:/admin/product/" +id;
    }

    @PostMapping(value = "/edit")
    public String editProduct(@ModelAttribute("ProductModel") ProductModel newProduct, @RequestParam("id") long id) {
        Optional<ProductModel> optProduct = productRepository.findById(id);
        ProductModel existingProduct = optProduct
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
        existingProduct.setDescription(newProduct.getDescription());
        existingProduct.setTrainedPrice(newProduct.getTrainedPrice());
        existingProduct.setUntrainedPrice(newProduct.getUntrainedPrice());
        productRepository.save(existingProduct);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/admin/product/{id}")
    public String deleteProduct(@PathVariable long id) {
        productRepository.deleteById(id);

        return "redirect:/admin/products";
    }

    @DeleteMapping("/admin/product/review/{id}")
    public String deleteProductReview(@PathVariable long id) {
        Optional<ReviewModel> optReview = reviewRepository.findById(id);
        ReviewModel review = optReview
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found."));

        reviewRepository.deleteById(id);
        return "redirect:/admin/user/" + review.getProduct().getId();
    }

    @GetMapping("/admin/users")
    public String getAccounts(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<AccountModel> accounts = accountRepository.findAll(PageRequest.of(page, 5, Sort.Direction.DESC, "id"));
        model.addAttribute("users", accounts);

        int totalPages = accounts.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/users.html";
    }

    @GetMapping("/admin/user/{id}")
    public String getAccount(@PathVariable long id, Model model) {
        Optional<AccountModel> optAccount = accountRepository.findById(id);
        AccountModel account = optAccount
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found."));

        model.addAttribute("user", account);
        return "admin/user.html";
    }

    @GetMapping("/admin/user/promote/{id}")
    public String promoteAccount(@PathVariable long id) {
        Optional<AccountModel> optAccount = accountRepository.findById(id);
        AccountModel account = optAccount
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found."));

        account.promote();
        accountRepository.save(account);
        return "redirect:/admin/users";//+ id;
    }

    @GetMapping("/admin/user/demote/{id}")
    public String demoteAccount(@PathVariable long id) {
        Optional<AccountModel> optAccount = accountRepository.findById(id);
        AccountModel account = optAccount
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found."));

        account.demote();
        accountRepository.save(account);
        return "redirect:/admin/users"; //+ id;
    }

    @DeleteMapping("/admin/user/review/{id}")
    public String deleteUserReview(@PathVariable long id) {
        Optional<ReviewModel> optReview = reviewRepository.findById(id);
        ReviewModel review = optReview
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found."));

        reviewRepository.deleteById(id);
        return "redirect:/admin/user/" + review.getAccount().getId();
    }

    @GetMapping("/admin/orders")
    public String getOrders(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<OrderModel> orders = orderRepository.findAll(PageRequest.of(page, 5, Sort.Direction.DESC, "id"));
        model.addAttribute("orders", orders);

        int totalPages = orders.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/orders.html";
    }

    @GetMapping("/admin/order/{id}")
    public String getOrder(@PathVariable long id, Model model) {
        Optional<OrderModel> optOrder = orderRepository.findById(id);
        OrderModel order = optOrder
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));

        model.addAttribute("order", order);
        return "admin/order.html";
    }

    @PostMapping("/admin/order/{id}")
    public String updateOrderStatus(@PathVariable long id, @RequestParam OrderModel.Status status) {
        Optional<OrderModel> optOrder = orderRepository.findById(id);
        OrderModel order = optOrder
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found."));
        if (order.getStatus() == OrderModel.Status.SHIPPED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot update order status as order has already been shipped");
        }
        if (order.getStatus() == OrderModel.Status.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Cannot update order status as order has been cancelled");
        }
        order.setStatus(status);
        orderRepository.save(order);
        return "redirect:/admin/order/" + id;
    }

}
