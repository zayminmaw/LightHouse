package com.zayminmaw.lighthouse.controller;

import com.zayminmaw.lighthouse.entity.Seller;
import com.zayminmaw.lighthouse.entity.UserEntity;
import com.zayminmaw.lighthouse.entity.request.SellerSignupRequest;
import com.zayminmaw.lighthouse.entity.request.SellerUpdateRequest;
import com.zayminmaw.lighthouse.entity.response.LoginResponse;
import com.zayminmaw.lighthouse.entity.response.SellerUpdateResponse;
import com.zayminmaw.lighthouse.repository.SellerRepository;
import com.zayminmaw.lighthouse.repository.UserRespository;
import com.zayminmaw.lighthouse.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SellerSignupRequest request){
        UserEntity userEntity = userRespository.findByEmail(request.getEmail());
        if(userEntity != null) return new ResponseEntity<>("Email already exist!", HttpStatus.UNPROCESSABLE_ENTITY);
        if(!request.getEmail().matches(regex)) return new ResponseEntity<>("Invalid email!",HttpStatus.UNPROCESSABLE_ENTITY);
        if(request.getPassword().length() < 4) return new ResponseEntity<>("Password have to be at least 4 words!",HttpStatus.UNPROCESSABLE_ENTITY);
        userEntity = new UserEntity();
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        userEntity.setJoinedDate(new Timestamp(System.currentTimeMillis()));
        userEntity = userRespository.save(userEntity);

        Seller seller = new Seller();
        seller.setName(request.getName());
        seller.setUserEntity(userEntity);
        seller = sellerRepository.save(seller);

        return ResponseEntity.ok(seller);
    }

    @PostMapping("/login")
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
        Seller seller = sellerRepository.findByUserId(userEntity.getId());
        if(seller == null) return new ResponseEntity<>("You are not seller!",HttpStatus.UNAUTHORIZED);
        String token = jwtTokenProvider.createToken("SELLER:"+userEntity.getEmail(),"SELLER");
        response.setStatusCode("200");
        response.setMessage("Login Successful!");
        response.setEmail(userEntity.getEmail());
        response.setToken(token);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody SellerUpdateRequest request, @AuthenticationPrincipal UserDetails userDetails){
        String authorities = userDetails.getAuthorities().iterator().next().toString();
        if(!authorities.equals("SELLER")) return new ResponseEntity<>("You are not seller!",HttpStatus.FORBIDDEN);
        Seller seller = sellerRepository.getOne(request.getId());
        SellerUpdateResponse response = new SellerUpdateResponse();
        if(seller == null) return new ResponseEntity<>("User doesn't exist!",HttpStatus.UNPROCESSABLE_ENTITY);
        if(request.getName().equals("")) return new ResponseEntity<>("Field can't be empty!",HttpStatus.UNPROCESSABLE_ENTITY);
        seller.setName(request.getName());
        seller = sellerRepository.save(seller);
        response.setMessage("Updated!");
        response.setStatusCode("200");
        response.setUpdatedSellerUser(seller);
        return ResponseEntity.ok(response);
    }
}
