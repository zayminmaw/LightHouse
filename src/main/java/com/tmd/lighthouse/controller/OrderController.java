package com.tmd.lighthouse.controller;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.tmd.lighthouse.entity.*;
import com.tmd.lighthouse.entity.request.OrderCreateRequest;
import com.tmd.lighthouse.entity.request.OrderItemRequest;
import com.tmd.lighthouse.repository.*;
import org.apache.catalina.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @PostMapping("")
    public ResponseEntity<?> order(@RequestBody OrderCreateRequest request, @AuthenticationPrincipal UserDetails userDetails){
        UserEntity user = userRespository.findByEmail(userDetails.getUsername());
        if (user == null) return new ResponseEntity<>("User doesn't exist!", HttpStatus.FORBIDDEN);
        Buyer buyer = buyerRepository.findByUserId(user.getId());
        if (buyer == null) return new ResponseEntity<>("You are not buyer!", HttpStatus.UNAUTHORIZED);
        Order order = new Order();
        order.setAddress(request.getAddress());
        order.setPhoneNo(request.getPhoneNo());
        order.setBuyer(buyer);
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        order.setOrderStatus("RECEIVED");
        order.setTotal(0);
        orderRepository.save(order);
        Double total = 0.0;
        Product product;
        for (OrderItemRequest i : request.getProduct()){
            Optional<Product> productOptional = productRepository.findById(i.getId());
            if(productOptional.isEmpty()){
                orderRepository.delete(order);
                return new ResponseEntity<>("Product does not exist!",HttpStatus.NOT_FOUND);
            }
            product = productOptional.get();
            double subTotal = product.getPrice() * i.getQuantity();
            total += subTotal;
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(i.getQuantity());
            orderItem.setSubTotal(subTotal);
            orderItemRepository.save(orderItem);
        }
        order.setTotal(total);
        orderRepository.save(order);
        return ResponseEntity.ok("Order Successful!");
    }

    @GetMapping("")
    public ResponseEntity<?> allOrder(@AuthenticationPrincipal UserDetails userDetails){
        String authorities = userDetails.getAuthorities().iterator().next().toString();
        if(!authorities.equals("SELLER")) return new ResponseEntity<>("You are not seller!",HttpStatus.FORBIDDEN);
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteOrder(@RequestParam("id") long id,@AuthenticationPrincipal UserDetails userDetails){
        String authorities = userDetails.getAuthorities().iterator().next().toString();
        if(!authorities.equals("SELLER")) return new ResponseEntity<>("You are not seller!",HttpStatus.FORBIDDEN);
        Optional<Order> orderOptional = orderRepository.findById(id);
        if(orderOptional.isEmpty()) return new ResponseEntity<>("This order doesn't exist!",HttpStatus.NOT_FOUND);
        Order order = orderOptional.get();
        orderItemRepository.deleteAll(orderItemRepository.findAllByOrderId(order.getId()));
        orderRepository.delete(order);
        return ResponseEntity.ok("Deleted!");
    }
}
