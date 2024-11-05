package org.example.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.WalletDto;
import org.example.dto.WalletRequestDto;
import org.example.service.WalletService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class WalletController {
    private final WalletService walletService;

    @PostMapping("/wallet")
    public WalletDto makeOperation(@Valid @RequestBody WalletRequestDto walletRequestDto) {
        return walletService.makeOperation(walletRequestDto);
    }

    @GetMapping("/wallets/{walletId}")
    public WalletDto getWalletById(@PathVariable("walletId") UUID walletId) {
        return walletService.getWalletById(walletId);
    }
}
