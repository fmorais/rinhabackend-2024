package org.fmorais.controller.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record Extrato(Saldo saldo, List<Transacao> ultimas_transacoes) {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Saldo(BigInteger total, ZonedDateTime dataExtrato, BigInteger limite) {}

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public record Transacao(BigInteger valor, String tipo, String descricao, ZonedDateTime realizadaEm) {}
}
