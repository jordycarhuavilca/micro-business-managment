package com.hotelpe.HotelPe_Backend.service.impl;

import com.hotelpe.HotelPe_Backend.client.AutenticacionClient;
import com.hotelpe.HotelPe_Backend.config.SqsMessageProducer;
import com.hotelpe.HotelPe_Backend.dto.*;
import com.hotelpe.HotelPe_Backend.entity.User;
import com.hotelpe.HotelPe_Backend.exception.OurException;
import com.hotelpe.HotelPe_Backend.repo.UserRepository;
import com.hotelpe.HotelPe_Backend.service.interfac.IUserService;
import com.hotelpe.HotelPe_Backend.utils.JWTUtils;
import com.hotelpe.HotelPe_Backend.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService implements IUserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    AutenticacionClient autenticacionClient;
    @Autowired
    private SqsMessageProducer producer;

    @Override
    public Response register(UserDTO user) {
        Response response = new Response();
        try {
            if (user.getRole() == null || user.getRole().isBlank()) {
                user.setRole("USER");
            }
            log.info("register.sendingMessage ");
            Map<String, Object> headers = new HashMap<>();
            //headers.put("Message-Type", MessageType.ORDER.name());
            headers.put("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            producer.send(user,headers);
            //ResponseEntity<RegisterResponseDto> responseEntity = autenticacionClient.register(user);
            log.info("register.sentMessage");
            /*if (responseEntity.getStatusCode().is2xxSuccessful()){
                RegisterResponseDto registerResponseDto = responseEntity.getBody();
                response.setStatusCode(registerResponseDto.getStatusCode());
                return response;
            }*/
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            return response;
        } catch (OurException e) {
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            log.info("register.error " + e );
            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.setMessage("Se produjo un error durante el registro de usuario " + e.getMessage());

        }
        return response;
    }

    @Override
    public Response login(LoginRequest loginRequest) {

        Response response = new Response();

        try {
            ResponseEntity<VerifyRegistrationResponseDto> responseEntity = autenticacionClient.login(loginRequest);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                VerifyRegistrationResponseDto registerResponseDto = responseEntity.getBody();
                response.setStatusCode(HttpStatus.SC_OK);
                response.setMessage(registerResponseDto.getMessage());
                response.setToken(registerResponseDto.getToken());
                response.setRole(registerResponseDto.getRole());
                response.setExpirationTime(registerResponseDto.getExpirationTime());
                return response;
            }
            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            response.setMessage("Failed");
            return response;

        } catch (OurException e) {
            response.setStatusCode(HttpStatus.SC_NOT_FOUND);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
            response.setMessage("Se produjo un error durante el inicio de sesion de usuario " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {

        Response response = new Response();
        try {
            List<User> userList = userRepository.findAll();
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userList);
            response.setStatusCode(HttpStatus.SC_OK);
            response.setMessage("Exitoso");
            response.setUserList(userDTOList);

        } catch (Exception e) {
            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.setMessage("Error al obtener todos los usuarios" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserBookingHistory(String userId) {

        Response response = new Response();


        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("Usuario no encontrado"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTOPlusUserBookingsAndRoom(user);
            response.setStatusCode(HttpStatus.SC_OK);
            response.setMessage("Exitoso");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(HttpStatus.SC_NOT_FOUND);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.setMessage("Error al obtener todos los usuarios " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteUser(String userId) {

        Response response = new Response();

        try {
            userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("Usuario no encontrado"));
            userRepository.deleteById(Long.valueOf(userId));
            response.setStatusCode(HttpStatus.SC_OK);
            response.setMessage("Exitoso");

        } catch (OurException e) {
            response.setStatusCode(HttpStatus.SC_NOT_FOUND);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.setMessage("Error al obtener todos los usuarios " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {

        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new OurException("Usuario no encontrado"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(HttpStatus.SC_OK);
            response.setMessage("Exitoso");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(HttpStatus.SC_NOT_FOUND);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.setMessage("Error al obtener todos los usuarios " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {

        Response response = new Response();

        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new OurException("Usuario no encontrado"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);
            response.setStatusCode(HttpStatus.SC_OK);
            response.setMessage("Exitoso");
            response.setUser(userDTO);

        } catch (OurException e) {
            response.setStatusCode(HttpStatus.SC_NOT_FOUND);
            response.setMessage(e.getMessage());

        } catch (Exception e) {

            response.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            response.setMessage("Error al obtener todos los usuarios " + e.getMessage());
        }
        return response;
    }

    public Response verifyRegistration(VerifyRegistrationDto verifyRegistrationDto){
        Response response = new Response();
        log.info("verifyRegistration.init " + verifyRegistrationDto.toString());

        ResponseEntity<VerifyRegistrationResponseDto> responseEntity = autenticacionClient.verifyRegistration(verifyRegistrationDto);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            VerifyRegistrationResponseDto registerResponseDto = responseEntity.getBody();
            response.setStatusCode(HttpStatus.SC_OK);
            response.setMessage(registerResponseDto.getMessage());
            response.setToken(registerResponseDto.getToken());
            response.setRole(registerResponseDto.getRole());
            response.setExpirationTime(registerResponseDto.getExpirationTime());
            return response;
        }
        response.setStatusCode(HttpStatus.SC_BAD_REQUEST);
        response.setMessage("Failed");
        return response;
    }
}
