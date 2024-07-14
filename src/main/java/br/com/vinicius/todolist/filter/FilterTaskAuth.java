package br.com.vinicius.todolist.filter;

import java.io.IOException;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.vinicius.todolist.user.model.User;
import br.com.vinicius.todolist.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        var servletPath = request.getServletPath();

        if (servletPath.startsWith("/task/")){
            
            var authorization = request.getHeader("Authorization");

            var authEncoded = authorization.substring("Basic".length()).trim();

            byte[] authDecode = Base64Coder.decode(authEncoded);

            var authString = new String(authDecode);
            String[] credentials = authString.split(":");
            String username = credentials[0];
            String password = credentials[1];

            Optional<User> user = userRepository.findByUsername(username);

            if (user.isPresent()){
                User userConfirm = user.get();

                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), userConfirm.getPassword());

                if (passwordVerify.verified){
                    request.setAttribute("idUser", userConfirm.getId());
                    filterChain.doFilter(request, response); 
                } else {
                    response.sendError(401);
                }
            } else {
                response.sendError(401);
            }
        } else {
            filterChain.doFilter(request, response);
        } 
    }
}
