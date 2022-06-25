package project.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.dao.KorisnikDAO;
import project.forms.KorisnikForm;
import project.validator.KorisnikValidator;

@Controller
public class MainController {

	@Autowired
	private KorisnikDAO korisnikDAO;

	@Autowired
	private KorisnikValidator korisnikValidator;

	@InitBinder
	protected void initBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		
		if(target == null) {
			return;
		}

		if(target.getClass() == KorisnikForm.class) {
			dataBinder.setValidator(korisnikValidator);
		}
	}

	@RequestMapping(value = {"/", "/welcome"}, method = RequestMethod.GET)
	public String welcomePage(Model model) {
		model.addAttribute("title", "Dobro došli");
		return "welcomePage";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Model model) {
		return "loginPage";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String registerPage(Model model) {
		KorisnikForm form = new KorisnikForm();
		model.addAttribute("korisnikForm", form);
		return "registerPage";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String saveRegisterOsoba(Model model, @ModelAttribute("korisnikForm") @Validated KorisnikForm form, 
															BindingResult result, final RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			return "registerPage";
		}

		try {
			korisnikDAO.createKorisnik(form);
		} catch(Exception e) {
			model.addAttribute("errorMessage", "Error: " + e.getMessage());
			return "registerPage";
		}

		return "loginPage";
	}

	@RequestMapping(value = "/preusmjeriProfil", method = RequestMethod.GET)
	public String preusmjeriProfil(Model model, Principal principal) {
		return "redirect:/korisnik/profil";
	}
}