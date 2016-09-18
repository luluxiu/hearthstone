package com.tigercel.hearthstone.service;

import com.tigercel.hearthstone.Constants;
import com.tigercel.hearthstone.model.HFUser;
import com.tigercel.hearthstone.model.support.UserRole;
import com.tigercel.hearthstone.repository.HFUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Created by somebody on 2016/8/2.
 *
 * "{\"meta\":{\"retCode\": 500,\"retInfo\":\"Error\",\"description\":\"\"}}";
 */
@Service
public class HFUserService implements UserDetailsService {

    @Autowired
    private HFUserRepository hfUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(HFUserService.class);


    /*************** for spring security **********************/
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        HFUser user = hfUserRepository.findByName(name);
        if (user == null) {
            throw new UsernameNotFoundException("user not found");
        }
        return createSpringUser(user);
    }

    private org.springframework.security.core.userdetails.User createSpringUser(HFUser user) {
        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                Collections.singleton(createAuthority(user)));
    }

    private GrantedAuthority createAuthority(HFUser user) {
        return new SimpleGrantedAuthority(user.getRole().toString());
    }

    /************** init admin user *******************/

    //@PostConstruct
    protected void initialize() {
        getSuperUser();
    }

    public HFUser getSuperUser(){
        HFUser user = hfUserRepository.findByName(Constants.DEFAULT_ADMIN_NAME);

        if(user == null) {
            user = createUser(new HFUser(Constants.DEFAULT_ADMIN_NAME, Constants.DEFAULT_ADMIN_PASSWORD,
                    UserRole.ROLE_ADMIN));
        }

        return user;
    }

    public HFUser createUser(HFUser user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return hfUserRepository.save(user);
    }
}



