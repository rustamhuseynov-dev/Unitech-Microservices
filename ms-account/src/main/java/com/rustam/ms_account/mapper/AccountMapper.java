package com.rustam.ms_account.mapper;

import com.rustam.ms_account.dao.entity.Account;
import com.rustam.ms_account.dto.response.AccountIncreaseResponse;
import com.rustam.ms_account.dto.response.AccountResponse;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING,
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = DateMapper.class
)
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "localDateTimeToString")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "localDateTimeToString")
    AccountResponse toDto(Account account);

    @Mapping(target = "increaseBalanceAt", source = "increaseBalanceAt", qualifiedByName = "localDateTimeToString")
    AccountIncreaseResponse toIncreaseResponse(Account account);


    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "longToLocalDateTime")
    @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "longToLocalDateTime")
    Account toEntity(AccountResponse accountResponse);
}
