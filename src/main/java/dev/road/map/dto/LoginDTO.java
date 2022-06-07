package dev.road.map.dto;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import dev.road.map.domain.user.Field;
import dev.road.map.domain.user.Provider;
import dev.road.map.domain.user.Role;
import dev.road.map.domain.user.Type;
import dev.road.map.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Component
public class LoginDTO {

    private String password;
    private String email;
}
