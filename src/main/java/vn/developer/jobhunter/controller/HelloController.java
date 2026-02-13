package vn.developer.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.developer.jobhunter.domain.RestResponse;

// import vn.developer.jobhunter.util.error.IdInvalidException;

@RestController
public class HelloController {

    @GetMapping("/")
    public ResponseEntity<Object> home() {
    return ResponseEntity.ok("Hello World");
}
}
