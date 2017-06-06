package com.algaworks.brewer.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.brewer.model.Usuario;

@Controller
public class UsuariosController {
	
	@RequestMapping("/usuarios/novo")
	public String novo(Usuario usuario) {
		return "usuario/CadastroUsuario";
	}
	
	@RequestMapping(value = "/usuarios/novo", method = RequestMethod.POST)
	public String cadastrar(@Valid Usuario usuario, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return novo(usuario);
		}
		
		// addFlashAttribute - mantem atributos mesmo após um redirect
		redirectAttributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso!");
		return "redirect:/usuarios/novo";
	}

}
