package com.jwt.jwt.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data  //@Getter, @Setter, @ToString, @RequiredArgsConstructor, @EqualsAndHashCode를 합친 종합선물세트
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String roles; //role이 여러개 될 수 있다(USER, ADMIN)

    // roles필드에 저장된 role 목록을 출력하여 List형태로 반환 (쉼표로 분리하여 리스트로 반환). 개별 role을 다루기 쉽게 하려고 만듦
    public List<String> getRoleList(){
        if(this.roles.length()>0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
