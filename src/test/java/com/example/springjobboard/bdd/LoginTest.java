package com.example.springjobboard.bdd;

import com.codeborne.selenide.Condition;
import com.example.springjobboard.model.users.User;
import com.example.springjobboard.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {

    private static UserRepository userRepository;
    private static User user;

    @BeforeAll
    public static void beforeAll(@Autowired UserRepository repository,
                            @Autowired PasswordEncoder passwordEncoder) {

        user = User.builder()
                .name("seleniumtest")
                .email("seleniumtest@mail.com")
                .password("seleniumtest")
                .isActive(true)
                .build();

        userRepository = repository;

        var userPersisted = userRepository.findByEmailEagerly(user.getEmail());
        if (userPersisted != null) {
            userRepository.delete(userPersisted);
        }

//        userPersisted = User.builder()
//                .name(user.getName())
//                .email(user.getEmail())
//                .password(passwordEncoder.encode(user.getPassword()))
//                .isActive(user.getIsActive())
//                .build();

//        userRepository.save(userPersisted);
    }

    @AfterAll
    public static void afterAll() {
        var userPersisted = userRepository.findByEmailEagerly(user.getEmail());
        if (userPersisted != null) {
            userRepository.delete(userPersisted);
        }
    }

    @Test
    @Order(1)
    public void registerPage() {

        open("http://localhost:8080/auth/register");

        $(By.xpath("//h3[text()='Registration']")).shouldBe(Condition.visible);

        $(By.xpath("//form//input[@id='name']")).val(user.getName());
        $(By.xpath("//form//input[@id='email']")).val(user.getUsername());
        $(By.xpath("//form//input[@id='password']")).val(user.getPassword());

        $(By.xpath("//form//button[@type='submit']")).click();

        // redirected to login page
        $(By.xpath("//form//input[@id='name']")).shouldNotBe(Condition.visible);
        $(By.xpath("//form//button[text()='Log in']")).shouldBe(Condition.visible);

        assertNotNull(userRepository.findByEmailEagerly(user.getEmail()));
    }

    @Test
    @Order(2)
    public void loginPageFailing() {

        open("http://localhost:8080/auth/login");

        $(By.xpath("//h3[text()='Log in']")).shouldBe(Condition.visible);

        $(By.xpath("//form//input[@id='username']")).val(user.getUsername() + "1"); // wrong username
        $(By.xpath("//form//input[@id='password']")).val(user.getPassword() + "1"); // wrong password

        $(By.xpath("//form//button[@type='submit']")).click();

        $(By.xpath("//p//span[text()='" + user.getName() + "']")).shouldNotBe(Condition.visible);
        $(By.xpath("//div//a[text()='Edit']")).shouldNotBe(Condition.visible);

        $(By.xpath("//form//input[@id='username']")).shouldBe(Condition.visible);
        $(By.xpath("//form//button[@type='submit']")).shouldBe(Condition.visible);
    }

    @Test
    @Order(3)
    public void loginPageSuccess() {

        open("http://localhost:8080/auth/login");

        $(By.xpath("//h3[contains(text(), 'Log in')]")).shouldBe(Condition.visible);

        $(By.xpath("//form//input[@id='username']")).val(user.getUsername());
        $(By.xpath("//form//input[@id='password']")).val(user.getPassword());

        $(By.xpath("//form//button[@type='submit']")).click();

        // redirected to 'my-page'
        $(By.xpath("//p//span[text()='" + user.getName() + "']")).shouldBe(Condition.visible);
        $(By.xpath("//p//span[text()='" + user.getEmail() + "']")).shouldBe(Condition.visible);
        $(By.xpath("//div//a[text()='Edit']")).shouldBe(Condition.visible);
    }
}
