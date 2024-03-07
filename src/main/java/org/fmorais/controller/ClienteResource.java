package org.fmorais.controller;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import org.fmorais.controller.model.Extrato;
import org.fmorais.controller.model.Saldo;
import org.fmorais.controller.model.Transacao;
import org.fmorais.infrastructure.persistence.model.Cliente;
import org.fmorais.model.cliente.ClienteService;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
@Path("/clientes")
public class ClienteResource {

    @Inject
    ClienteService clienteService;

    @POST
    @Path("{id}/transacoes")
    @Transactional
    public Saldo transacoes(@PathParam("id") String id, Transacao transacao) {
        return clienteService.novaTransacao(id, transacao);
    }

    @GET
    @Path("{id}/extrato")
    public Extrato extrato(@PathParam("id") String id) {
        return clienteService.obterExtradoPorCliente(id);
    }
}
