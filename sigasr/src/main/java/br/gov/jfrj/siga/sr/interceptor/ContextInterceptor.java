package br.gov.jfrj.siga.sr.interceptor;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.util.jpa.extra.ParameterLoaderInterceptor;
import br.gov.jfrj.siga.dp.dao.CpDao;
import br.gov.jfrj.siga.model.ContextoPersistencia;
import br.gov.jfrj.siga.model.dao.HibernateUtil;
import br.gov.jfrj.siga.sr.model.Sr;
import br.gov.jfrj.siga.vraptor.ParameterOptionalLoaderInterceptor;

@RequestScoped
@Intercepts(before = { ParameterLoaderInterceptor.class,
		ParameterOptionalLoaderInterceptor.class })
public class ContextInterceptor implements Interceptor {

	public ContextInterceptor(EntityManager em, Result result) throws Exception{
		ContextoPersistencia.setEntityManager(em);
		EntityTransaction t = em.getTransaction();
		CpDao.freeInstance();
		CpDao.getInstance((Session) em.getDelegate(), ((Session) em
				.getDelegate()).getSessionFactory().openStatelessSession());
		
		//Edson: não sei por que o HibernateUtil precisa de uma sessao. Os Dao's
				//que chamam essa classe já têm o objeto sessão 
		HibernateUtil.configurarHibernate((Session)em.getDelegate());
		
		Sr.getInstance().getConf().limparCacheSeNecessario();
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return false;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method,
			Object resourceInstance) throws InterceptionException {
		return;
	}

}