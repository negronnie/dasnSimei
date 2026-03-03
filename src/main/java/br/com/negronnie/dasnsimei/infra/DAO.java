package br.com.negronnie.dasnsimei.infra;

import jakarta.persistence.*;

import java.util.List;

public class DAO<Entidade> {

    private static EntityManagerFactory emf;
    private EntityManager em;
    private Class<Entidade> classe;

    static {
        try{
            emf = Persistence.createEntityManagerFactory("dasnSimei");
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erro ao Criar EMF", e);
        }
    }

    public DAO() {
        this(null);
    }

    public DAO(Class<Entidade> classe) {
        this.classe = classe;
        em = emf.createEntityManager();
    }

    public DAO<Entidade> startTransaction() {
        em.getTransaction().begin();
        return this;
    }

    public DAO<Entidade> endTransaction() {
        em.getTransaction().commit();
        return this;
    }

    public DAO<Entidade> incluir(Entidade entidade) {
        em.persist(entidade);
        return this;
    }

    public List<Entidade> listarTodos() {
        return this.listarTodos(300,0);
    }

    public Entidade obterPorId(Integer id) {
        return em.find(classe, id);
    }


    public List<Entidade> listarTodos(int limit, int offset) {
        if(classe == null){
            throw new UnsupportedOperationException("Classe nula!");
        }
        String jpql = "select e from " + classe.getName() + " e";
        TypedQuery<Entidade> query = em.createQuery(jpql, classe);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public void fechar() {
        em.close();
    }
}
