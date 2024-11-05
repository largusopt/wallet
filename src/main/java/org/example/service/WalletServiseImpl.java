package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.WalletDto;
import org.example.dto.WalletRequestDto;
import org.example.enums.OperationType;
import org.example.exception.AmountNotEnoughtException;
import org.example.exception.NotFoundException;
import org.example.mapper.WalletMapper;
import org.example.model.Wallet;
import org.example.repository.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.example.exception.message.WalletMessageExceptions.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletServiseImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;

    @Override
    @Transactional
    public WalletDto makeOperation(WalletRequestDto walletRequestDto) {
        checkOperation(walletRequestDto.getOperationType());
        Wallet wallet = checkWalletById(walletRequestDto.getWalletId());
        BigDecimal currentAmount = wallet.getAmount();
        OperationType operation = OperationType.valueOf(walletRequestDto.getOperationType());
        switch (operation) {
            case DEPOSIT:
                wallet.setAmount(currentAmount.add(walletRequestDto.getAmount()));
                break;
            case WITHDRAW:
                if (currentAmount.compareTo(walletRequestDto.getAmount()) < 0) {
                    throw new AmountNotEnoughtException(NOT_ENOUGHT_MONEY);
                }
                wallet.setAmount(currentAmount.subtract(walletRequestDto.getAmount()));
                break;
        }
        walletRepository.save(wallet);

        return walletMapper.walletToDto(wallet);
    }

    @Override
    @Transactional
    public WalletDto getWalletById(UUID walletId) {
        return walletMapper.walletToDto(checkWalletById(walletId));
    }

    private void checkOperation(String operationType) {
        if (!OperationType.DEPOSIT.toString().equals(operationType) && !OperationType.WITHDRAW.toString().equals(operationType)) {
            log.error(OPERATION_NOT_FOUND);
            throw new NotFoundException(OPERATION_NOT_FOUND);
        }
    }

    private Wallet checkWalletById(UUID walletId) {
        Optional<Wallet> wallet = walletRepository.findByWalletId(walletId);
        if (wallet.isEmpty()) {
            log.error(NOT_EXISTS_WALET);
            throw new NotFoundException(NOT_EXISTS_WALET);
        }
        return wallet.get();
    }
}
