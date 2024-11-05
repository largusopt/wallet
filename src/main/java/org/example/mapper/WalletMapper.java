package org.example.mapper;

import org.example.dto.WalletDto;
import org.example.model.Wallet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WalletMapper {
     WalletDto walletToDto(Wallet wallet);
}
