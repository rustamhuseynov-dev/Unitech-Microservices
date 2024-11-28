package com.rustam.unitech.util;

import com.rustam.unitech.exception.custom.InvalidUUIDFormatException;
import com.rustam.unitech.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Slf4j
public class UtilService {

    UserRepository userRepository;

    public UUID convertToUUID(String id) {
        try {
            log.info("id {}",id);
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new InvalidUUIDFormatException("Invalid UUID format for ID: " + id, e);
        }
    }
}
