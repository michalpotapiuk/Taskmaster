package com.example.engineer.config;


import com.example.engineer.Model.User.UserEntity;
import com.example.engineer.Model.User.UserRoleEntity;
import com.example.engineer.Repository.UserRepository;
import com.example.engineer.Repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class Util implements ApplicationRunner {


    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        loadData();
    }

    private void loadData() throws Exception {
        UserEntity user1 = new UserEntity();
        user1.setFirstName("Jan");
        user1.setLastName("Kowalski");
        user1.setEmail("superadmin@example.com");
        user1.setPassword("{bcrypt}$2a$10$Ruu5GtmSVkfLeuGfz/wHUuzflCcMbwJHSBo/.Wui0EM0KIM52Gs2S");


        UserEntity user2 = new UserEntity();
        user2.setFirstName("John");
        user2.setLastName("Abacki");
        user2.setEmail("john@example.com");
        user2.setPassword("{MD5}{AlZCLSQMMNLBS5mEO0kSem9V3mxplC6cTjWy9Kj/Gxs=}d9007147eb3a5f727b2665d647d36e35");


        UserEntity user3 = new UserEntity();
        user3.setFirstName("Marek");
        user3.setLastName("Zalewski");
        user3.setEmail("java_lover@example.com");
        user3.setPassword("{argon2}$argon2id$v=19$m=4096,t=3,p=1$YBBBwx+kfrNgczYDcLlWYA$LEPgdtfskoobyFtUWTMejaE5SBRyieHYbiE5ZmFKE7I");


        // Dodawanie ról użytkowników
        UserRoleEntity admin = new UserRoleEntity();
        admin.setName("ADMIN");
        userRoleRepository.save(admin);

        UserRoleEntity userRole = new UserRoleEntity();
        userRole.setName("USER");
        userRoleRepository.save(userRole);

        // Przypisywanie ról do użytkowników
        user1.setRoles(Set.of(admin));
        user2.setRoles(Set.of(userRole));
        user3.setRoles(Set.of(userRole));

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }
}
