package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.example.dto.WalletDto;
import org.example.dto.WalletRequestDto;
import org.example.exception.message.WalletMessageExceptions;
import org.example.model.Wallet;
import org.example.repository.WalletRepository;
import org.example.utils.IntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
class WalletControllerTest extends IntegrationTest {
    private static String NOT_EXISTS_UUID = "bbec5693-b5b4-4c11-b9f1-71237806e06d";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WalletRepository walletRepository;
    private WalletDto walletDto;
    private WalletRequestDto walletRequestDto;
    private Wallet wallet;

    @BeforeEach
    void init() {
        // wallet = new Wallet(UUID.fromString("ef9b2598-c0ea-4254-b203-65c6c7e7b202"),new BigDecimal(1000));
        wallet = walletRepository.save(new Wallet(UUID.fromString("ef9b2598-c0ea-4254-b203-65c6c7e7b202"), new BigDecimal(1000)));
        walletDto = new WalletDto(new BigDecimal(1000));
        walletRequestDto = new WalletRequestDto(wallet.getWalletId(), "DEPOSIT", new BigDecimal(500));
    }

    @AfterEach
    void clear() {
        walletRepository.deleteAll();
    }

    @Test
    @DisplayName("Получение кошелька по Id успешно")
    @SneakyThrows
    void whenGetWalletByIdIsSuccessful() {
        String actual = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wallets/{walletId}", wallet.getWalletId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(objectMapper.writeValueAsString(walletDto));
    }

    @Test
    @DisplayName("Получение кошелька по Id не успешно.")
    @SneakyThrows
    void whenGetWalletByIdIsNotSuccessful() {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/wallets/{walletId}", NOT_EXISTS_UUID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(WalletMessageExceptions.NOT_EXISTS_WALET));
    }

    @Test
    @DisplayName("Добавление денег на кошелек успешно.")
    @SneakyThrows
    void whenDepositIsSuccess() {
        walletDto.setAmount(new BigDecimal(1500));
        String actual = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                        .content(objectMapper.writeValueAsString(walletRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(objectMapper.writeValueAsString(walletDto));
    }

    @Test
    @DisplayName("Удаление денег из кошелека успешно.")
    @SneakyThrows
    void whenWithdrawIsSuccess() {
        walletDto.setAmount(new BigDecimal(500));
        walletRequestDto.setOperationType("WITHDRAW");
        String actual = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/wallet")
                        .content(objectMapper.writeValueAsString(walletRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertThat(actual).isEqualTo(objectMapper.writeValueAsString(walletDto));
    }

}