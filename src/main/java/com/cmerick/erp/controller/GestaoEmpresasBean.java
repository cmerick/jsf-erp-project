package com.cmerick.erp.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Arrays;

import javax.faces.convert.Converter;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;


import com.cmerick.erp.model.Empresa;
import com.cmerick.erp.model.RamoAtividade;
import com.cmerick.erp.model.TipoEmpresa;
import com.cmerick.erp.repository.Empresas;
import com.cmerick.erp.repository.RamoAtividades;
import com.cmerick.erp.service.CadastroEmpresaService;
import com.cmerick.erp.util.FacesMessages;

@Named
@ViewScoped
public class GestaoEmpresasBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private Empresas empresas;
    
    @Inject
    private FacesMessages messages;
    
    @Inject
    private RamoAtividades ramoAtividades;
    
    @Inject
    private CadastroEmpresaService cadastroEmpresaService;
    
    private List<Empresa> listaEmpresas;
    
    private String termoPesquisa;
    
    private Converter ramoAtividadeConverter;
    
    private Empresa empresa;
    
    public void prepararNovaEmpresa() {
        empresa = new Empresa();
    }
    public void prepararEdicao() {
		ramoAtividadeConverter = new RamoAtividadeConverter(Arrays.asList(empresa.getRamoAtividade()));
	}
    
    public void salvar() {
    	try {
    		cadastroEmpresaService.salvar(empresa);
            
    		updateList();
            
            messages.info("Empresa salva com sucesso!");
		} catch (Exception e) {
			// TODO: handle exception
			messages.info(e.getMessage());
		}
        
        
    }
    
    public void apagarEmpresa() {
    	cadastroEmpresaService.excluir(empresa);
    	
    	empresa = null;
    	
    	updateList();
    	
        messages.info("Empresa excluida com sucesso!");
    	
    }
    
    public void pesquisar() {
        listaEmpresas = empresas.pesquisar(termoPesquisa);
        
        if (listaEmpresas.isEmpty()) {
            messages.info("Sua consulta não retornou registros.");
        }
    }
    
    public void todasEmpresas() {
        listaEmpresas = empresas.findAll();
    }
    
    public List<RamoAtividade> completarRamoAtividade(String termo) {
        List<RamoAtividade> listaRamoAtividades = ramoAtividades.pesquisar(termo);
        
        ramoAtividadeConverter = new RamoAtividadeConverter(listaRamoAtividades);
        
        return listaRamoAtividades;
    }
    
    private boolean jaHouvePesquisa() {
        return termoPesquisa != null && !"".equals(termoPesquisa);
    }
    
    public void updateList() {
    	if (jaHouvePesquisa()) {
            pesquisar();
        } else {
            todasEmpresas();
        }
    }
    
    public List<Empresa> getListaEmpresas() {
        return listaEmpresas;
    }
    
    public String getTermoPesquisa() {
        return termoPesquisa;
    }
    
    public void setTermoPesquisa(String termoPesquisa) {
        this.termoPesquisa = termoPesquisa;
    }
    
    public TipoEmpresa[] getTiposEmpresa() {
        return TipoEmpresa.values();
    }
    
    public Converter getRamoAtividadeConverter() {
        return ramoAtividadeConverter;
    }
    
    public Empresa getEmpresa() {
        return empresa;
    }
    
    public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
    
    public boolean isEmpresaSelecionada() {
    	return empresa != null && empresa.getId() != null;
    }
    
    
    
    
}