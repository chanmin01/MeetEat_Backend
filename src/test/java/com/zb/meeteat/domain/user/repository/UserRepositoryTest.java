package com.zb.meeteat.domain.user.repository;

import com.zb.meeteat.domain.user.entity.Role;
import com.zb.meeteat.domain.user.entity.SignupType;
import com.zb.meeteat.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("이메일로 사용자 조회 테스트")
    void findByEmailTest() {
        // given
        User user = User.builder()
                .email("test@example.com")
                .password("password123")
                .nickname("testuser")
                .role(Role.USER)
                .signupType(SignupType.EMAIL)
                .build();

        userRepository.save(user);

        // when
        User foundUser = userRepository.findByEmail("test@example.com")
                .orElseThrow(() -> new RuntimeException("User not found"));

        // then
        assertThat(foundUser.getEmail()).isEqualTo("test@example.com");
        assertThat(foundUser.getNickname()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 조회 시 예외 발생 테스트")
    void findByNonExistingEmailTest() {
        // when, then
        assertThatThrownBy(() -> userRepository.findByEmail("nonexistent@example.com")
                .orElseThrow(() -> new RuntimeException("User not found")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    @DisplayName("닉네임으로 사용자 조회 테스트")
    void findByNicknameTest() {
        // given
        User user = User.builder()
                .email("nickname@example.com")
                .password("password123")
                .nickname("uniqueNickname")
                .role(Role.USER)
                .signupType(SignupType.EMAIL)
                .build();

        userRepository.save(user);

        // when
        User foundUser = userRepository.findByNickname("uniqueNickname")
                .orElseThrow(() -> new RuntimeException("User not found"));

        // then
        assertThat(foundUser.getNickname()).isEqualTo("uniqueNickname");
        assertThat(foundUser.getEmail()).isEqualTo("nickname@example.com");
    }

    @Test
    @DisplayName("존재하지 않는 닉네임으로 조회 시 예외 발생 테스트")
    void findByNonExistingNicknameTest() {
        // when, then
        assertThatThrownBy(() -> userRepository.findByNickname("nonexistentNickname")
                .orElseThrow(() -> new RuntimeException("User not found")))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");

    }

    @Test
    @DisplayName("사용자 저장 테스트")
    void savedUserTest() {
        // given
        User user = User.builder()
                .email("save@example.com")
                .password("password123")
                .nickname("savedUser")
                .role(Role.USER)
                .signupType(SignupType.EMAIL)
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("save@example.com");
        assertThat(savedUser.getNickname()).isEqualTo("savedUser");

    }

    @Test
    @DisplayName("이메일 존재 여부 확인 테스트")
    void existsByEmailTest() {
        // given
        User user = User.builder()
                .email("exists@example.com")
                .password("password123")
                .nickname("existsUser")
                .role(Role.USER)
                .signupType(SignupType.EMAIL)
                .build();

        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByEmail("exists@example.com");

        // then
        assertThat(exists).isTrue();

    }

    @Test
    @DisplayName("닉네임 존재 여부 확인 테스트")
    void existsByNicknameTest() {
        // given
        User user = User.builder()
                .email("nicknameexists@example.com")
                .password("password123")
                .nickname("nicknameExists")
                .role(Role.USER)
                .signupType(SignupType.EMAIL)
                .build();

        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByNickname("nicknameExists");

        // then
        assertThat(exists).isTrue();

    }
}




