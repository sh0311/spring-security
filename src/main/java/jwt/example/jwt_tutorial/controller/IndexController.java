package jwt.example.jwt_tutorial.controller;

import jwt.example.jwt_tutorial.model.User;
import jwt.example.jwt_tutorial.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // localhost:8080/
    // localhost:8080
    @GetMapping({"","/"})
    public @ResponseBody String index() {
        return "index";
    }

    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    // 스프링시큐리티가 해당 주소를 낚아채버림. 저절로 로그인화면으로 가게됨 -> Security Config 파일 작성 후 낚아채지 않게 됨
    @GetMapping("/loginForm")
    public String login(){
        return "loginForm";
    }

    @GetMapping("/joinForm")   //회원가입 할 수 있는 페이지로 간다
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){  // 메소드에서 반환되는 값을 HTTP 응답 본문에 직접 쓰도록 지시
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword=user.getPassword();
        String encPassword=bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); //회원가입은 잘 됨. 비밀번호 1234 -> 시큐리티로 로그인 할 수 없다. 패스워드가 암호화가 안되었기 때문!
        return "redirect:/loginForm";
    }

    @Secured("Role_ADMIN") //권한이 Admin이어야 접근 가능. 하나만 걸고 싶을 때 주로 사용
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")  //여러개 걸고 싶을 때 주로 사용
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터정보";
    }
}
