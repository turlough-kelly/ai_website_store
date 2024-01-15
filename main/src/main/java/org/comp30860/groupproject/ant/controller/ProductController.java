package org.comp30860.groupproject.ant.controller;

import org.comp30860.groupproject.ant.persistence.entity.AccountModel;
import org.comp30860.groupproject.ant.persistence.entity.ProductModel;
import org.comp30860.groupproject.ant.persistence.entity.ReviewModel;
import org.comp30860.groupproject.ant.persistence.repository.AccountRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/products")
    public String getProducts(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<ProductModel> products = productRepository
                .findByHiddenFalse(PageRequest.of(page, 5, Sort.Direction.DESC, "id"));
        model.addAttribute("products", products);

        int totalPages = products.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "products.html";
    }



    @GetMapping("/view/{id}")
    public String getProduct(@PathVariable long id, Model model) {
        Optional<ProductModel> optProduct = productRepository.findById(id);
        ProductModel product = optProduct
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
        model.addAttribute("product", product);
        return "view.html";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable long id, Model model) {
        Optional<ProductModel> optProduct = productRepository.findById(id);
        ProductModel product = optProduct
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
        model.addAttribute("product", product);
        return "edit.html";
    }

    // @PostMapping(value="/edit/{id}")
    // public String editProduct(@ModelAttribute("ProductModel") ProductModel
    // newProduct, @PathVariable long id) {
    // Optional<ProductModel> optProduct = productRepository.findById(id);
    // ProductModel existingProduct = optProduct.orElseThrow(() -> new
    // ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));
    // existingProduct.setDescription(newProduct.getDescription());
    // existingProduct.setTrainedPrice(newProduct.getTrainedPrice());
    // existingProduct.setUntrainedPrice(newProduct.getUntrainedPrice());
    // productRepository.save(existingProduct);
    // return "redirect:/products";
    // }

    @PostMapping("/review/{id}")
    public String addReview(@PathVariable long id, @ModelAttribute ReviewModel review, Authentication authentication) {
        AccountModel account = accountRepository.findByUsername(authentication.getName());
        Optional<ProductModel> optProduct = productRepository.findById(id);
        ProductModel product = optProduct
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found."));

        review.setAccount(account);
        review.setProduct(product);
        reviewRepository.save(review);
        return "redirect:/user/" + id;
    }

}