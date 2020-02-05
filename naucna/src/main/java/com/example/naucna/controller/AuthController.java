package com.example.naucna.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.naucna.auth.JwtAuthenticationRequest;
import com.example.naucna.model.KorisnikDto;
import com.example.naucna.model.Role;
import com.example.naucna.model.User;
import com.example.naucna.model.UserTokenState;
import com.example.naucna.security.CustomUserDetailsService;
import com.example.naucna.security.TokenUtils;
import com.example.naucna.services.UserService;
import com.example.naucna.utils.Utils;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "auth")
public class AuthController {
	 @Autowired
	    public TokenUtils tokenUtils;

	    @Autowired
	    public AuthenticationManager manager;

	    @Autowired
	    public CustomUserDetailsService userDetailsService;

	    @Autowired
	    public UserService userService;

	    @RequestMapping(value="/login",method = RequestMethod.POST)
	    public ResponseEntity<UserTokenState> loginUser(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response, Device device, HttpServletRequest hr){
	        System.out.println("usao u metodu za logovanje");
	        final Authentication authentication = manager
	                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
	        System.out.println("Ulogovani korisnik je:");
	        System.out.println(authentication.getName());
	        SecurityContextHolder.getContext().setAuthentication(authentication);


	        User user =  (User) authentication.getPrincipal();
	        // VRATI DRUGI STATUS KOD
	        if(user == null) {
	            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	        }

	        String jwt = tokenUtils.generateToken(user.getUsername(), device);
	        int expiresIn = 3600;

	        return ResponseEntity.ok(new UserTokenState(jwt,expiresIn));
	    }

	    @RequestMapping(value="/logout", method = RequestMethod.POST)
	    public ResponseEntity<?> logOut(HttpServletRequest request, HttpServletResponse response){
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        System.out.println("dosao do logout");
	        if (authentication != null)
	            new SecurityContextLogoutHandler().logout(request, response, authentication);

	        return new ResponseEntity<>(HttpStatus.OK);
	    }

	    @RequestMapping(value = "/getUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	    public ResponseEntity<KorisnikDto> getUser(HttpServletRequest request){
	        System.out.println("usao u metodu za dobijanje ulogovanog");
	        String username = Utils.getUsernameFromRequest(request, tokenUtils);
	        KorisnikDto ulogovan = new KorisnikDto();
	        if(username != "" && username != null) {
	            User u = (User) userService.findUserByUsername(username);
	            for(Role role: u.getRoles()){
	                if(role.getName().equals("ROLE_ADMIN")){
	                	ulogovan.setRole("ROLE_ADMIN");
	                    break;
	                }
	                if(role.getName().equals("ROLE_UREDNIK")){
	                	ulogovan.setRole("ROLE_UREDNIK");
	                    break;
	                }
	                if(role.getName().equals("ROLE_RECENZENT")){
	                	ulogovan.setRole("ROLE_RECENZENT");
	                    break;
	                }
	            }
	            ulogovan.setUsername(u.getUsername());
	            return new ResponseEntity<KorisnikDto>(ulogovan, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }
	    }

}
