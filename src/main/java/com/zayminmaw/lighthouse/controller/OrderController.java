package com.zayminmaw.lighthouse.controller;

import com.zayminmaw.lighthouse.entity.*;
import com.zayminmaw.lighthouse.entity.request.OrderCreateRequest;
import com.zayminmaw.lighthouse.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
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

    @PostMapping("/getCart")
    public ResponseEntity<?> getCart(@AuthenticationPrincipal UserDetails userDetails){
        UserEntity user = userRespository.findByEmail(userDetails.getUsername());
        if (user == null) return new ResponseEntity<>("User doesn't exist!", HttpStatus.FORBIDDEN);
        Buyer buyer = buyerRepository.findByUserId(user.getId());
        if (buyer == null) return new ResponseEntity<>("You are not buyer!", HttpStatus.UNAUTHORIZED);
        Optional<Order> orderOptional = orderRepository.findByOrderStatusAndBuyerId("CART",buyer.getId());
        if(orderOptional.isEmpty()){
            Order order = new Order();
            order.setBuyer(buyer);
            order.setOrderStatus("CART");
            order = orderRepository.save(order);
            return ResponseEntity.ok(orderItemRepository.findAllProductAndQuantityAndSubTotalByOrderId(order.getId()));
        }
        Order order = orderOptional.get();
        return ResponseEntity.ok(orderItemRepository.findAllProductAndQuantityAndSubTotalByOrderId(order.getId()));
    }
    @PostMapping("/add")
    public ResponseEntity<?> addOrderItem(@RequestParam("productId") long productId,@RequestParam("quantity") int quantity,@AuthenticationPrincipal UserDetails userDetails){
        UserEntity user = userRespository.findByEmail(userDetails.getUsername());
        if (user == null) return new ResponseEntity<>("User doesn't exist!", HttpStatus.FORBIDDEN);
        Buyer buyer = buyerRepository.findByUserId(user.getId());
        if (buyer == null) return new ResponseEntity<>("You are not buyer!", HttpStatus.UNAUTHORIZED);
        Optional<Order> orderOptional = orderRepository.findByOrderStatusAndBuyerId("CART", buyer.getId());
        if(orderOptional.isEmpty()) return new ResponseEntity<>("Cart doesn't exist!",HttpStatus.NOT_FOUND);
        Order order = orderOptional.get();
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) return new ResponseEntity<>("Product doesn't exist anymore!",HttpStatus.NOT_FOUND);
        Product product = productOptional.get();
        Double subTotal = product.getPrice() * quantity;
        OrderItem orderItem = new OrderItem();
        orderItem.setSubTotal(subTotal);
        orderItem.setQuantity(quantity);
        orderItem.setProduct(product);
        orderItem.setOrder(order);
        orderItemRepository.save(orderItem);
        return ResponseEntity.ok("Added!");
    }
    @PutMapping("/update")
    public ResponseEntity<?> updateOrderItem(@RequestParam("productId") long productId,@RequestParam("quantity") int quantity,@AuthenticationPrincipal UserDetails userDetails){
        UserEntity user = userRespository.findByEmail(userDetails.getUsername());
        if (user == null) return new ResponseEntity<>("User doesn't exist!", HttpStatus.FORBIDDEN);
        Buyer buyer = buyerRepository.findByUserId(user.getId());
        if (buyer == null) return new ResponseEntity<>("You are not buyer!", HttpStatus.UNAUTHORIZED);
        Optional<Order> orderOptional = orderRepository.findByOrderStatusAndBuyerId("CART", buyer.getId());
        if(orderOptional.isEmpty()) return new ResponseEntity<>("Cart doesn't exist!",HttpStatus.NOT_FOUND);
        Order order = orderOptional.get();
        Optional<Product> productOptional = productRepository.findById(productId);
        if(productOptional.isEmpty()) return new ResponseEntity<>("Product doesn't exist anymore!",HttpStatus.NOT_FOUND);
        Product product = productOptional.get();
        OrderItem orderItem =orderItemRepository.findByProductIdAndOrderId(productId,order.getId());
        if(quantity == 0){
            orderItemRepository.delete(orderItem);
        }
        Double subTotal = product.getPrice() * quantity;
        orderItem.setSubTotal(subTotal);
        orderItem.setQuantity(quantity);
        orderItemRepository.save(orderItem);
        return ResponseEntity.ok("Updated!");
    }
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeOrderItem(@RequestParam("productId") long productId,@AuthenticationPrincipal UserDetails userDetails){
        UserEntity user = userRespository.findByEmail(userDetails.getUsername());
        if (user == null) return new ResponseEntity<>("User doesn't exist!", HttpStatus.FORBIDDEN);
        Buyer buyer = buyerRepository.findByUserId(user.getId());
        if (buyer == null) return new ResponseEntity<>("You are not buyer!", HttpStatus.UNAUTHORIZED);
        Optional<Order> orderOptional = orderRepository.findByOrderStatusAndBuyerId("CART", buyer.getId());
        if(orderOptional.isEmpty()) return new ResponseEntity<>("Cart doesn't exist!",HttpStatus.NOT_FOUND);
        Order order = orderOptional.get();
        orderItemRepository.delete(orderItemRepository.findByProductIdAndOrderId(productId,order.getId()));
        return ResponseEntity.ok("Removed!");
    }
    @DeleteMapping("/removeAll")
    public ResponseEntity<?> removeAllOrderItem(@AuthenticationPrincipal UserDetails userDetails){
        UserEntity user = userRespository.findByEmail(userDetails.getUsername());
        if (user == null) return new ResponseEntity<>("User doesn't exist!", HttpStatus.FORBIDDEN);
        Buyer buyer = buyerRepository.findByUserId(user.getId());
        if (buyer == null) return new ResponseEntity<>("You are not buyer!", HttpStatus.UNAUTHORIZED);
        Optional<Order> orderOptional = orderRepository.findByOrderStatusAndBuyerId("CART", buyer.getId());
        if(orderOptional.isEmpty()) return new ResponseEntity<>("Cart doesn't exist!",HttpStatus.NOT_FOUND);
        Order order = orderOptional.get();
        orderItemRepository.deleteAll(orderItemRepository.findAllByOrderId(order.getId()));
        return ResponseEntity.ok("Cart Emptied!");
    }
    @PutMapping("/confirm")
    public ResponseEntity<?> confirmOrder(@RequestBody OrderCreateRequest request,@AuthenticationPrincipal UserDetails userDetails){
        UserEntity user = userRespository.findByEmail(userDetails.getUsername());
        if (user == null) return new ResponseEntity<>("User doesn't exist!", HttpStatus.FORBIDDEN);
        Buyer buyer = buyerRepository.findByUserId(user.getId());
        if (buyer == null) return new ResponseEntity<>("You are not buyer!", HttpStatus.UNAUTHORIZED);
        Optional<Order> orderOptional = orderRepository.findByOrderStatusAndBuyerId("CART", buyer.getId());
        if(orderOptional.isEmpty()) return new ResponseEntity<>("Cart doesn't exist!",HttpStatus.NOT_FOUND);
        Order order = orderOptional.get();
        Double total = 0.0;
        for (OrderItem i : orderItemRepository.findAllByOrderId(order.getId())){
            total += i.getSubTotal();
        }
        order.setTotal(total);
        order.setPhoneNo(request.getPhoneNo());
        order.setAddress(request.getAddress());
        order.setOrderStatus("CONFIRM");
        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
        return ResponseEntity.ok("Order Confirm!");
    }
    @GetMapping("/all")
    public ResponseEntity<?> allOrder(@AuthenticationPrincipal UserDetails userDetails){
        String authorities = userDetails.getAuthorities().iterator().next().toString();
        if(!authorities.equals("SELLER")) return new ResponseEntity<>("You are not seller!",HttpStatus.FORBIDDEN);
        return ResponseEntity.ok(orderRepository.findAllByOrderStatusNot("CART"));
    }
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestParam("orderId") long orderId,@AuthenticationPrincipal UserDetails userDetails){
        String authorities = userDetails.getAuthorities().iterator().next().toString();
        if(!authorities.equals("SELLER")) return new ResponseEntity<>("You are not seller!",HttpStatus.FORBIDDEN);
        Order order = orderRepository.findByOrderStatusAndId("CONFIRM",orderId);
        if(order == null) return new ResponseEntity<>("Order doesn't exist!",HttpStatus.NOT_FOUND);
        order.setOrderStatus("CANCEL");
        orderRepository.save(order);
        return ResponseEntity.ok("Order Canceled! ID : "+orderId);
    }

//    @PostMapping("")
//    public ResponseEntity<?> order(@RequestBody OrderCreateRequest request, @AuthenticationPrincipal UserDetails userDetails){
//        UserEntity user = userRespository.findByEmail(userDetails.getUsername());
//        if (user == null) return new ResponseEntity<>("User doesn't exist!", HttpStatus.FORBIDDEN);
//        Buyer buyer = buyerRepository.findByUserId(user.getId());
//        if (buyer == null) return new ResponseEntity<>("You are not buyer!", HttpStatus.UNAUTHORIZED);
//        Order order = new Order();
//        order.setAddress(request.getAddress());
//        order.setPhoneNo(request.getPhoneNo());
//        order.setBuyer(buyer);
//        order.setOrderDate(new Timestamp(System.currentTimeMillis()));
//        order.setOrderStatus("RECEIVED");
//        order.setTotal(0);
//        orderRepository.save(order);
//        Double total = 0.0;
//        Product product;
//        for (OrderItemRequest i : request.getProduct()){
//            Optional<Product> productOptional = productRepository.findById(i.getId());
//            if(productOptional.isEmpty()){
//                orderRepository.delete(order);
//                return new ResponseEntity<>("Product does not exist!",HttpStatus.NOT_FOUND);
//            }
//            product = productOptional.get();
//            double subTotal = product.getPrice() * i.getQuantity();
//            total += subTotal;
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order);
//            orderItem.setProduct(product);
//            orderItem.setQuantity(i.getQuantity());
//            orderItem.setSubTotal(subTotal);
//            orderItemRepository.save(orderItem);
//        }
//        order.setTotal(total);
//        orderRepository.save(order);
//        return ResponseEntity.ok("Order Successful!");
//    }
//

//
//    @DeleteMapping("/delete")
//    public ResponseEntity<?> deleteOrder(@RequestParam("id") long id,@AuthenticationPrincipal UserDetails userDetails){
//        String authorities = userDetails.getAuthorities().iterator().next().toString();
//        if(!authorities.equals("SELLER")) return new ResponseEntity<>("You are not seller!",HttpStatus.FORBIDDEN);
//        Optional<Order> orderOptional = orderRepository.findById(id);
//        if(orderOptional.isEmpty()) return new ResponseEntity<>("This order doesn't exist!",HttpStatus.NOT_FOUND);
//        Order order = orderOptional.get();
//        orderItemRepository.deleteAll(orderItemRepository.findAllByOrderId(order.getId()));
//        orderRepository.delete(order);
//        return ResponseEntity.ok("Deleted!");
//    }
}
