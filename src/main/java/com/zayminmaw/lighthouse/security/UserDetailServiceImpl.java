package com.zayminmaw.lighthouse.security;

import com.zayminmaw.lighthouse.entity.Buyer;
import com.zayminmaw.lighthouse.entity.Seller;
import com.zayminmaw.lighthouse.entity.UserEntity;
import com.zayminmaw.lighthouse.repository.BuyerRepository;
import com.zayminmaw.lighthouse.repository.SellerRepository;
import com.zayminmaw.lighthouse.repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRespository userRespository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Autowired
    private SellerRepository sellerRepository;
    @Override
    public UserDetails loadUserByUsername(String data) throws UsernameNotFoundException {
        String[] roleAndEmail = data.split(":");
        String role = roleAndEmail[0];
        String email = roleAndEmail[1];
        UserEntity userEntity = userRespository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(email);
        if (role.equals("SELLER")){
            Seller seller = sellerRepository.findByUserId(userEntity.getId());
            if(seller == null) throw new UsernameNotFoundException(email);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority((role)));
            return new User(seller.getUserEntity().getEmail(),seller.getUserEntity().getPassword(),authorities);
        }else{
            Buyer buyer = buyerRepository.findByUserId(userEntity.getId());
            if(buyer == null) throw new UsernameNotFoundException(email);
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority((role)));
            return new User(buyer.getUserEntity().getEmail(),buyer.getUserEntity().getPassword(),authorities);
        }
    }
}
