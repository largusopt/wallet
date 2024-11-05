package org.example.service;

import org.example.dto.WalletDto;
import org.example.dto.WalletRequestDto;

import java.util.UUID;

public interface WalletService {
    WalletDto makeOperation(WalletRequestDto walletRequestDto);

    WalletDto getWalletById(UUID walletId);
}
