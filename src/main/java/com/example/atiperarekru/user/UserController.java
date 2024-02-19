package com.example.atiperarekru.user;

import com.example.atiperarekru.NoDataFoundException;
import com.example.atiperarekru.user.UserService.UserRepositoryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{username}/repository")
    public List<UserRepositoryDTO> getUserRepositoriesData(@PathVariable String username)
            throws URISyntaxException, NoDataFoundException, IOException, InterruptedException {
        return userService.getUserRepositoriesData(username);
    }

}