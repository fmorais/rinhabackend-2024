package org.fmorais.infrastructure.persistence.model;

import jakarta.persistence.*;
import org.fmorais.controller.model.Extrato;

import java.math.BigInteger;
import java.time.ZonedDateTime;

@Entity
@Table(name = "transacao")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente")
    private Cliente cliente;

    private String tipo;
    private BigInteger valor;


    private String descricao;

    @Column(name = "criado_em")
    private ZonedDateTime criadoEm;

    public static Transacao fromTransacaoJson(Cliente cliente, org.fmorais.controller.model.Transacao transacao) {
        var novaTransacao = new Transacao();
        novaTransacao.setCliente(cliente);
        novaTransacao.setTipo(transacao.tipo().toLowerCase());
        novaTransacao.setValor(new BigInteger(transacao.valor()));
        novaTransacao.setDescricao(transacao.descricao());
        novaTransacao.setCriadoEm(ZonedDateTime.now());
        return novaTransacao;
    }

    public Extrato.Transacao toTransacaoModel() {
        return new Extrato.Transacao(valor, tipo, descricao, criadoEm);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigInteger getValor() {
        return valor;
    }

    public void setValor(BigInteger valor) {
        this.valor = valor;
    }

    public ZonedDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(ZonedDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
