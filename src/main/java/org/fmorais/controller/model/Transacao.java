package org.fmorais.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;

public record Transacao(@JsonProperty(required = true) String valor, @JsonProperty(required = true) String tipo, @JsonProperty(required = true) String descricao) {
}
