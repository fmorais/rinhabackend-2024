package org.fmorais.infrastructure.persistence.model;

import jakarta.persistence.*;

import java.math.BigInteger;
import java.util.List;


@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    private BigInteger limite;

    private String nome;
    private BigInteger saldo;

    public Cliente(Integer id) {
        this.id = id;
    }

    public Cliente() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getLimite() {
        return limite;
    }

    public void setLimite(BigInteger limite) {
        this.limite = limite;
    }

    public BigInteger getSaldo() {
        return saldo;
    }

    public void setSaldo(BigInteger saldo) {
        this.saldo = saldo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
