package by.rymtsou.service;

import by.rymtsou.model.Security;
import by.rymtsou.model.User;
import by.rymtsou.model.dto.RegistrationRequestDto;
import by.rymtsou.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Optional;

@Service
public class SecurityService {

    public final SecurityRepository securityRepository;

    @Autowired
    public SecurityService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public Optional<Security> getSecurityById(Long id) {
        return securityRepository.getSecurityById(id);
    }

    public Optional<User> registration(RegistrationRequestDto requestDto) throws SQLException {
        return securityRepository.registration(requestDto);
    }

    public Optional<Security> updateSecurity(Security security) {
        Boolean isUserUpdated = securityRepository.updateSecurity(security);
        if (isUserUpdated) {
            return getSecurityById(security.getId());
        } else {
            return Optional.empty();
        }
    }

    public Optional<Security> deleteSecurity(Long id) {
        Boolean isUserDeleted = securityRepository.deleteSecurity(id);
        if (isUserDeleted) {
            return getSecurityById(id);
        } else {
            return Optional.empty();
        }
    }
}
