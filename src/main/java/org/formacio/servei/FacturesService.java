package org.formacio.servei;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.formacio.domain.LiniaFactura;
import org.formacio.domain.Factura;
import org.formacio.repositori.FacturesRepositori;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FacturesService {

	@PersistenceContext 
	private EntityManager em;
	
	@Autowired
	FacturesRepositori repositori;
	
	@Autowired
	FidalitzacioService fidalitzacioService;
	/*
	 * Aquest metode ha de carregar la factura amb id idFactura i afegir una nova linia amb les dades
	 * passades (producte i totalProducte)
	 * 
	 * S'ha de retornar la factura modificada
	 * 
	 * Per implementar aquest metode necessitareu una referencia (dependencia) a FacturesRepositori
	 */
	public Factura afegirProducte (long idFactura, String producte, int totalProducte) {
		
		Optional<Factura> fact = repositori.findById(idFactura);
		
		if(fact.isPresent()) {
			LiniaFactura linia = new LiniaFactura();
			linia.setProducte(producte);
			linia.setTotal(totalProducte);
			fact.get().getLinies().add(linia);
			repositori.save(fact.get());
			
			notificarRegal(fact.get());
		}
		return fact.get();
	}
	
	public void notificarRegal(Factura factura) {
		int lineasDeFactura = 4;
		if(factura.getLinies().size() >= lineasDeFactura) {
			fidalitzacioService.notificaRegal(factura.getClient().getEmail());
		}
	}
}
