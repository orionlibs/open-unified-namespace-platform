package io.github.orionlibs.core.user;

import io.github.orionlibs.core.user.model.UserDAO;
import io.github.orionlibs.core.user.model.UserModel;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService
{
    @Autowired
    private UserDAO dao;


    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        UserModel user = dao.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.builder()
                        .username(user.getUsername())
                        .password(user.getPassword())
                        .authorities(user.getAuthorities())
                        .accountExpired(false)
                        .accountLocked(false)
                        .credentialsExpired(false)
                        .disabled(!user.isEnabled())
                        .build();
    }


    @Transactional(readOnly = true)
    public UserModel loadUserAsModelByUsername(String username) throws UsernameNotFoundException
    {
        return dao.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }


    @Transactional(readOnly = true)
    public UserModel loadUserByUserID(String userID) throws UserIDNotFoundException
    {
        return loadUserByUserID(UUID.fromString(userID));
    }


    @Transactional(readOnly = true)
    public UserModel loadUserByUserID(UUID userID) throws UserIDNotFoundException
    {
        return dao.findByUserID(userID)
                        .orElseThrow(() -> new UserIDNotFoundException("User not found"));
    }


    @Transactional
    public UserModel saveUser(UserModel user)
    {
        return dao.save(user);
    }


    @Transactional
    public void deleteAll()
    {
        dao.deleteAll();
    }
}
