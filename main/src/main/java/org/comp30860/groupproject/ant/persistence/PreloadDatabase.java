package org.comp30860.groupproject.ant.persistence;

import org.comp30860.groupproject.ant.persistence.entity.AccountModel;
import org.comp30860.groupproject.ant.persistence.entity.ProductModel;
import org.comp30860.groupproject.ant.persistence.repository.AccountRepository;
import org.comp30860.groupproject.ant.persistence.repository.ProductRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PreloadDatabase {

    @Bean
    CommandLineRunner initDatabase(AccountRepository accountRepo, ProductRepository productRepo) {
        return args -> {
            AccountModel superAdmin = accountRepo.findByUsername("superadmin");

            if (superAdmin == null) {
                superAdmin = new AccountModel("Super", "Admin", "superadmin", "superadmin@comp30860.com", "password123",
                        "owner");
                accountRepo.save(superAdmin);
            }

            AccountModel customer = accountRepo.findByUsername("customer");

            if (customer == null) {
                customer = new AccountModel("customer", "one", "customer", "customer@comp308601.com", "customer123",
                        "customer");
                accountRepo.save(customer);
            }

            ProductModel product1 = productRepo.findByName("Background Removal");

            if (product1 == null) {
                product1 = new ProductModel("Background Removal", "Remove backgrounds from your photos", 300.0, 100.00,
                        false);
                productRepo.save(product1);
            }
            ProductModel product2 = productRepo.findByName("Person Detection");

            if (product2 == null) {
                product2 = new ProductModel("Person Detection", "Detects humans in photos and videos", 350.0, 125.00,
                        false);
                productRepo.save(product2);
            }
            ProductModel product3 = productRepo.findByName("Chatbot");

            if (product3 == null) {
                product3 = new ProductModel("Chatbot", "Answers customer queries on your website", 250.0, 80.0,
                        false);
                productRepo.save(product3);
            }
            ProductModel product4 = productRepo.findByName("Emotion Recognition");

            if (product4 == null) {
                product4 = new ProductModel("Emotion Recognition", "Detects emotions on human faces in photos", 200.0, 70.0,
                        false);
                productRepo.save(product4);
            }
            ProductModel product5 = productRepo.findByName("Iris recognition");

            if (product5 == null) {
                product5 = new ProductModel("Iris recognition", "Detects and recognises human irises", 400.0, 150.0,
                        false);
                productRepo.save(product5);
            }
            ProductModel product6 = productRepo.findByName("Text detection");

            if (product6 == null) {
                product6 = new ProductModel("Text detection", "capable of detecting and reading the text in an image", 200.0, 120.0,
                        false);
                productRepo.save(product6);
            }
            ProductModel product7 = productRepo.findByName("Dog breed classification");

            if (product7 == null) {
                product7 = new ProductModel("Dog breed classification", "Can detect and return the dog breed of a dog in a photo", 200.0, 100.0,
                        false);
                productRepo.save(product7);
            }
            ProductModel product8 = productRepo.findByName("Furniture segmentation");

            if (product8 == null) {
                product8 = new ProductModel("Furniture segmentation", "Allows you to visualise what an item of furniture could look like in your home", 400.0, 230.0,
                        false);
                productRepo.save(product8);
            }
            ProductModel product9 = productRepo.findByName("Recipe generator");

            if (product9 == null) {
                product9 = new ProductModel("Recipe generator", "Returns potential recipe and dish ideas based on ingredients you submit", 210.0, 100.0,
                        false);
                productRepo.save(product9);
            }
            ProductModel product10 = productRepo.findByName("AI detector");

            if (product10 == null) {
                product10 = new ProductModel("AI detector", "Detects the possibility of AI involvement in a written text", 610.0, 340.0,
                        false);
                productRepo.save(product10);
            }
        };
    }

}