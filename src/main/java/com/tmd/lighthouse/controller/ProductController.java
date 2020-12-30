package com.tmd.lighthouse.controller;

import com.tmd.lighthouse.entity.Product;
import com.tmd.lighthouse.entity.response.*;
import com.tmd.lighthouse.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/news")
    public ResponseEntity<?> creates(@RequestBody List<Product> request){
        productRepository.saveAll(request);
        ProductsCreateResponse response = new ProductsCreateResponse();
        response.setMessage("Everything Inserted!");
        response.setStatusCode("200");
        response.setNewProducts(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/new")
    public ResponseEntity<?> create(@RequestBody Product request){
        productRepository.save(request);
        ProductCreateResponse response = new ProductCreateResponse();
        response.setMessage("Product Inserted");
        response.setStatusCode("200");
        response.setNewProduct(request);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Product request){
        Optional<Product> productOptional = productRepository.findById(request.getId());
        if(productOptional.isEmpty()) return new ResponseEntity<>("No Such ID!", HttpStatus.UNPROCESSABLE_ENTITY);
        Product product = productOptional.get();
        product.setName(request.getName());
        product.setCountry(request.getCountry());
        product.setDistributor(request.getDistributor());
        product.setGenericName(request.getGenericName());
        product.setManufacturer(request.getManufacturer());
        product.setPicture(request.getPicture());
        product.setPreparation(request.getPreparation());
        product.setPrice(request.getPrice());
        product.setUnit(request.getUnit());
        productRepository.save(product);
        ProductUpdateResponse response = new ProductUpdateResponse();
        response.setMessage("Product Updated");
        response.setStatusCode("200");
        response.setUpdatedProduct(request);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("id") long id){
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) return new ResponseEntity<>("No Such ID!",HttpStatus.UNPROCESSABLE_ENTITY);
        Product product = productOptional.get();
        productRepository.delete(product);
        ProductDeleteResponse response = new ProductDeleteResponse();
        response.setStatusCode("200");
        response.setMessage("Deleted ID : "+id);
        response.setDeletedProduct(product);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/withPagination")
    public ResponseEntity<?> readWithPagination(@RequestParam("offset") Integer offset,@RequestParam("limit") Integer limit){
        Pageable page = PageRequest.of(offset,limit,Sort.by("id"));
        return ResponseEntity.ok(productRepository.findAll(page));
    }
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam("q") String name){
        List<Product> resultList = productRepository.findAllByName(name);
        return ResponseEntity.ok(resultList);
    }
    @GetMapping("")
    public ResponseEntity<?> read(){
        List<Product> productList = productRepository.findAll(Sort.by("id").ascending());
        return ResponseEntity.ok(productList);
    }
}
