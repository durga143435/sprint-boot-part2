package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ChangePasswordRequest;
import com.codewithmosh.store.dtos.RegisterUserRequest;
import com.codewithmosh.store.dtos.UserDto;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

//    @RequestMapping("/users")
//    @GetMapping("/users")
    @GetMapping
    public Iterable<UserDto> getAllUsers(
            @RequestHeader(name= "x-auth-token", required = false) String authToken,
            @RequestParam(required=false, defaultValue="name", name="sort") String sort
    ){
       /* return userRepository.findAll().stream()
                .map(user -> new UserDto(user.getId(), user.getName(), user.getEmail())).toList();*/

       /* return userRepository.findAll().stream()
                .map(user -> userMapper.toDto(user)).toList();*/

        //we can also use method reference wherever we have lambda expressions

        System.out.println(authToken);

        if(!Set.of("name","email").contains(sort))
            sort = "name";
        return userRepository.findAll(Sort.by(sort).descending()).stream()
                .map(userMapper::toDto).toList();
    }

//    @GetMapping("users/{id}")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id){
        User user = userRepository.findById(id).orElse(null);
//        UserDto userDto = new UserDto(user.getId(), user.getName(), user.getEmail());
        UserDto userDto = userMapper.toDto(user);

        if(user == null)
//            return new ResponseEntity<>(HttpStatusCode.valueOf(404));
            return ResponseEntity.notFound().build();
        else
//            return new ResponseEntity<>(user, HttpStatusCode.valueOf(200));
            return ResponseEntity.ok().body(userDto);
    }



    @PostMapping
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
    ){
        if(userRepository.existsByEmail(request.getEmail()))
            return ResponseEntity.badRequest().body(
                    Map.of("email", "Email is already exists")
            );
        User user = userRepository.save(userMapper.toEntity(request));

        UserDto userDto= userMapper.toDto(user);
        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(user).toUri();
    return ResponseEntity.created(uri).body(userDto);
    }


    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request
    ){
        User user = userRepository.findById(id).orElse(null);

        if(user == null)
            return ResponseEntity.notFound().build();

        /*user.setName(request.getName());
        user.setEmail(request.getEmail());*/

        userMapper.updateUserEntity(request, user);

        userRepository.save(user);
        return ResponseEntity.ok(userMapper.toDto(user));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id") Long id){
        var user = userRepository.findById(id).orElse(null);

        if(user == null)
           return ResponseEntity.notFound().build();

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request){

        User user = userRepository.findById(id).orElse(null);

        if(user == null)
            return ResponseEntity.notFound().build();

        if(!user.getPassword().equals(request.getOldPassword()))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
        return ResponseEntity.noContent().build();

    }
}
