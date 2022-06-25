package project.controllers;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import project.dao.*;
import project.entities.*;
import project.excel.ExcelExporter;
import project.forms.*;
import project.models.*;

@Controller
@RequestMapping("/korisnik")
public class KorisnikController {
	@Autowired
	private KorisnikDAO korisnikDAO;
	
	@Autowired
	private StrojDAO strojDAO;
	
	@Autowired
	private ServisDAO servisDAO;
	
	@Autowired
	private RadnikDAO radnikDAO;
	
	@Autowired
	private PosaoDAO posaoDAO;
	
	@Autowired
	private MaterijalDAO materijalDAO;
	
	@Autowired
	private PosaoStrojDAO posaoStrojDAO;
	
	@Autowired
	private PosaoRadnikDAO posaoRadnikDAO;
	
	@Autowired
	private PosaoMaterijalDAO posaoMaterijalDAO;

	@RequestMapping(value = {"/profil", "/"}, method = RequestMethod.GET)
	public String userInfo(Model model, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		List<PosaoENTITY> list = posaoDAO.getJobs(user.getID());
		List<Kalendar> list2 = new LinkedList<>();
		
		for(PosaoENTITY posao: list) {
			Kalendar p = new Kalendar(posao.getNaziv(), posao.getDatumPocetka().toString(), posao.getDatumKraja().toString());
			list2.add(p);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("list2", list2);
		
		return "profilPage";
	}
	
	@RequestMapping(value = "/podaci", method = RequestMethod.GET)
	public String data(Model model, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		KorisnikENTITY korisnik = korisnikDAO.findUserByID(user.getID());
		
		model.addAttribute("user", user);
		model.addAttribute("korisnik", korisnik);
		
		return "data";
	}
	
	@RequestMapping(value = "/podaci/uredi", method = RequestMethod.GET)
	public String urediDataPage(Model model, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		KorisnikENTITY korisnik = korisnikDAO.findUserByID(user.getID());
		
		model.addAttribute("user", user);
		model.addAttribute("korisnik", korisnik);
		
		return "updateDataPage";
	}
	
	@RequestMapping(value = "/podaci/uredi", method = RequestMethod.POST)
	public String urediData(Model model, Principal principal, @RequestParam("ime") String ime, 
			@RequestParam("prezime") String prezime, @RequestParam("korisnickoIme") String korisnickoIme, 
													@RequestParam("email") String email, @RequestParam("tvrtka") String tvrtka) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		KorisnikENTITY korisnik = korisnikDAO.findUserByID(user.getID());
		String error = "";
		List<KorisnikENTITY> korisnici = korisnikDAO.getUsers();
		korisnici.remove(korisnik);
		boolean b = false;
		
		for(KorisnikENTITY kor: korisnici) {
			if(kor.getKorisnickoIme().equals(korisnickoIme)) {
				b = true;
				error = "Navedeno korisničko ime je već zauzeto";
			} else if(kor.getEmail().equals(email)) {
				b = true;
				error = "Navedeni email je već zauzet";
			} else if(kor.getTvrtka().equals(tvrtka)) {
				b = true;
				error = "Navedena tvrtka je već zauzeta";
			}
			
			if(b) {
				break;
			}
		}
		
		if(b) {
			model.addAttribute("user", user);
			model.addAttribute("korisnik", korisnik);
			model.addAttribute("error", error);
			
			return "updateDataPage";
		}
		
		korisnikDAO.updateData(user.getID(), ime, prezime, korisnickoIme, email, tvrtka);
		
		return "redirect:/korisnik/podaci";
	}
	
	@RequestMapping(value = "/strojevi", method = RequestMethod.GET)
	public String machines(Model model, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		System.out.println(user.getID());
		List<StrojENTITY> machines = strojDAO.getMachines(user.getID());
		
		model.addAttribute("user", user);
		model.addAttribute("machines", machines);
		
		return "machinesListPage";
	}
	
	@RequestMapping(value = "/noviStroj", method = RequestMethod.GET)
	public String getNewMachinePage(Model model, Principal principal) {
//		StrojForm form = new StrojForm();
//		model.addAttribute("strojForm", form);
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		model.addAttribute("user", user);
		
		return "newMachinePage";
	}
	
	@RequestMapping(value = "/noviStroj", method = RequestMethod.POST)
	public String saveNewMachine(Model model, @RequestParam("name") String name, Principal principal) {
		//System.out.println(form);
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		List<StrojENTITY> machines = strojDAO.getMachines(user.getID());
		
		boolean b = false;
		for(StrojENTITY machine: machines) {
			if(machine.getNaziv().toUpperCase().equals(name.toUpperCase())) {
				b = true;
				break;
			}
		}
		
		if(b) {
			model.addAttribute("user", user);
			model.addAttribute("error", "Već imate taj stroj");
			return "newMachinePage";
		}
		
		model.addAttribute("user", user);
		strojDAO.insertMachine(name, user);
		
		return "redirect:/korisnik/strojevi";
	}
	
	@RequestMapping(value = "/stroj{strojID}", method = RequestMethod.GET)
	public String getMachine(Model model, Principal principal, @PathVariable("strojID") int strojID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		StrojENTITY stroj = strojDAO.machine(strojID);
		
		List<PosaoENTITY> jobs = posaoDAO.getJobs(user.getID());
		List<PosaoENTITY> popis = strojDAO.jobList(user.getID(), strojID, jobs);
		List<ServisENTITY> services = servisDAO.getServices(user.getID(), strojID);
		List<Kalendar> list = new LinkedList<>();
		List<KalendarServis> list2 = new LinkedList<>();
		List<Kalendar> list3 = new LinkedList<>();
		
		for(PosaoENTITY posao: popis) {
			Kalendar p = new Kalendar(posao.getNaziv(), posao.getDatumPocetka().toString(), posao.getDatumKraja().toString());
			list.add(p);
			list3.add(p);
		}
		
		for(ServisENTITY servis: services) {
			KalendarServis p = new KalendarServis(servis.getDatumPočetka().toString(), servis.getDatumKraja().toString(), servis.getCijena());
			list2.add(p);
			
			Kalendar p2 = new Kalendar("servis", servis.getDatumPočetka().toString(), servis.getDatumKraja().toString());
			list.add(p2);
		}
		
		Collections.sort(list, new Comparator<Kalendar>() {
			@Override
			public int compare(Kalendar o1, Kalendar o2) {
				return o1.getStart().compareTo(o2.getStart());
			}
		});
		
		Collections.sort(list3, new Comparator<Kalendar>() {
			@Override
			public int compare(Kalendar o1, Kalendar o2) {
				return o1.getStart().compareTo(o2.getStart());
			}
		});
		
		Collections.sort(list2, new Comparator<KalendarServis>() {
			@Override
			public int compare(KalendarServis o1, KalendarServis o2) {
				return o1.getStart().compareTo(o2.getStart());
			}
		});
		
		model.addAttribute("user", user);
		model.addAttribute("stroj", stroj);
		model.addAttribute("list", list);
		model.addAttribute("list2", list2);
		model.addAttribute("list3", list3);
//		model.addAttribute("list2", list2);
//		model.addAttribute("list3", list3);
		
		return "machinePage";
	}
	
	@RequestMapping(value = "/ukloniStroj{ID}", method = RequestMethod.POST)
	public String removeMachine(Model model, Principal principal, @PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
//		List<PosaoStrojENTITY> list = posaoStrojDAO.machineForJobs(user.getID(), ID);
//		if(!list.isEmpty()) {
//			return "redirect:/korisnik/stroj" + ID;
//		}
		
		
		strojDAO.deleteMachine(ID, user.getID());
		
		return "redirect:/korisnik/strojevi";
	}
	
	@RequestMapping(value = "/stroj{ID}/uredi", method = RequestMethod.GET)
	public String urediStrojPage(Model model, Principal principal, @PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		StrojENTITY stroj = strojDAO.machine(ID);
		
		model.addAttribute("user", user);
		model.addAttribute("stroj", stroj);
		
		return "updateMachinePage";
	}
	
	@RequestMapping(value = "/stroj{ID}/uredi", method = RequestMethod.POST)
	public String urediStroj(Model model, @RequestParam("name") String name, @PathVariable("ID") int ID, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		List<StrojENTITY> machines = strojDAO.getMachines(user.getID());
		StrojENTITY stroj = strojDAO.machine(ID);
		machines.remove(stroj);
		
		boolean b = false;
		for(StrojENTITY machine: machines) {
			if(machine.getNaziv().toUpperCase().equals(name.toUpperCase())) {
				b = true;
				break;
			}
		}
		
		if(b) {
			model.addAttribute("user", user);
			model.addAttribute("stroj", stroj);
			model.addAttribute("error", "Već imate taj stroj");
			return "updateMachinePage";
		}
		
		strojDAO.updateMachine(ID, user.getID(), name);
		
		return "redirect:/korisnik/stroj" + ID;
	}
	
	@RequestMapping(value = "/stroj{ID}/servis", method = RequestMethod.GET)
	public String getServicePage(Model model, Principal principal, @PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		ServisForm form = new ServisForm();
		model.addAttribute("servisForm", form);
		StrojENTITY stroj = strojDAO.machine(ID);
		
		model.addAttribute("user", user);
		model.addAttribute("stroj", stroj);
		
		return "servicePage";
	}
	
	@RequestMapping(value = "/stroj{ID}/servis", method = RequestMethod.POST)
	public String saveService(Model model, @ModelAttribute("servisForm") ServisForm form, Principal principal, 
																							@PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		System.out.println(form.getDateStart());
		System.out.println(form.getDateEnd());
		System.out.println(form.getCost());
		
		LocalDate d1 = LocalDate.parse(form.getDateStart());
		LocalDate d2 = LocalDate.parse(form.getDateEnd());
		long h1 = d1.toEpochDay();
		long h2 = d2.toEpochDay();
		
		if(h1 > h2) {
			ServisForm form2 = new ServisForm();
			StrojENTITY stroj = strojDAO.machine(ID);
			
			model.addAttribute("user", user);
			model.addAttribute("servisForm", form2);
			model.addAttribute("stroj", stroj);
			model.addAttribute("error", "Datumi moraju biti valjani");
			
			return "servicePage";
		}
		
		List<PosaoENTITY> jobs = posaoDAO.getJobs(user.getID());
		List<PosaoENTITY> popis = strojDAO.jobList(user.getID(), ID, jobs);
		List<ServisENTITY> services = servisDAO.getServices(user.getID(), ID);
		boolean check = servisDAO.checkServis(d1, d2, popis, services);
		
		if(check) {
			ServisForm form2 = new ServisForm();
			StrojENTITY stroj = strojDAO.machine(ID);
			
			model.addAttribute("user", user);
			model.addAttribute("servisForm", form2);
			model.addAttribute("stroj", stroj);
			model.addAttribute("error", "Ne može se poklapati s poslovima ili drugim servisima");
			
			return "servicePage";
		}
		
		Collections.sort(jobs, new Comparator<PosaoENTITY>() {
			@Override
			public int compare(PosaoENTITY o1, PosaoENTITY o2) {
				int d1 = o1.getDatumKraja().compareTo(o2.getDatumKraja());
				int d2 = o1.getDatumPocetka().compareTo(o2.getDatumPocetka());
				int d3 = o1.getNaziv().compareTo(o2.getNaziv());
				
				if(d1 == 0) {
					if(d2 == 0) {
						return d3;
					} else {
						return d2;
					}
				} else {
					return d1;
				}
				
				//return o1.getDatumKraja().compareTo(o2.getDatumKraja());
			}
		});
		
		List<ServisTrosak> stList = new LinkedList<>();
		double cost = form.getCost();
		double zarada = 0;
		double troškovi = 0;
		boolean b = false;
		
		for(PosaoENTITY posao: jobs) {
			if(posao.getDatumKraja().toEpochDay() < d1.toEpochDay()) {
				if(posao.getZarada() >= cost) {
					zarada = posao.getZarada() - cost;
					troškovi = posao.getTroškovi() + cost;
					posaoDAO.setNewIncomeForJob(posao.getID(), zarada);
					posaoDAO.setNewCostForJob(posao.getID(), troškovi);
					
					PosaoServisENTITY posaoServis = servisDAO.jobService(user.getID(), posao.getID());
					double trošak = posaoServis.getTrošak() + cost;
					servisDAO.setJobServisCost(user.getID(), posao.getID(), trošak);
					b = true;
					break;
				} else {
					zarada = 0;
					troškovi = posao.getTroškovi() + posao.getZarada();
					cost -= posao.getZarada();
					ServisTrosak st = new ServisTrosak(posao.getID(), posao.getZarada(), troškovi);
					stList.add(st);
//					posaoDAO.setNewIncomeForJob(posao.getID(), zarada);
//					posaoDAO.setNewCostForJob(posao.getID(), troškovi);
				}
			}
		}
		
		if(b) {
			for(ServisTrosak st: stList) {
				posaoDAO.setNewIncomeForJob(st.getId(), 0);
				posaoDAO.setNewCostForJob(st.getId(), st.getTroškovi());
				
				PosaoServisENTITY posaoServis = servisDAO.jobService(user.getID(), st.getId());
				double trošak = posaoServis.getTrošak() + st.getZarada();
				servisDAO.setJobServisCost(user.getID(), st.getId(), trošak);
			}
			servisDAO.setServis(user.getID(), ID, d1, d2, form.getCost());
		} else {
			ServisForm form2 = new ServisForm();
			StrojENTITY stroj = strojDAO.machine(ID);
			
			model.addAttribute("user", user);
			model.addAttribute("servisForm", form2);
			model.addAttribute("stroj", stroj);
			model.addAttribute("error", "Nemate novaca");
			
			return "servicePage";
		}
		
		
		return "redirect:/korisnik/stroj" + ID;
	}
	
	@RequestMapping(value = "/radnici", method = RequestMethod.GET)
	public String workers(Model model, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		System.out.println(user.getID());
		List<RadnikENTITY> workers = radnikDAO.getWorkers(user.getID());
		
		model.addAttribute("user", user);
		model.addAttribute("workers", workers);
		
		return "workersListPage";
	}
	
	@RequestMapping(value = "/noviRadnik", method = RequestMethod.GET)
	public String getNewWorkerPage(Model model, Principal principal) {
		RadnikForm form = new RadnikForm();
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		
		model.addAttribute("user", user);
		model.addAttribute("radnikForm", form);
		
		return "newWorkerPage";
	}
	
	@RequestMapping(value = "/noviRadnik", method = RequestMethod.POST)
	public String saveNewWorker(Model model, @ModelAttribute("radnikForm") RadnikForm form, Principal principal) {
		System.out.println(form);
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		List<RadnikENTITY> list = radnikDAO.getWorkers(user.getID());
		boolean b = false;
		
		for(RadnikENTITY r: list) {
			if(r.getKorisnickoIme().equals(form.getUserName())) {
				b = true;
				break;
			}
		}
		
		if(b) {
			RadnikForm form2 = new RadnikForm();
			model.addAttribute("radnikForm", form2);
			model.addAttribute("user", user);
			model.addAttribute("error", "Već postoji radnik s tim korisničkim imenom");
			
			return "newWorkerPage";
		}
		
		
		model.addAttribute("user", user);
		radnikDAO.insertWorker(form, user);
		
		return "redirect:/korisnik/radnici";
	}
	
	@RequestMapping(value = "/radnik{ID}", method = RequestMethod.GET)
	public String getWorker(Model model, Principal principal, @PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		RadnikENTITY radnik = radnikDAO.worker(ID);
		
		List<PosaoENTITY> jobs = posaoDAO.getJobs(user.getID());
		List<PosaoENTITY> popis = radnikDAO.jobList(user.getID(), ID, jobs);
		List<Kalendar> list = new LinkedList<>();
		List<KalendarRadnik> list2 = new LinkedList<>();
		
		for(PosaoENTITY posao: popis) {
			Kalendar p = new Kalendar(posao.getNaziv(), posao.getDatumPocetka().toString(), posao.getDatumKraja().toString());
			list.add(p);
			
			PosaoRadnikENTITY pr = posaoRadnikDAO.jobWorker(user.getID(), posao.getID(), ID);
			KalendarRadnik p2 = new KalendarRadnik(posao.getNaziv(), posao.getDatumPocetka().toString(), posao.getDatumKraja().toString(), pr.getIsplata());
			list2.add(p2);
		}
		
		Collections.sort(list, new Comparator<Kalendar>() {
			@Override
			public int compare(Kalendar o1, Kalendar o2) {
				return o1.getStart().compareTo(o2.getStart());
			}
		});
		
		Collections.sort(list2, new Comparator<KalendarRadnik>() {
			@Override
			public int compare(KalendarRadnik o1, KalendarRadnik o2) {
				return o1.getStart().compareTo(o2.getStart());
			}
		});
		
		model.addAttribute("user", user);
		model.addAttribute("radnik", radnik);
		model.addAttribute("list", list);
		model.addAttribute("list2", list2);
		
		return "workerPage";
	}
	
	@RequestMapping(value = "/radnik{ID}/uredi", method = RequestMethod.GET)
	public String urediRadnikPage(Model model, Principal principal, @PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		RadnikENTITY radnik = radnikDAO.worker(ID);
		
		model.addAttribute("user", user);
		model.addAttribute("radnik", radnik);
		
		return "updateWorkerPage";
	}
	
	@RequestMapping(value = "/radnik{ID}/uredi", method = RequestMethod.POST)
	public String urediRadnika(Model model, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, 
									@RequestParam("userName") String userName, @PathVariable("ID") int ID, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		List<RadnikENTITY> workers = radnikDAO.getWorkers(user.getID());
		RadnikENTITY radnik = radnikDAO.worker(ID);
		workers.remove(radnik);
		
		boolean b = false;
		for(RadnikENTITY worker: workers) {
			if(worker.getKorisnickoIme().equals(userName)) {
				b = true;
				break;
			}
		}
		
		if(b) {
			model.addAttribute("user", user);
			model.addAttribute("radnik", radnik);
			model.addAttribute("error", "Već postoji radnik s tim korisničkim imenom");
			
			return "updateWorkerPage";
		}
		
		radnikDAO.updateWorker(user.getID(), ID, firstName, lastName, userName);
		
		return "redirect:/korisnik/radnik" + ID;
	}
	
	@RequestMapping(value = "/ukloniRadnika{ID}", method = RequestMethod.POST)
	public String removeWorker(Model model, Principal principal, @PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
//		List<PosaoStrojENTITY> list = posaoStrojDAO.machineForJobs(user.getID(), ID);
//		if(!list.isEmpty()) {
//			return "redirect:/korisnik/stroj" + ID;
//		}
		
		
		radnikDAO.deleteWorker(ID, user.getID());
		
		return "redirect:/korisnik/radnici";
	}
	
	@RequestMapping(value = "/poslovi", method = RequestMethod.GET)
	public String jobs(Model model, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		System.out.println(user.getID());
		List<PosaoENTITY> jobs = posaoDAO.getJobs(user.getID());
		//Comparator<PosaoENTITY> comp = Comparator.comparing(p -> p.datumPočetka().toString());
		Collections.sort(jobs, new Comparator<PosaoENTITY>() {
			@Override
			public int compare(PosaoENTITY o1, PosaoENTITY o2) {
				int d1 = o1.getDatumPocetka().compareTo(o2.getDatumPocetka());
				
				if(d1 == 0) {
					int d2 = o1.getDatumKraja().compareTo(o2.getDatumKraja());
					if(d2 == 0) {
						int d3 = o1.getNaziv().compareTo(o2.getNaziv());
						return d3;
					} else {
						return d2;
					}
				}
				
				return d1;
			}
		});
		
		model.addAttribute("user", user);
		model.addAttribute("jobs", jobs);
		
		return "jobsListPage";
	}
	
	@RequestMapping(value = "/noviPosao", method = RequestMethod.GET)
	public String getNewJobPage(Model model, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		PosaoForm form = new PosaoForm();
		
		model.addAttribute("user", user);
		model.addAttribute("posaoForm", form);
		
		return "newJobPage";
	}
	
	@RequestMapping(value = "/noviPosao", method = RequestMethod.POST)
	public String setJob(Model model, @ModelAttribute("posaoForm") PosaoForm form, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		List<PosaoENTITY> list = posaoDAO.getJobs(user.getID());
		boolean b = false;
		
		for(PosaoENTITY posao: list) {
			if(posao.getNaziv().toUpperCase().equals(form.getName().toUpperCase())) {
				b = true;
				break;
			}
		}
		
		if(b) {
			PosaoForm form2 = new PosaoForm();
			
			model.addAttribute("user", user);
			model.addAttribute("posaoForm", form2);
			model.addAttribute("error", "Već imate posao s tim nazivom");
			
			return "newJobPage";
		}
		
		long d1 = LocalDate.parse(form.getDateStart()).toEpochDay();
		long d2 = LocalDate.parse(form.getDateEnd()).toEpochDay();
		
		if(d1 > d2) {
			PosaoForm form2 = new PosaoForm();
			
			model.addAttribute("user", user);
			model.addAttribute("posaoForm", form2);
			model.addAttribute("error", "Datumi moraju biti valjani");
			
			return "newJobPage";
		}
		
		posaoDAO.insertJob(form, user);
		
		List<PosaoENTITY> jobs = posaoDAO.allJobs();
		int posaoID = jobs.size();
		servisDAO.insertJobServis(user.getID(), posaoID);
		
		//return "redirect:/korisnik/resursi";
		return "redirect:/korisnik/poslovi";
	}
	
	@RequestMapping(value = "/posao{ID}", method = RequestMethod.GET)
	public String getJob(Model model, Principal principal, @PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		PosaoENTITY posao = posaoDAO.job(ID);
		
		List<StrojENTITY> machines = strojDAO.getMachines(user.getID());
		List<StrojENTITY> machinesReady = posaoStrojDAO.showMachinesForJob(user.getID(), ID, machines);
		
		List<RadnikENTITY> workers = radnikDAO.getWorkers(user.getID());
		List<Radnik> radnici = posaoRadnikDAO.showWorkersForJob(user.getID(), ID, workers);
		
		List<MaterijalENTITY> materials = posaoMaterijalDAO.materials();
		List<Materijal> materijali = posaoMaterijalDAO.showMaterialsForJob(user.getID(), ID, materials);
		
		model.addAttribute("user", user);
		model.addAttribute("posao", posao);
		model.addAttribute("machinesReady", machinesReady);
		model.addAttribute("radnici", radnici);
		model.addAttribute("materijali", materijali);
		
		List<Pie> pieList = new LinkedList<>();
		double sum = 0;
		
		List<PosaoRadnikENTITY> posaoRadnikList = posaoRadnikDAO.getJobWorkers(user.getID(), ID);
		for(PosaoRadnikENTITY pr: posaoRadnikList) {
			sum += pr.getIsplata();
		}
		
		if(sum != 0) {
			Pie pie = new Pie("Radnici", sum);
			pieList.add(pie);
		}
		
		sum = 0;
		List<PosaoMaterijalENTITY> posaoMaterijalList = posaoMaterijalDAO.getJobMaterials(user.getID(), ID);
		for(PosaoMaterijalENTITY pm: posaoMaterijalList) {
			sum += pm.getCijena();
		}
		
		if(sum != 0) {
			Pie pie = new Pie("Materijali", sum);
			pieList.add(pie);
		}
		
		PosaoServisENTITY posaoServis = servisDAO.jobService(user.getID(), ID);
		if(posaoServis.getTrošak() != 0) {
			Pie pie = new Pie("Servisi", posaoServis.getTrošak());
			pieList.add(pie);
		}
		
		Collections.sort(pieList, new Comparator<Pie>() {
			@Override
			public int compare(Pie o1, Pie o2) {
				if(o1.getTrošak() == o2.getTrošak()) {
					return o1.getNaziv().compareTo(o2.getNaziv());
				} else if(o1.getTrošak() > o2.getTrošak()) {
					return -1;
				} else {
					return 1;
				}
			}
		});
		
		model.addAttribute("pieList", pieList);
		
		return "jobPage";
	}
	
	@RequestMapping(value = "/posao{ID}/uredi", method = RequestMethod.GET)
	public String urediPosaoPage(Model model, Principal principal, @PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		PosaoENTITY posao = posaoDAO.job(ID);
		
		model.addAttribute("user", user);
		model.addAttribute("posao", posao);
		
		return "updateJobPage";
	}
	
	@RequestMapping(value = "/posao{ID}/uredi", method = RequestMethod.POST)
	public String urediPosao(Model model, Principal principal, @PathVariable("ID") int ID, 
			@RequestParam("name") String name, @RequestParam("address") String address, @RequestParam("object") String object) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		List<PosaoENTITY> list = posaoDAO.getJobs(user.getID());
		PosaoENTITY posao = posaoDAO.job(ID);
		list.remove(posao);
		boolean b = false;
		
		for(PosaoENTITY job: list) {
			if(job.getNaziv().equals(name)) {
				b = true;
				break;
			}
		}
		
		if(b) {
			model.addAttribute("user", user);
			model.addAttribute("posao", posao);
			model.addAttribute("error", "Već imate posao s tim nazivom");
			
			return "updateJobPage";
		}
		
		posaoDAO.updateJob(user.getID(), ID, name, address, object);
		
		return "redirect:/korisnik/posao" + ID;
	}
	
	@RequestMapping(value = "/dodajStroj{ID}", method = RequestMethod.GET)
	public String getMachineForJobPage(Model model, Principal principal, @PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		PosaoENTITY job = posaoDAO.job(ID);
		List<StrojENTITY> machines = strojDAO.getMachines(user.getID());
		List<PosaoENTITY> jobs = posaoDAO.getJobs(user.getID());
		
		List<StrojENTITY> machinesReady = posaoStrojDAO.choices(user.getID(), job, jobs, machines);
		for(StrojENTITY stroj: machinesReady) {
			System.out.println(stroj.getNaziv());
		}
		
		CheckForm form = new CheckForm();
		List<Integer> checkedItems = new LinkedList<>();
		form.setCheckedItems(checkedItems);
		
		model.addAttribute("user", user);
		model.addAttribute("machinesReady", machinesReady);
		model.addAttribute("posao", job);
		model.addAttribute("checkForm", form);
		
		return "machinesForJob";
	}
	
	@RequestMapping(value = "/dodajStroj{ID}", method = RequestMethod.POST)
	public String saveMachineForJob(Model model, @ModelAttribute("checkForm") CheckForm form, Principal principal, 
																							@PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		System.out.println(form.getCheckedItems());
		
		for(Integer strojID: form.getCheckedItems()) {
			System.out.println(user.getID());
			System.out.println(ID);
			System.out.println(strojID);
			posaoStrojDAO.setMachinesForJob(user.getID(), ID, strojID);
		}
		
		return "redirect:/korisnik/posao" + ID;
	}
	
	@RequestMapping(value = "/makniStroj/{posaoID}/{strojID}", method = RequestMethod.GET)
	public String removeMachineForJob(Model model, Principal principal, @PathVariable("posaoID") int posaoID, 
																					@PathVariable("strojID") int strojID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		posaoStrojDAO.removeMachinesForJob(user.getID(), posaoID, strojID);
		
		return "redirect:/korisnik/posao" + posaoID;
	}
	
	@RequestMapping(value = "/dodajRadnika{ID}", method = RequestMethod.GET)
	public String getWorkerForJobPage(Model model, Principal principal, @PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		PosaoENTITY job = posaoDAO.job(ID);
		List<RadnikENTITY> workers = radnikDAO.getWorkers(user.getID());
		List<PosaoENTITY> jobs = posaoDAO.getJobs(user.getID());
		
		List<RadnikENTITY> workersReady = posaoRadnikDAO.choices(user.getID(), job, jobs, workers);
		for(RadnikENTITY radnik: workersReady) {
			System.out.println(radnik.getIme() + " " + radnik.getPrezime());
		}
		
		CheckForm form = new CheckForm();
		List<Integer> checkedItems = new LinkedList<>();
		form.setCheckedItems(checkedItems);
		
		model.addAttribute("user", user);
		model.addAttribute("workersReady", workersReady);
		model.addAttribute("posao", job);
		model.addAttribute("checkForm", form);
		
		return "workersForJob";
	}
	
	@RequestMapping(value = "/dodajRadnika{posaoID}/{radnikID}", method = RequestMethod.GET)
	public String paymentWorkerForJob(Model model, Principal principal, @PathVariable("posaoID") int posaoID, 
																						@PathVariable("radnikID") int radnikID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		RadnikENTITY radnik = radnikDAO.worker(radnikID);
		
		model.addAttribute("user", user);
		model.addAttribute("posaoID", posaoID);
		model.addAttribute("radnik", radnik);
		
		return "paymentForWorker";
	}
	
	@RequestMapping(value = "/dodajRadnika{posaoID}/{radnikID}", method = RequestMethod.POST)
	public String saveWorkerForJob(Model model, Principal principal, @PathVariable("posaoID") int posaoID, 
											 @PathVariable("radnikID") int radnikID, @RequestParam("isplata") double isplata) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		PosaoENTITY job = posaoDAO.job(posaoID);
		double zarada = job.getZarada() - isplata;
		double troškovi = job.getTroškovi() + isplata;
		
		if(zarada < 0) {
			RadnikENTITY radnik = radnikDAO.worker(radnikID);
			model.addAttribute("user", user);
			model.addAttribute("posaoID", posaoID);
			model.addAttribute("radnik", radnik);
			model.addAttribute("error", "Ne možeš ići u minus");
			return "paymentForWorker";
		}
		
		posaoRadnikDAO.setWorkerForJob(user.getID(), posaoID, radnikID, isplata);
		posaoDAO.setNewIncomeForJob(posaoID, zarada);
		posaoDAO.setNewCostForJob(posaoID, troškovi);
		
		return "redirect:/korisnik/posao" + posaoID;
	}
	
	@RequestMapping(value = "/makniRadnika/{posaoID}/{radnikID}", method = RequestMethod.GET)
	public String removeWorkerForJob(Model model, Principal principal, @PathVariable("posaoID") int posaoID, 
																					@PathVariable("radnikID") int radnikID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		PosaoRadnikENTITY posaoRadnik = posaoRadnikDAO.jobWorker(user.getID(), posaoID, radnikID);
		PosaoENTITY job = posaoDAO.job(posaoID);
		double isplata = posaoRadnik.getIsplata();
		double zarada = job.getZarada() + isplata;
		double troškovi = job.getTroškovi() - isplata;
		
		posaoRadnikDAO.removeWorkerForJob(user.getID(), posaoID, radnikID);
		posaoDAO.setNewIncomeForJob(posaoID, zarada);
		posaoDAO.setNewCostForJob(posaoID, troškovi);
		
		return "redirect:/korisnik/posao" + posaoID;
	}
	
	@RequestMapping(value = "/dodajMaterijal{ID}", method = RequestMethod.GET)
	public String getMaterialForJobPage(Model model, Principal principal, @PathVariable("ID") int ID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		MaterijalForm form = new MaterijalForm();
		
		model.addAttribute("user", user);
		model.addAttribute("posaoID", ID);
		model.addAttribute("materijalForm", form);
		
		return "materialForJob";
	}
	
	@RequestMapping(value = "/dodajMaterijal{posaoID}", method = RequestMethod.POST)
	public String saveMaterialForJob(Model model, @ModelAttribute("materijalForm") MaterijalForm form, Principal principal, 
																							@PathVariable("posaoID") int posaoID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		PosaoENTITY job = posaoDAO.job(posaoID);
		double cijena = form.getPrice();
		double zarada = job.getZarada() - cijena;
		double troškovi = job.getTroškovi() + cijena;
		
		if(zarada < 0) {
			MaterijalForm materijalForm = new MaterijalForm();
			model.addAttribute("user", user);
			model.addAttribute("posaoID", posaoID);
			model.addAttribute("materijalForm", materijalForm);
			model.addAttribute("error", "Ne možeš ići u minus");
			
			return "materialForJob";
		}
		
		List<MaterijalENTITY> list = materijalDAO.materials();
		int materialID = posaoMaterijalDAO.getMaterialID(form.getName(), list);
		
		if(materialID == 0) {
			posaoMaterijalDAO.insertMaterial(form.getName());
			materialID = list.size() + 1;
		}
		
		PosaoMaterijalENTITY pm = posaoMaterijalDAO.jobMaterial(user.getID(), posaoID, materialID);
		
		if(pm != null) {
			MaterijalForm form2 = new MaterijalForm();
			
			model.addAttribute("user", user);
			model.addAttribute("posaoID", posaoID);
			model.addAttribute("materijalForm", form2);
			model.addAttribute("error", "Već imate taj materijal");
			
			return "materialForJob";
		}
		
		posaoMaterijalDAO.setMaterialForJob(user.getID(), posaoID, materialID, cijena);
		posaoDAO.setNewIncomeForJob(posaoID, zarada);
		posaoDAO.setNewCostForJob(posaoID, troškovi);
		
		return "redirect:/korisnik/posao" + posaoID;
	}
	
	@RequestMapping(value = "/makniMaterijal/{posaoID}/{materialID}", method = RequestMethod.GET)
	public String removeMaterialForJob(Model model, Principal principal, @PathVariable("posaoID") int posaoID, 
																					@PathVariable("materialID") int materialID) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		PosaoMaterijalENTITY posaoMaterijal = posaoMaterijalDAO.jobMaterial(user.getID(), posaoID, materialID);
		PosaoENTITY job = posaoDAO.job(posaoID);
		double cijena = posaoMaterijal.getCijena();
		double zarada = job.getZarada() + cijena;
		double troškovi = job.getTroškovi() - cijena;
		
		posaoMaterijalDAO.removeMaterialForJob(user.getID(), posaoID, materialID);
		posaoDAO.setNewIncomeForJob(posaoID, zarada);
		posaoDAO.setNewCostForJob(posaoID, troškovi);
		
		return "redirect:/korisnik/posao" + posaoID;
	}
	
	@RequestMapping(value = "/zarade", method = RequestMethod.GET)
	public String chartEarnings(Model model, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		List<PosaoENTITY> list = posaoDAO.getJobs(user.getID());
		
		Collections.sort(list, new Comparator<PosaoENTITY>() {
			@Override
			public int compare(PosaoENTITY o1, PosaoENTITY o2) {
				int d1 = o1.getDatumKraja().compareTo(o2.getDatumKraja());
				int d2 = o1.getDatumPocetka().compareTo(o2.getDatumPocetka());
				int d3 = o1.getNaziv().compareTo(o2.getNaziv());
				
				if(d1 == 0) {
					if(d2 == 0) {
						return d3;
					} else {
						return d2;
					}
				} else {
					return d1;
				}
				
				//return o1.getDatumPocetka().compareTo(o2.getDatumPocetka());
			}
		});
		
		List<Line> list2 = new LinkedList<>();
		for(PosaoENTITY posao: list) {
			Line l = new Line(posao.getNaziv(), posao.getZarada());
			list2.add(l);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("list2", list2);
		
		return "earningsLineChart";
	}
	
	@RequestMapping(value = "/troškovi", method = RequestMethod.GET)
	public String chartCosts(Model model, Principal principal) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		List<PosaoENTITY> list = posaoDAO.getJobs(user.getID());
		
		Collections.sort(list, new Comparator<PosaoENTITY>() {
			@Override
			public int compare(PosaoENTITY o1, PosaoENTITY o2) {
				int d1 = o1.getDatumKraja().compareTo(o2.getDatumKraja());
				int d2 = o1.getDatumPocetka().compareTo(o2.getDatumPocetka());
				int d3 = o1.getNaziv().compareTo(o2.getNaziv());
				
				if(d1 == 0) {
					if(d2 == 0) {
						return d3;
					} else {
						return d2;
					}
				} else {
					return d1;
				}
				
				//return o1.getDatumPocetka().compareTo(o2.getDatumPocetka());
			}
		});
		
		List<Line> list2 = new LinkedList<>();
		for(PosaoENTITY posao: list) {
			Line l = new Line(posao.getNaziv(), posao.getTroškovi());
			list2.add(l);
		}
		
		model.addAttribute("user", user);
		model.addAttribute("list2", list2);
		
		return "costsLineChart";
	}
	
	@RequestMapping(value = "/excel", method = RequestMethod.GET)
	public void exportToExcel(Model model, Principal principal, HttpServletResponse response) {
		Korisnik user = (Korisnik) ((Authentication) principal).getPrincipal();
		List<PosaoENTITY> list = posaoDAO.getJobs(user.getID());
		
		Collections.sort(list, new Comparator<PosaoENTITY>() {
			@Override
			public int compare(PosaoENTITY o1, PosaoENTITY o2) {
				int d1 = o1.getDatumPocetka().compareTo(o2.getDatumPocetka());
				
				if(d1 == 0) {
					int d2 = o1.getDatumKraja().compareTo(o2.getDatumKraja());
					if(d2 == 0) {
						int d3 = o1.getNaziv().compareTo(o2.getNaziv());
						return d3;
					} else {
						return d2;
					}
				}
				
				return d1;
			}
		});
		
		ExcelExporter exp = new ExcelExporter();
		try {
			exp.export(list, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}