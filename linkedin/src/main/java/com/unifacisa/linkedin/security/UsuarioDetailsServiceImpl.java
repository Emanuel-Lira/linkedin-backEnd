package com.unifacisa.linkedin.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.unifacisa.linkedin.user.User;
import com.unifacisa.linkedin.user.UserRepository;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepository;

    @Override
    public UsuarioDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        return new UsuarioDetails(usuario);
    }
}