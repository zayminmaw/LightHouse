package com.tmd.lighthouse.controller;

import com.tmd.lighthouse.entity.Buyer;
import com.tmd.lighthouse.entity.UserEntity;
import com.tmd.lighthouse.entity.request.BuyerSignupRequest;
import com.tmd.lighthouse.entity.request.BuyerUpdateRequest;
import com.tmd.lighthouse.entity.response.LoginResponse;
import com.tmd.lighthouse.entity.response.BuyerUpdateResponse;
import com.tmd.lighthouse.repository.BuyerRepository;
import com.tmd.lighthouse.repository.UserRespository;
import com.tmd.lighthouse.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/buyer")
public class BuyerController {

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody BuyerSignupRequest request){
        UserEntity userEntity = userRespository.findByEmail(request.getEmail());
        if(userEntity != null) return new ResponseEntity<>("Email already exist!", HttpStatus.UNPROCESSABLE_ENTITY);
        if(!request.getEmail().matches(regex)) return new ResponseEntity<>("Invalid email!",HttpStatus.UNPROCESSABLE_ENTITY);
        if(request.getPassword().length() < 4) return new ResponseEntity<>("Password have to be at least 4 words!",HttpStatus.UNPROCESSABLE_ENTITY);
        userEntity = new UserEntity();
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        userEntity.setJoinedDate(new Timestamp(System.currentTimeMillis()));
        userEntity = userRespository.save(userEntity);

        Buyer buyer = new Buyer();
        buyer.setAddress(request.getAddress());
        buyer.setName(request.getName());
        buyer.setPhoneNumber(request.getPhoneNo());
        buyer.setUserEntity(userEntity);
        buyer = buyerRepository.save(buyer);

        return ResponseEntity.ok(buyer);
    }

    @PostMapping("/login")
//    @RequestMapping(method = RequestMethod.POST,path = "/login")
    public ResponseEntity<?> login(@RequestBody UserEntity request){
        LoginResponse response = new LoginResponse();
        UserEntity userEntity;
        try{
            userEntity = userRespository.findByEmail(request.getEmail());
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(userEntity == null) return new ResponseEntity<>("User doesn't exist!",HttpStatus.FORBIDDEN);
        if(!bCryptPasswordEncoder.matches(request.getPassword(),userEntity.getPassword())) return new ResponseEntity<>("Wrong password!",HttpStatus.FORBIDDEN);
        Buyer buyer = buyerRepository.findByUserId(userEntity.getId());
        if(buyer == null) return  new ResponseEntity<>("You are not buyer!",HttpStatus.UNAUTHORIZED);
        String token = jwtTokenProvider.createToken("BUYER:"+userEntity.getEmail(),"BUYER");
        response.setStatusCode("200");
        response.setMessage("Login Successful!");
        response.setEmail(userEntity.getEmail());
        response.setToken(token);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody BuyerUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails){
//        List<?> authorities = Arrays.asList(userDetails.getAuthorities().toArray());
//        System.out.println(authorities.get(0));
        String authorities = userDetails.getAuthorities().iterator().next().toString();
        if(!authorities.equals("BUYER")) return new ResponseEntity<>("You are not buyer!",HttpStatus.FORBIDDEN);
        Buyer buyer = buyerRepository.getOne(request.getId());
//        UserEntity userEntity = userRespository.getOne(buyer.getUserId());
        BuyerUpdateResponse response = new BuyerUpdateResponse();
        if(buyer == null) return new ResponseEntity<>("User doesn't exist!",HttpStatus.UNPROCESSABLE_ENTITY);
        if(request.getAddress().equals("") || request.getPhoneNo().equals("") || request.getName().equals("")) return new ResponseEntity<>("Field can't be empty!",HttpStatus.UNPROCESSABLE_ENTITY);
//        if(!request.getEmail().matches(regex)) return new ResponseEntity<>("Invalid email!",HttpStatus.UNPROCESSABLE_ENTITY);
//        if(request.getPassword().length() < 4) return new ResponseEntity<>("Password have to be at least 4 words!",HttpStatus.UNPROCESSABLE_ENTITY);
//        userEntity.setEmail(request.getEmail());
//        userEntity.setPassword(request.getPassword());
//        userRespository.save(userEntity);
        buyer.setPhoneNumber(request.getPhoneNo());
        buyer.setName(request.getName());
        buyer.setAddress(request.getAddress());
        buyer = buyerRepository.save(buyer);
        response.setMessage("Updated!");
        response.setStatusCode("200");
        response.setUpdatedBuyerUser(buyer);
        return ResponseEntity.ok(response);
    }
}
