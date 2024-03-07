package org.fmorais.model.cliente;

import io.smallrye.config.common.utils.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.NotFoundException;
import org.fmorais.controller.model.Extrato;
import org.fmorais.controller.model.Saldo;
import org.fmorais.controller.model.Transacao;
import org.fmorais.infrastructure.persistence.model.Cliente;
import org.hibernate.exception.GenericJDBCException;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ClienteService {

    @PersistenceContext
    EntityManager em;

    private final List<String> tipoTransacao = List.of("D", "d", "C", "c");

    public Saldo novaTransacao(String idCliente, Transacao transacao) {
        if (!tipoTransacao.contains(transacao.tipo()) || "".equalsIgnoreCase(transacao.descricao())) throw new ClientErrorException(422);
        if (!StringUtil.isNumeric(transacao.valor()) || transacao.valor().contains(".")) throw new ClientErrorException(400);
        var cliente = findCliente(idCliente);
        var valor = new BigInteger(transacao.valor());
        if (valor.compareTo(BigInteger.ZERO) < 0) throw new ClientErrorException(400);
        if (transacao.tipo().equalsIgnoreCase("d") && cliente.getSaldo().subtract(valor).compareTo(cliente.getLimite().multiply(new BigInteger("-1"))) < 0) {
            throw new ClientErrorException(422);
        }
        if (transacao.descricao() == null || transacao.descricao().length() > 10) throw new ClientErrorException(422);
        cliente.setSaldo(cliente.getSaldo().subtract(valor));
        persistTransacao(cliente, transacao);
        em.refresh(cliente);
        return new Saldo(cliente.getSaldo(), cliente.getLimite());
    }

    @Transactional
    public Cliente findCliente(String idCliente) {
        if (!StringUtil.isNumeric(idCliente)) {
            throw new ClientErrorException(404);
        }
        var query = em.createNativeQuery("select * from Cliente where id = ? for update", Cliente.class);
        query.setParameter(1, Long.parseLong(idCliente));
        try {
            return (Cliente) query.getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException();
        }
    }

    @Transactional
    public void persistTransacao(Cliente cliente, Transacao transacao) {
        try {
            em.persist(org.fmorais.infrastructure.persistence.model.Transacao.fromTransacaoJson(cliente, transacao));
        } catch (GenericJDBCException e) {
            throw new ClientErrorException(422);
        }
    }


    public Extrato obterExtradoPorCliente(String idCliente) {
        var cliente = findCliente(idCliente);
        var query = em.createQuery("from Transacao where cliente = :cliente order by criadoEm desc ",
                org.fmorais.infrastructure.persistence.model.Transacao.class);
        query.setMaxResults(10);
        query.setParameter("cliente", cliente);
        var transacoes = query.getResultList();
        return new Extrato(new Extrato.Saldo(cliente.getSaldo(), ZonedDateTime.now(),  cliente.getLimite()),
                transacoes.stream().map(org.fmorais.infrastructure.persistence.model.Transacao::toTransacaoModel).collect(Collectors.toList()));
    }
}
