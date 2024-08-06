package jwt.example.jwt_tutorial.repository;

import jwt.example.jwt_tutorial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


//JpaRepository를 상속했기에 @Repository 어노테이션이 없어도 됨
public interface UserRepository extends JpaRepository<User, Integer> {
    public User findByUsername(String username); //Jpa Query methods
}
